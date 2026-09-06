package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.utils.CommonMsgUtils;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.api.DcepSndMbridgeService;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.common.utils.MsgIdUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ChainTransMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ZerooutCtrlDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.ChainTransDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.dto.mcbs.MbridgeReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.*;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.gateway.mcbdc.dto.mcbs203.Mcbs20300101DTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dcepex.trace.support.async.TraceExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

@Slf4j
@DubboService
public class DcepSndMbridgeServiceImpl implements DcepSndMbridgeService {
    ExecutorService asyncPool = new TraceExecutorService(ThreadPoolUtils.getThreadPoolFixSize(100, "hlht2Mbridge-async"));

    @Autowired
    MbridgeManager mbridgeManager;

    @Autowired
    SettlementProdMapper settlementProdMapper;

    @Autowired
    DcepToMbridgeConvertManager dcep2mBridge;

    @Autowired
    AccountingManager accountingManager;

    @Autowired
    ZerooutCtrlDOMapper zerooutCtrlDOMapper;

    @Autowired
    ChainTransMapper chainTransMapper;

    @Autowired
    AccountingInstrMapper accountingInstrMapper;

    @Autowired
    StorageForwardManager storageForwardManager;

    @Autowired
    CommonManager commonManager;

    @Override
    public Response<EnvelopeDTO<GwDTO>> prepare(EnvelopeDTO<GwDTO> gwReqDTO) throws DcepException {
        // 报文转换
        GenericEnvelopeDTO<GenericGwDTO> mBridgeReqEnvelopeDTO = dcep2mBridge.convertRequest(gwReqDTO);
        // 桥下发起dcep203、213请求报文
        ClearingDTO clearingDTO = (ClearingDTO) gwReqDTO.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);
        MbridgeReqDTO mbridgeDTO = clearingDTO.clrMbridgeInf();
        log.info("dcep send service: convertRequest succ msgId={}", clearingDTO.getClrMsgId());
        // 生成mcbs报文标识号,赋值mcbs报文
        String mcbsMsgId = MsgIdUtil.genMcbsMsgId(mbridgeDTO);
        mbridgeDTO.setMcbsMsgId(mcbsMsgId);

        // 1.业务检查
        if (clearing.checkMode() != ClearingCheckModeEnum.NON_CHECK) {
            ZerooutCtrlDO zerooutCtrlDO = zerooutCtrlDOMapper.selecPrcSts(CheckUtil.getBizDt(clearingDTO.clrCreDtTm()), Constant.MCBS);
            ClearingProdErrorEnum errorEnum = checkBusinessInfo(clearingDTO, CheckUtil.getBizDt(clearingDTO.clrCreDtTm()), zerooutCtrlDO != null ? zerooutCtrlDO.getPrcSts() : null);
            if (errorEnum != null) {
                return assembly900Msg(clearingDTO, gwReqDTO, ClearingStatusEnum.FAILED.getCode(), errorEnum);
            }
        }
        AccountingInstrDO accountingInstrDO;
        // 2.调用交易登记组件
        try {
            log.info("dcep send service: record start msgId={}", clearingDTO.getClrMsgId());
            accountingInstrDO = mbridgeManager.prepare(gwReqDTO, mBridgeReqEnvelopeDTO);
            log.info("dcep send service: record end msgId={}", clearingDTO.getClrMsgId());
        } catch (DuplicateKeyException e) {
            // 幂等
            log.info("duplicationException: msgId={}, msgTp={}", clearingDTO.getClrMsgId(), clearingDTO.clrMsgTp(), e);
            Response<EnvelopeDTO<GwDTO>> gwRespDTO = dealDuplicateKeyException(clearingDTO, gwReqDTO, mcbsMsgId, clearing);
            if (gwRespDTO != null){
                return gwRespDTO;
            }
            // 幂等条件符合，查询记账指令表获取原记账信息 todo 返回多条
            accountingInstrDO = accountingInstrMapper.selectByMsgId(clearingDTO.getClrMsgId());
            // 幂等条件符合，查询区块链对接产品表返回原报文标识号
            mcbsMsgId = chainTransMapper.selectByMsgId(clearingDTO.getClrMsgId()).getOutMsgId();
        }

        // 3.异步调用
        try {
            AccountingInstrDO finalAccountingInstrDO = accountingInstrDO;
            String finalMcbsMsgId = mcbsMsgId;
            asyncPool.execute(() -> {
                mbridgeManager.hlht2MbridgeAsync(mBridgeReqEnvelopeDTO, gwReqDTO, finalAccountingInstrDO, finalMcbsMsgId);
            });
        } catch (RejectedExecutionException e) {
            log.error("async thread pool is full, msgId={}", clearingDTO.getClrMsgId(), e);
        }

        return assembly900Msg(clearingDTO, gwReqDTO, ClearingStatusEnum.ACCEPTED.getCode(), ClearingProdErrorEnum.BUSI_SUCCESS);
    }

    @Override
    public Response<GenericEnvelopeDTO<GenericGwDTO>> finish(GenericEnvelopeDTO<GenericGwDTO> genericGwReqDTO) throws DcepException {
        // 是否为mcbs203应答
        if (!(genericGwReqDTO.body() instanceof Mcbs20300101DTO)) {
            log.error("onMbridge response service: msgTp is not mcbs203, mcbsMsgId={}", genericGwReqDTO.body().fetchMsgId());
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), Constant.MBRIDGE_REQHDLG_DESC_TPERR));
        }
        Mcbs20300101DTO mcbs203DTO = (Mcbs20300101DTO) genericGwReqDTO.body();

        // 1.通过原mcbsMsgId查询区块链对接产品表
        String originMsgId = mcbs203DTO.getOriginGroupInfoAndStatus().getOriginMsgId();
        ChainTransDO orgChainTransDO = chainTransMapper.selectByPrimaryKey(originMsgId);
        if (orgChainTransDO == null) {
            log.error("onMbridge response service: select chainTrans failed, no original transaction record, orgMcbsMsgId={}", originMsgId);
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.FAIL.getCode(), Constant.MBRIDGE_REQHDLG_DESC_NOTMATCH));
        }
        log.info("onMbridge response service: select chainTrans succ, orgMcbsMsgId={}", originMsgId);
        // 2.根据区块链对接产品表msgId查询结算产品表
        SettlementProdDO orgSettlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(orgChainTransDO.getMsgId()));
        if (ClearingStatusEnum.SETTLED.getCode().equals(orgSettlementProdDO.getBizSts())
                || ClearingStatusEnum.FAILED.getCode().equals(orgSettlementProdDO.getBizSts())) {
            log.info("onMbridge response service: original transaction status is {}, no need to process, orgMsgId={}", orgSettlementProdDO.getBizSts(), orgSettlementProdDO.getMsgId());
            return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.SUCD.getCode(), Constant.MBRIDGE_REQHDLG_DESC_SUCC));
        }
        log.info("onMbridge response service: select settlementProd succ, orgMsgId={}", orgSettlementProdDO.getMsgId());
        // 3.原交易处理
        dealOrgTrans(orgSettlementProdDO, mcbs203DTO);

        return new Response<>(DtoUtil.assemblyMcbs900(genericGwReqDTO, McbsStatusEnum.SUCD.getCode(), Constant.MBRIDGE_REQHDLG_DESC_SUCC));
    }

    private void dealOrgTrans(SettlementProdDO orgSettlementProdDO, Mcbs20300101DTO mcbs203DTO){
        // 查询记账指令表
        AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgIdPrepare(orgSettlementProdDO.getMsgId());
        log.info("onMbridge response service: select accountingInstr succ, orgMsgId={}", orgSettlementProdDO.getMsgId());
        log.info("onMbridge response service statusId={}, orgMsgId={}", mcbs203DTO.getTransactionInfoAndStatus().getStatusId(), orgSettlementProdDO.getMsgId());
        // 原交易为上桥
        if (Constant.DCEP_MSGTYPE_203.equals(orgSettlementProdDO.getMsgTp())) {
            log.info("onMbridge response service: original transaction is issue, orgMsgId={}", orgSettlementProdDO.getMsgId());
            if (McbsStatusEnum.SUCD.getCode().equals(mcbs203DTO.getTransactionInfoAndStatus().getStatusId())){  // todo 桥上应答只有成功、失败吗？
                // 成功-》交易终态维护
                mbridgeManager.pendingFinish(null, null, accountingInstrDO, ClearingStatusEnum.SETTLED, false, true,
                        false, false, orgSettlementProdDO.getSendPtyId(), null);
            } else {
                // 失败-》调用结算钱包冲正-》交易终态维护
                // 账号转换
                convertAcct(accountingInstrDO);
                log.info("invocation acctrans transfer API to reverse, transId={}, orgMsgId={}", accountingInstrDO.getTransId(), orgSettlementProdDO.getMsgId());
                // 冲正流水登记记账指令表
                try {
                    accountingInstrMapper.insert(accountingInstrDO);
                } catch (DuplicateKeyException e) {
                    log.error("reverse record insert accountingInstr failed, transId={}, orgMsgId={}", accountingInstrDO.getTransId(), orgSettlementProdDO.getMsgId());
                    throw new DcepException(ClearingProdErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(), ClearingProdErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
                }
                mbridgeManager.transfer(accountingInstrDO, false);
                if (ActgStsEnum.SUCCESS.getCode().equals(accountingInstrDO.getActgSts())){
                    // 记账指令表终态更新冲正流水
                    accountingInstrDO.setActgPrcCd(mcbs203DTO.getTransactionInfoAndStatus().getStatusId());
                    accountingInstrDO.setActgPrcInf(mcbs203DTO.getTransactionInfoAndStatus().getStatusReasonInfo().getAdditionInfo());  // todo 错误码转换
                    mbridgeManager.pendingFinish(null, null, accountingInstrDO, ClearingStatusEnum.FAILED, true, true,
                            false, false, orgSettlementProdDO.getSendPtyId(), null);
                } else {
                    log.info("acctrans transfer API return status:{}, transId={}, orgMsgId={}", accountingInstrDO.getActgSts(), accountingInstrDO.getTransId(), orgSettlementProdDO.getMsgId());
                }
            }
        // 原交易为下桥
        } else {
            log.info("onMbridge response service: original transaction is redeem, orgMsgId={}", orgSettlementProdDO.getMsgId());
            if (McbsStatusEnum.SUCD.getCode().equals(mcbs203DTO.getTransactionInfoAndStatus().getStatusId())){
                // 成功-》调用结算钱包记账-》交易终态维护
                mbridgeManager.transfer(accountingInstrDO, false);
                if (ActgStsEnum.SUCCESS.getCode().equals(accountingInstrDO.getActgSts())){
                    mbridgeManager.pendingFinish(null, null, accountingInstrDO, ClearingStatusEnum.SETTLED, true, true,
                            false, false, orgSettlementProdDO.getSendPtyId(), null);
                } else {
                    log.info("acctrans transfer API return status:{}, orgMsgId={}", accountingInstrDO.getActgSts(), orgSettlementProdDO.getMsgId());
                }
            } else {
                // 失败-》交易终态维护
                accountingInstrDO.setActgSts(ActgStsEnum.FAILED.getCode());
                accountingInstrDO.setGmtModified(new Date());
                accountingInstrDO.setActgPrcCd(mcbs203DTO.getTransactionInfoAndStatus().getStatusId());
                accountingInstrDO.setActgPrcInf(mcbs203DTO.getTransactionInfoAndStatus().getStatusReasonInfo().getAdditionInfo());
                mbridgeManager.pendingFinish(null, null, accountingInstrDO, ClearingStatusEnum.FAILED, true, true,
                        false, false, orgSettlementProdDO.getSendPtyId(), null);
            }
        }
    }

    /**
     * 收付款钱包ID、转入转出系统标识、转入转出机构标识转换
     * @param accountingInstrDO
     */
    private void convertAcct(AccountingInstrDO accountingInstrDO){
        accountingInstrDO.setOrgnlTransId(accountingInstrDO.getTransId());
        String transId = IdUtils.randomTransIdWithBizDt(accountingInstrDO.getBizDt());
        accountingInstrDO.setTransId(transId);
        String sysIdTmp = accountingInstrDO.getToClrSysId();
        String mmbIdTmp = accountingInstrDO.getToClrMmbId();
        String wlltIdTmp = accountingInstrDO.getToWlltId();
        accountingInstrDO.setToClrSysId(accountingInstrDO.getFromClrSysId());
        accountingInstrDO.setFromClrSysId(sysIdTmp);
        accountingInstrDO.setToClrMmbId(accountingInstrDO.getFromClrMmbId());
        accountingInstrDO.setFromClrMmbId(mmbIdTmp);
        accountingInstrDO.setToWlltId(accountingInstrDO.getFromWlltId());
        accountingInstrDO.setFromWlltId(wlltIdTmp);
        accountingInstrDO.setActgSts(ActgStsEnum.PROCESS.getCode());
    }

    /**
     * 业务检查
     * @param clearingDTO
     * @param prcSts
     * @return
     */
    private ClearingProdErrorEnum checkBusinessInfo(ClearingDTO clearingDTO, String curActgDt, String prcSts) {
        log.info("dcep send service: checkBusinessInfo start, msgId={}", clearingDTO.getClrMsgId());
        // 1.检查收款机构、付款机构状态是否正常
        if (!InfoCacheUtil.checkInstState(clearingDTO.clrSendPtyId())) {
            log.info("sender state illegal,sendPtyId={}, msgId={}", clearingDTO.clrSendPtyId(), clearingDTO.getClrMsgId());
            return ClearingProdErrorEnum.SENDER_STATE_ILLEGAL;
        }
        if (!InfoCacheUtil.checkInstState(clearingDTO.clrRecvPtyId())) {
            log.info("reciver state illegal,recvPtyId={}, msgId={}", clearingDTO.clrRecvPtyId(), clearingDTO.getClrMsgId());
            return ClearingProdErrorEnum.RECEIVER_STATE_ILLEGAL;
        }
        // 2.查询系统状态表，检查报文中系统工作日与当前系统工作日是否匹配
        boolean flag = commonManager.checkSystemDate(curActgDt);
        if (!flag){
            log.info("curSystime and reqSystime not match, reqSystime={}, msgId={}", curActgDt, clearingDTO.getClrMsgId());
            return ClearingProdErrorEnum.CREDTTM_ILLEGAL;
        }
        // 3.查询清零控制表，检查清零处理状态是否为“清零中”
        if (StringUtils.equals(prcSts, ZerooutPrcStsEnum.PROCESS.getCode())){
            log.info("zerooutCtrl status is process, mBridge transactions are not allowed, msgId={}", clearingDTO.getClrMsgId());
            return ClearingProdErrorEnum.UNKNOWN_EXCEPTION;// todo
        }
        log.info("dcep send service: checkBusinessInfo end, msgId={}", clearingDTO.getClrMsgId());
        return null;
    }

    /**
     * 异常处理
     * @param clearingDTO
     * @param gwReqDTO
     * @param mcbsMsgId
     * @param clearing
     * @return
     */
    private Response<EnvelopeDTO<GwDTO>> dealDuplicateKeyException(ClearingDTO clearingDTO, EnvelopeDTO<GwDTO> gwReqDTO, String mcbsMsgId, Clearing clearing) {
        // transId重复生成，返回dcep911
        if (Constant.INSERT_STEP_THREE.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
            log.error("transId insert repeat: msgId={}, insertStep={}", clearingDTO.getClrMsgId(), Constant.INSERT_STEP_THREE);
            return assembly911Msg(gwReqDTO, ClearingProdErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(), ClearingProdErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
        }

        // mcbsMsgId重复生成，返回dcep911
        if (Constant.INSERT_STEP_TWO.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
            log.error("mcbsMsgId insert repeat: mcbsMsgId={}, insertStep={}, msgId={}", mcbsMsgId, Constant.INSERT_STEP_TWO, clearingDTO.getClrMsgId());
            return assembly911Msg(gwReqDTO, ClearingProdErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(), ClearingProdErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());

        }

        // msgId重复
        // select原交易, 并对原交易核心要素幂等要素进行检查
        SettlementProdDO settlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(clearingDTO.getClrMsgId()));
        // 原交易为终态，返回dcep911
        if (ClearingStatusEnum.FAILED.getCode().equals(settlementProdDO.getBizSts())
            || ClearingStatusEnum.SETTLED.getCode().equals(settlementProdDO.getBizSts())) {
            return assembly911Msg(gwReqDTO, ClearingProdErrorEnum.BUSI_DUPLICATION.getCode(), ClearingProdErrorEnum.BUSI_DUPLICATION.getDescription());
        }
        if (!CheckUtil.idempotentMatch(clearingDTO, settlementProdDO, clearing)) {
            return assembly911Msg(gwReqDTO, ClearingProdErrorEnum.NO_MATCH_ORIGNAL.getCode(), ClearingProdErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        return null;
    }

    /**
     * 组装dcep900报文
     * @param clearingDTO
     * @param in
     * @param prcSts
     * @return
     */
    private Response<EnvelopeDTO<GwDTO>> assembly900Msg(ClearingDTO clearingDTO, EnvelopeDTO<GwDTO> in, String prcSts, ClearingProdErrorEnum prcEnum) {
        return new Response<>(CommonMsgUtils.genDc900(clearingDTO.getClrMsgId(), in.getSoapHeader().getSender(), in.getSoapHeader().getReceiver(),
                in.getSoapHeader().getMsgTp(), prcSts, prcEnum.getCode(), prcEnum.getDescription(), null));
    }

    /**
     * 组装dcep911报文
     * @param gwReqDTO
     * @param errorCode
     * @param errorMsg
     * @return
     */
    private Response<EnvelopeDTO<GwDTO>> assembly911Msg(EnvelopeDTO<GwDTO> gwReqDTO, String errorCode, String errorMsg) {
        return new Response<>(DtoUtil.dcep911(gwReqDTO.getSoapHeader().getMsgSN(), InfoCacheUtil.getPbocInf(), gwReqDTO.getSoapHeader().getSender(),
                errorCode, errorMsg, gwReqDTO.getSoapHeader().getReceiver(), errorMsg));
    }
}
