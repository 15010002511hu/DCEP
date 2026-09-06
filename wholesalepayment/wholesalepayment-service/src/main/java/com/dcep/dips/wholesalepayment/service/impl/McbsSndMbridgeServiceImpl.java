package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.api.McbsSndMbridgeService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.*;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ChainTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.ChainTransDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.ClearingCheckModeEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.McbsStatusEnum;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeToDcepConvertManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.gateway.mcbdc.dto.mcbs204.Mcbs20400101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dcepex.trace.support.async.TraceExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@DubboService
public class McbsSndMbridgeServiceImpl implements McbsSndMbridgeService {
    ExecutorService asyncPool1 = new TraceExecutorService(ThreadPoolUtils.getThreadPoolFixSize(100, "mBridge2Hlht-async"));

    @Autowired
    CommonManager commonManager;

    @Autowired
    MbridgeToDcepConvertManager mbridgeToDcepConvertManager;

    @Autowired
    MbridgeManager mbridgeManager;

    @Autowired
    SettlementProdMapper settlementProdMapper;

    @Autowired
    AccountingManager accountingManager;

    @Autowired
    ChainTransMapper chainTransMapper;

    @Autowired
    AccountingInstrMapper accountingInstrMapper;

    @Override
    public Response<GenericEnvelopeDTO<GenericGwDTO>> process(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO) throws DcepException {
        // 报文转换
        EnvelopeDTO<GwDTO> gwDTOEnvelopeDTO = mbridgeToDcepConvertManager.convertRequest(genericGwReqDTO);
        ClearingDTO clearingDTO = (ClearingDTO) gwDTOEnvelopeDTO.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);
        MbridgeReqDTO mbridgeDTO = clearingDTO.clrMbridgeInf();
        // 生成dcep报文标识号,赋值dcep报文
        String dcepMsgId = MsgIdUtil.genDcepMsgId(clearingDTO, mbridgeDTO.getShardingMsgId());
        clearingDTO.fillMsgId(dcepMsgId, dcepMsgId);
        log.info("mcbs send service: convertRequest succ, dcepMsgId={}, mcbsMsgId={}", clearingDTO.getClrMsgId(), mbridgeDTO.getMcbsMsgId());
        // 1.业务检查
        if (clearing.checkMode() != ClearingCheckModeEnum.NON_CHECK) {
            ClearingProdErrorEnum errorEnum = checkBusinessInfo(clearingDTO, TimeUtil.fromMcbs(mbridgeDTO.getSndDtTm(), DcepDateUtils.ISO_DATETIME_PATTERN).substring(0, 10).replace("-", ""));
            if (errorEnum != null) {
                return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(errorEnum.getCode())));
            }
        }
        // 2.交易登记-》幂等处理
        // 调用交易登记组件
        AccountingInstrDO accountingInstrDO;
        try {
            log.info("mcbs send service: record start, dcepMsgId={}", clearingDTO.getClrMsgId());
            accountingInstrDO = mbridgeManager.prepare(gwDTOEnvelopeDTO, genericGwReqDTO);
            log.info("mcbs send service: record end, dcepMsgId={}, mcbsMsgId={}", clearingDTO.getClrMsgId(), mbridgeDTO.getMcbsMsgId());
        } catch (DuplicateKeyException e) {
            // 幂等
            log.info("duplicationException: dcepMsgId={}, msgTp={}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp(), e);
            Response<GenericEnvelopeDTO<GenericGwDTO>> genericGwRespDTO = dealDuplicateKeyException(clearingDTO, genericGwReqDTO, dcepMsgId, mbridgeDTO, clearing);
            if (genericGwRespDTO != null){
                return genericGwRespDTO;
            }
            // 幂等条件符合，查询记账指令表获取原记账信息
            accountingInstrDO = accountingInstrMapper.selectByMsgId(clearingDTO.getClrMsgId());
        }
        // 3.异步调用结算钱包记账
        // 补充业务通信层标识
        gwDTOEnvelopeDTO.getSoapHeader().setMsgSN(dcepMsgId + CommonConstant.SysCode.WHOLESALE);
        try {
            AccountingInstrDO finalAccountingInstrDO = accountingInstrDO;
            asyncPool1.execute(() -> {
                mbridgeManager.mBridge2HlhtAsync(genericGwReqDTO, gwDTOEnvelopeDTO, finalAccountingInstrDO);
            });
        } catch (RejectedExecutionException e) {
            log.error("async thread pool is full, msgId={}", clearingDTO.getClrMsgId(), e);
        }
        return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.RSVL.getCode(), Constant.MBRIDGE_REQHDLG_DESC_SUCC));
    }

    @Override
    public Response<GenericEnvelopeDTO<GenericGwDTO>> querySts(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO) throws DcepException {
        Mcbs20400101DTO mcbs204DTO = (Mcbs20400101DTO) genericGwReqDTO.body();
        MbridgeReqDTO mbridgeReqDTO = getMbridgeReqDTO(genericGwReqDTO);

        // 1.通过原mcbsMsgId查询区块链对接产品表
        String originMsgId = mcbs204DTO.getOriginGroupInfo().getOriginMsgId();
        ChainTransDO orgChainTransDO = chainTransMapper.selectByPrimaryKey(originMsgId);
        if (orgChainTransDO == null) {
            log.error("onMbridge query service: select chainTrans failed, no original transaction record, orgMcbsMsgId={}", originMsgId);
            return new Response<>(DtoUtil.assemblyMcbs203(genericGwReqDTO, McbsStatusEnum.EPTY.getCode(), Constant.MBRIDGE_REQHDLG_DESC_NOTMATCH, mbridgeReqDTO));
        }
        log.info("onMbridge query service: select chainTrans succ, orgMcbsMsgId={}", originMsgId);
        // 2.根据区块链对接产品表msgId查询结算产品表
        SettlementProdDO orgSettlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(orgChainTransDO.getMsgId()));

        return new Response<>(DtoUtil.assemblyMcbs203(genericGwReqDTO, statusConvert(orgSettlementProdDO.getBizSts()),
                ClearingStatusEnum.FAILED.getCode().equals(orgSettlementProdDO.getBizSts()) ? ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(orgSettlementProdDO.getBizPrcCd()) : null, mbridgeReqDTO));
    }

    /**
     * 检查业务
     * @param clearingDTO
     * @param curActgDt
     * @return
     */
    private ClearingProdErrorEnum checkBusinessInfo(ClearingDTO clearingDTO, String curActgDt) {
        log.info("mBridge checkBusinessInfo start, dcepMsgId={}", clearingDTO.getClrMsgId());
        // 1.检查收款机构、付款机构状态是否正常
        if (!InfoCacheUtil.checkInstState(clearingDTO.clrSendPtyId())) {
            log.info("sender state illegal, sendPtyId={}, dcepMsgId={}", clearingDTO.clrSendPtyId(), clearingDTO.getClrMsgId());
            return ClearingProdErrorEnum.SENDER_STATE_ILLEGAL;
        }
        if (!InfoCacheUtil.checkInstState(clearingDTO.clrRecvPtyId())) {
            log.info("reciver state illegal, recvPtyId={}, dcepMsgId={}", clearingDTO.clrRecvPtyId(), clearingDTO.getClrMsgId());
            return ClearingProdErrorEnum.RECEIVER_STATE_ILLEGAL;
        }

        // 2.查询系统状态表，检查报文中系统工作日与当前系统工作日是否匹配
        boolean flag = commonManager.checkSystemDate(curActgDt);
        if (!flag){
            log.info("curSystime and reqSystime not match, reqSystime={}, dcepMsgId={}", curActgDt, clearingDTO.getClrMsgId());
            return ClearingProdErrorEnum.CREDTTM_ILLEGAL;
        }

        log.info("mBridge checkBusinessInfo end, dcepMsgId={}", clearingDTO.getClrMsgId());
        return null;
    }

    private Response<GenericEnvelopeDTO<GenericGwDTO>> dealDuplicateKeyException(ClearingDTO clearingDTO, GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO, String dcepMsgId, MbridgeReqDTO mbridgeDTO, Clearing clearing){
        // transId重复生成，返回mcbs900
        if (Constant.INSERT_STEP_THREE.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
            log.error("transId insert repeat: dcepMsgId={}, insertStep={}", clearingDTO.getClrMsgId(), Constant.INSERT_STEP_THREE);
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(ClearingProdErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode())));
        }

        // msgId重复生成，返回mcbs900
        if (Constant.INSERT_STEP_ONE.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
            log.error("msgId insert repeat: dcepMsgId={}, insertStep={}", dcepMsgId, Constant.INSERT_STEP_ONE);
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(ClearingProdErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode())));
        }

        // mcbsMsgId重复
        // select原交易, 并对原交易核心要素幂等要素进行检查
        ChainTransDO chainTransDO = chainTransMapper.selectByPrimaryKey(mbridgeDTO.getMcbsMsgId());
        SettlementProdDO settlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(chainTransDO.getMsgId()));
        // 原交易为终态，返回mcbs900
        if (ClearingStatusEnum.FAILED.getCode().equals(settlementProdDO.getBizSts())
                || ClearingStatusEnum.SETTLED.getCode().equals(settlementProdDO.getBizSts())) {
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(ClearingProdErrorEnum.BUSI_DUPLICATION.getCode())));
        }
        if (!CheckUtil.idempotentMatch(clearingDTO, settlementProdDO, clearing)) {
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), ErrorCodeUtil.getMbridgeStsRsnInfAddtlInf(ClearingProdErrorEnum.NO_MATCH_ORIGNAL.getCode())));
        }
        return null;
    }

    private MbridgeReqDTO getMbridgeReqDTO(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO) {
        String mcbsMsgId = genericGwReqDTO.body().fetchMsgId();
        MbridgeReqDTO mbridgeReqDTO = new MbridgeReqDTO();
        mbridgeReqDTO.setMcbsMsgId(mcbsMsgId);
        mbridgeReqDTO.setShardingMsgId(mcbsMsgId);
        return mbridgeReqDTO;
    }

    private String statusConvert(String status) {
        switch (ClearingStatusEnum.getEnum(status)) {
            case SETTLED:
                return McbsStatusEnum.SUCD.getCode();
            case FAILED:
                return McbsStatusEnum.FAIL.getCode();
            case ACCEPTED:
                return McbsStatusEnum.RSVL.getCode();
            case WAIT_SETTLE:
                return McbsStatusEnum.PDNG.getCode();
            default:
                throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), "未知的结算状态");
        }
    }
}
