package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90200101DTO;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.dips.acctrans.constants.Constant.AccountingStatus;
import com.dcep.dips.acctrans.constants.Constant.WalletStatus;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustNotifyRespDTO;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustRespDTO;
import com.dcep.dips.acctrans.dto.liquid.QueryWalletDetailReqDTO;
import com.dcep.dips.acctrans.dto.liquid.QueryWalletDetailRespDTO;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.bo.ActgAdjustRespBO;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.*;
import com.dcep.dips.wholesalepayment.dto.BizStatusDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.FundingManager;
import com.dcep.dips.wholesalepayment.manager.StorageForwardManager;
import com.dcep.dips.wholesalepayment.manager.SystemStatusManager;
import com.dcep.dips.wholesalepayment.utils.EnvInfoUtil;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.supergw.api.GwoutService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

/**
 * 注资预注资-资金调整管理类
 */
@Slf4j
@Service
public class FundingManagerImpl implements FundingManager {

    @DubboReference
    private GwoutService gwoutService;

    @Autowired
    private HvpsTransMapper hvpsTransMapper;

    @Autowired
    private AccountingInstrMapper accountingInstrMapper;

    @Autowired
    private FundAdjustProdMapper fundAdjustProdMapper;

    @Autowired
    private CommonRecordMapper commonRecordMapper;

    @Autowired
    private StorageForwardMapper storageForwardMapper;

    @Autowired
    private StorageForwardManager storageForwardManager;

    @Autowired
    private AccountingManager accountingManager;

    @Autowired
    private FundingManager fundingManager;

    @Autowired
    private SystemStatusManager systemStatusManager;

    @Autowired
    @Qualifier("asyncBizPool")
    private ExecutorService asyncBizPool;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Response<AccountingInstrDO> decreaseRecord(EnvelopeDTO<GwDTO> gwReqDTO) {
        FundingDTO fundingDTO = (FundingDTO) gwReqDTO.body();
        // 获取当前系统工作日期
        String curSysDt = systemStatusManager.selectCurSysDt();

        // 上下文调用信息
        RpcContext context = RpcContext.getContext();

        log.info("登记《资金调整产品表》");
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
        fundAdjustProdMapper.insert(new FundAdjustProdDO(curSysDt, fundingDTO));

        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
        String transId = IdUtils.randomTransIdWithBizDt(curSysDt);
        log.info("运营中心生成的：transId={}", transId);

        log.info("登记《记账指令表》");
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO(transId, curSysDt, fundingDTO);
        accountingInstrMapper.insert(accountingInstrDO);

        log.info("登记《档案表》");
        commonRecordMapper.insert(new CommonRecordDO(fundingDTO));

        log.info("登记《存储转发表》，向《结算钱包》存储转发");
        storageForwardManager.saveForActgAdjustDecrease(fundingDTO);
        return new Response<>(accountingInstrDO);
    }

    @Override
    public Response<FundAdjustProdDO> zeroOutRecord(ZeroOutReqDTO zeroOutReqDTO) {
        // 上下文调用信息
        RpcContext context = RpcContext.getContext();

        // 生成报文标识号
        String msgId = MsgIdUtil.randomMsgId(MsgTpEnum.FUND_ADJUST_NOTICE_ABBR.getCode(), CommonConstant.PBOC_SHORT_PTY_ID, EnvInfoUtil.getEnvInfo());
        log.info("运营中心生成的：msgId={}", msgId);
        context.set(Constant.PREPARE_INSERT_MSGID, msgId);
        String msgTp = MsgTpEnum.FUND_ADJUST_NOTICE.getCode();

        // 获取当前系统日期
        String sysDt = systemStatusManager.selectCurSysDt();

        log.info("登记《记账指令表》");
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
        accountingInstrMapper.insert(new AccountingInstrDO(msgId, msgTp, sysDt, zeroOutReqDTO));

        log.info("登记《资金调整产品表》");
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
        FundAdjustProdDO fundAdjustProdDO = new FundAdjustProdDO(msgId, msgTp, sysDt, zeroOutReqDTO);
        fundAdjustProdMapper.insert(fundAdjustProdDO);

        log.info("登记《档案表》");
        commonRecordMapper.insert(new CommonRecordDO(msgId, msgTp, zeroOutReqDTO));

        log.info("登记《存储转发表》，向《准备金》存储转发");
        storageForwardManager.saveForHvpsZeroOut(msgId, msgTp, zeroOutReqDTO);
        return new Response<>(fundAdjustProdDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Response<AccountingInstrDO> increaseRecord(IncreaseReqDTO increaseReqDTO) {
        // 上下文调用信息
        RpcContext context = RpcContext.getContext();

        // 获取当前系统日期
        String curSysDt = systemStatusManager.selectCurSysDt();

        String msgId = MsgIdUtil.randomMsgId(MsgTpEnum.FUND_ADJUST_NOTICE_ABBR.getCode(), CommonConstant.PBOC_SHORT_PTY_ID, CommonUtil.getEnvVal());
        log.info("运营中心生成的：msgId={}", msgId);

        log.info("登记《大额对接产品表》");
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);
        HvpsTransDO hvpsTransDO = new HvpsTransDO(increaseReqDTO, msgId);
        hvpsTransMapper.insert(hvpsTransDO);

        log.info("登记《资金调整产品表》");
        FundAdjustProdDO fundAdjustProdDO = new FundAdjustProdDO(msgId, curSysDt, increaseReqDTO);
        fundAdjustProdMapper.insert(fundAdjustProdDO);

        String transId = IdUtils.randomTransIdWithBizDt(curSysDt);
        log.info("运营中心生成的：transId={}", transId);

        log.info("登记《记账指令表》");
        context.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO(transId, curSysDt, fundAdjustProdDO, increaseReqDTO);
        accountingInstrMapper.insert(accountingInstrDO);

        log.info("登记《档案表》");
        commonRecordMapper.insert(new CommonRecordDO(msgId, fundAdjustProdDO.getMsgTp(), increaseReqDTO.encode()));

        return new Response<>(accountingInstrDO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updatePrepareStatus(FundAdjustProdDO orgnlFundAdjustProdDO, FundAdjustProdDO fundAdjustProdDO,
                                    String transId, ActgAdjustRespBO actgAdjustRespBO) {
        // 1. 更新《资金调整产品表》中业务状态
        log.info("预记账阶段，更新《资金调整产品表》，更新条件：msgId={},bizSts={}，更新内容：{}",
                orgnlFundAdjustProdDO.getMsgId(), orgnlFundAdjustProdDO.getBizSts(), fundAdjustProdDO);
        if (fundAdjustProdMapper.updateBizSts(orgnlFundAdjustProdDO, fundAdjustProdDO) != 1) {
            log.warn("预记账阶段，更新《资金调整产品表》，更新条数不等于1，抛出异常");
            throw new DcepException(WholesaleErrorEnum.BUSI_DUPLICATION.getCode(),
                    WholesaleErrorEnum.BUSI_DUPLICATION.getDescription());
        }

        // 2. 更新《记账指令表》中记账状态和记账日期
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO(transId);
        accountingInstrDO.setActgSts(actgAdjustRespBO.getAccountingStatus());
        if (AccountingStatus.FAILED.equals(actgAdjustRespBO.getAccountingStatus())) {
            accountingInstrDO.setActgPrcCd(actgAdjustRespBO.getBizCode());
            accountingInstrDO.setActgPrcInf(actgAdjustRespBO.getBizMsg());
        } else {
            accountingInstrDO.setActgDt(actgAdjustRespBO.getAccountingDate());
        }

        log.info("预记账阶段，更新《记账指令表》，更新条件：transId={},partitionTime={},orgnlActgSts={}，更新内容：{}",
                accountingInstrDO.getTransId(), accountingInstrDO.getPartitionTime(), AccountingStatus.PROCESSING, accountingInstrDO);
        if (accountingInstrMapper.updateActgSts(accountingInstrDO, AccountingStatus.PROCESSING) != 1) {
            log.warn("预记账阶段，更新《记账指令表》，更新条数不等于1，抛出异常");
            throw new DcepException(WholesaleErrorEnum.BUSI_DUPLICATION.getCode(),
                    WholesaleErrorEnum.BUSI_DUPLICATION.getDescription());
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public FundAdjustProdDO updateDecreasePrepareStatus(FundingDTO fundingDTO, AccountingInstrDO accountingInstrDO,
                                                        ActgAdjustRespBO actgAdjustRespBO) {
        //《资金调整产品表》原记录选择条件
        FundAdjustProdDO orgnlFundAdjustProdDO = FundAdjustProdDO.builder()
                .msgId(fundingDTO.msgId())
                .bizSts(ClearingStatusEnum.PROCESS.getCode()).build();

        FundAdjustProdDO fundAdjustProdDO;
        if (AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            // 结算钱包：调减，冻结成功
            // 更新《资金调整产品表》状态为《已受理》
            fundAdjustProdDO = FundAdjustProdDO.builder()
                    .bizSts(ClearingStatusEnum.ACCEPTED.getCode())
                    .bizPrcCd(WholesaleErrorEnum.BUSI_SUCCESS.getCode())
                    .gmtModified(new Date()).build();
        } else {
            // 结算钱包：调减，冻结失败
            // 更新《资金调整产品表》状态为《失败》
            fundAdjustProdDO = FundAdjustProdDO.builder()
                    .bizSts(ClearingStatusEnum.FAILED.getCode())
                    .bizPrcCd(actgAdjustRespBO.getBizCode())
                    .bizPrcInf(actgAdjustRespBO.getBizMsg())
                    .gmtModified(new Date()).build();
        }

        // 更新《资金调整产品表》状态、更新《账户指令表》状态
        fundingManager.updatePrepareStatus(orgnlFundAdjustProdDO, fundAdjustProdDO,
                accountingInstrDO.getTransId(), actgAdjustRespBO);

        log.info("摘除《结算钱包》的存储转发，msgId={}, msgTp={}", fundingDTO.msgId(), fundingDTO.msgTp());
        storageForwardMapper.deleteByPrimaryKey(new StorageForwardDO(fundingDTO.msgId(), fundingDTO.msgTp()));

        if (AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            log.info("结算钱包：返回成功，登记《准备金》的存储转发");
            storageForwardManager.saveForHvpsAdjustDecrease(fundingDTO);
        }

        return fundAdjustProdDO;
    }

    /**
     * 注资调减、预注资调减、清零，都会走这个漏记
     * @param clearReportReqDTO 大额通知DTO
     * @param orgnlHvpsTransDO 大额对接产品表原DO
     * @param orgnlFundAdjustProdDO 资金调整产品表原DO
     * @param finishAccInstrDO 记账指令表DO
     * @param actgAdjustRespBO 结算钱包返回应答DTO
     * @param hvpsAdjStatus 大额资金调整结果
     * @return 返回机构报文
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<EnvelopeDTO<GwDTO>> updateDecreaseFinishStatus(ClearReportReqDTO clearReportReqDTO,
                                           HvpsTransDO orgnlHvpsTransDO, FundAdjustProdDO orgnlFundAdjustProdDO,
                                           AccountingInstrDO finishAccInstrDO, ActgAdjustRespBO actgAdjustRespBO,
                                           HvpsAdjStatusEnum hvpsAdjStatus) {
        HvpsTransDO hvpsTransDO = HvpsTransDO.builder()
                .bizSts(clearReportReqDTO.getPrcStatus())
                .bizPrcCd(clearReportReqDTO.getPrcCode())
                .bizPrcInf(clearReportReqDTO.getPrcInf())
                .sttlmDt(clearReportReqDTO.getSettlementDate()).build();

        FundAdjustProdDO fundAdjustProdDO;
        if (AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            // 结算钱包：返回成功，根据大额返回状态调整《资金产品表》的状态
            if (hvpsAdjStatus == HvpsAdjStatusEnum.SUCCESS) {
                fundAdjustProdDO = FundAdjustProdDO.builder()
                        .bizSts(ClearingStatusEnum.SETTLED.getCode())
                        .sttlmDt(actgAdjustRespBO.getAccountingDate())
                        .adjustWltId(actgAdjustRespBO.getWalletId())
                        .accountBalance(actgAdjustRespBO.getAccountBalance())
                        .accountFlag(actgAdjustRespBO.getCurrentSystemFlag())
                        .ciLimit(actgAdjustRespBO.getCiLimit())
                        .netQuota(actgAdjustRespBO.getNetQuota())
                        .build();
            } else {
                fundAdjustProdDO = FundAdjustProdDO.builder()
                        .bizSts(ClearingStatusEnum.FAILED.getCode())
                        .bizPrcCd(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode())
                        .bizPrcInf(clearReportReqDTO.getPrcInf()).build();
            }
        } else {
            // 结算钱包：返回失败
            // 更新《大额对接产品表》为《PR09-已拒绝、PR18-已退回、PR04-已清算》
            // 更新《资金调整产品表》为《失败》
            // 更新《记账指令表》为《失败》
            fundAdjustProdDO = FundAdjustProdDO.builder()
                    .bizSts(ClearingStatusEnum.FAILED.getCode())
                    .bizPrcCd(actgAdjustRespBO.getBizMsg())
                    .bizPrcInf(actgAdjustRespBO.getBizMsg()).build();

            // 存在两种情况
            // 1).如果大额返回《PR04-已清算》，表示结算钱包《解冻并记账》失败
            // 2).如果大额返回《PR09-已拒绝、PR18-已退回》，表示结算钱包《解冻》失败
            // TODO 此种情况，需卡在运营中心，打印报错日志，报监控系统，后续人工介入！
            log.error("致命故障，请紧急排查，注资调减、预注资调减、清零，大额清算通知，结算钱包返回失败，hvpsMsgId={},hvpsBizSts={}," +
                            "orgnlHvpsMsgId={},orgnlHvpsSendPty={},msgId={},bizSts={},transId={},actgSts={},actgInfo={}",
                    clearReportReqDTO.getMsgId(), clearReportReqDTO.getPrcStatus(),
                    orgnlHvpsTransDO.getHvpsMsgId(), orgnlHvpsTransDO.getHvpsSendPty(),
                    orgnlHvpsTransDO.getMsgId(), fundAdjustProdDO.getBizSts(), finishAccInstrDO.getTransId(),
                    actgAdjustRespBO.getAccountingStatus(), actgAdjustRespBO.getBizMsg());
        }

        fundingManager.updateFinishStatus(orgnlHvpsTransDO, hvpsTransDO, orgnlFundAdjustProdDO, fundAdjustProdDO,
                finishAccInstrDO.getTransId(), actgAdjustRespBO);

        if (!AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            log.info("《结算钱包系统》返回失败，不登记《存储转发表》（不通知机构）");
            return Collections.emptyList();
        }

        List<EnvelopeDTO<GwDTO>> dtoList = new ArrayList<>(2);
        if (hvpsAdjStatus == HvpsAdjStatusEnum.SUCCESS) {
            log.info("《结算钱包系统》返回成功，大额返回成功，组装通知机构的dcep.185报文，并登记《存储转发表》（通知机构）");
            String receiver = InfoCacheUtil.getCustodianInstNo(orgnlFundAdjustProdDO.getAdjustPtyId());
            EnvelopeDTO<GwDTO> dto185 = createNoticeInst185DTO(orgnlFundAdjustProdDO.getMsgId(), receiver);
            storageForwardManager.saveForInst(dto185, receiver);
            dtoList.add(dto185);
        }

        if ((orgnlFundAdjustProdDO.getAdjustTp().equals(HvpsAdjTypEnum.DECREASE.getCode())
                || orgnlFundAdjustProdDO.getAdjustTp().equals(HvpsAdjTypEnum.PRE_DECREASE.getCode()))
                && hvpsAdjStatus != HvpsAdjStatusEnum.SUCCESS) {
            String desc = HvpsAdjTypEnum.getDesc(orgnlFundAdjustProdDO.getAdjustTp());
            log.info("《结算钱包系统》返回成功，大额返回失败，调整类型为：{}，组装通知机构的dcep.200报文，并登记《存储转发表》（通知机构）", desc);
            String receiver = orgnlFundAdjustProdDO.getAdjustPtyId();
            EnvelopeDTO<GwDTO> dto200 = createNoticeInst200DTO(orgnlFundAdjustProdDO.getMsgId(), receiver, clearReportReqDTO.getPrcInf());
            storageForwardManager.saveForInst(dto200, receiver);
            dtoList.add(dto200);
        }
        return dtoList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void updateFinishStatus(HvpsTransDO orgnlHvpsTransDO, HvpsTransDO hvpsTransDO,
                                   FundAdjustProdDO orgnlFundAdjustProdDO, FundAdjustProdDO fundAdjustProdDO,
                                   String transId, ActgAdjustRespBO actgAdjustRespBO) {
        // 1. 更新《大额对接产品表》的状态为大额应答的状态
        if (null != orgnlHvpsTransDO) {
            hvpsTransDO.setGmtModified(new Date());
            log.info("完成记账阶段，更新《大额对接产品表》，更新条件：hvpsMsgId={},hvpsSendPty={},bizSts={}，更新内容：{}",
                    orgnlHvpsTransDO.getHvpsMsgId(), orgnlHvpsTransDO.getHvpsSendPty(), orgnlHvpsTransDO.getBizSts(), hvpsTransDO);
            if (hvpsTransMapper.updateBizSts(orgnlHvpsTransDO, hvpsTransDO) != 1) {
                log.warn("完成记账阶段，更新《大额对接产品表》，更新条数不等于1，抛出异常");
                throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                        WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
            }
        }

        // 2. 更新《资金调整产品表》中业务状态
        if (null != orgnlFundAdjustProdDO) {
            fundAdjustProdDO.setGmtModified(new Date());
            log.info("完成记账阶段，更新《资金调整产品表》，更新条件：msgId={},bizSts={}，更新内容：{}",
                    orgnlFundAdjustProdDO.getMsgId(), orgnlFundAdjustProdDO.getBizDt(), fundAdjustProdDO);
            if (fundAdjustProdMapper.updateBizSts(orgnlFundAdjustProdDO, fundAdjustProdDO) != 1) {
                log.warn("完成记账阶段，更新《资金调整产品表》，更新条数不等于1，抛出异常");
                throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                        WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
            }
        }

        // 3. 更新《记账指令表》状态
        if (!StringUtils.isEmpty(transId)) {
            AccountingInstrDO accountingInstrDO = new AccountingInstrDO(transId);
            accountingInstrDO.setActgSts(actgAdjustRespBO.getAccountingStatus());
            if (AccountingStatus.FAILED.equals(actgAdjustRespBO.getAccountingStatus())) {
                accountingInstrDO.setActgPrcCd(actgAdjustRespBO.getBizCode());
                accountingInstrDO.setActgPrcInf(actgAdjustRespBO.getBizMsg());
            } else {
                accountingInstrDO.setActgDt(actgAdjustRespBO.getAccountingDate());
            }

            log.info("完成记账阶段，更新《记账指令表》，更新条件：transId={},partitionTime={},orgnlActgSts={}，更新内容：{}",
                    accountingInstrDO.getTransId(), accountingInstrDO.getPartitionTime(), AccountingStatus.PROCESSING, accountingInstrDO);
            if (accountingInstrMapper.updateActgSts(accountingInstrDO, AccountingStatus.PROCESSING) != 1) {
                log.warn("完成记账阶段，更新《记账指令表》，更新条数不等于1，抛出异常");
                throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                        WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
            }
        }
    }

    /**
     * 检查业务信息（包含机构状态等）
     *
     * @param incrReqDTO 请求对象
     * @return 错误对象
     */
    @Override
    public Response<BizStatusDTO> checkHvpsBusinessInfo(IncreaseReqDTO incrReqDTO) {
        String clrMmbId = InfoCacheUtil.getInstNoForHvpsBkNo(incrReqDTO.getReceiveMemberId());
        QueryWalletDetailReqDTO detailReqDTO = new QueryWalletDetailReqDTO();
        detailReqDTO.setClearingMemberId(clrMmbId);
        detailReqDTO.setClearingSystemId(ChnlSysEnum.DCEP.getCode());
        detailReqDTO.setCurrency(CommonConstant.Currency.CNY);

        Response<QueryWalletDetailRespDTO> response = accountingManager.queryWalletDetail(detailReqDTO);
        if (!response.isSuccess()) {
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.FAILED.getCode(), response.getErrorCode(), response.getErrorMsg()));
        }

        // 检查钱包状态
        QueryWalletDetailRespDTO respDTO = response.getResult();
        if (WalletStatus.CANCELED.equals(respDTO.getWlltSts())) {
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.FAILED.getCode(),
                    ClearingProdErrorEnum.BUSI_SETTL_WAL_IN_DEL_ERR.getCode(), "转入机构的钱包已销户"));
        }

        return null;
    }

    public void asyncSendInst(List<EnvelopeDTO<GwDTO>> dtoList, String msgId, String envInfo) {
        try {
            log.info("准备切换线程，异步通知机构，msgId={}", msgId);
            asyncBizPool.execute(() -> sendInstAndDelStorageForward(dtoList, msgId, envInfo));
        } catch (RejectedExecutionException re) { // 线程池任务已满,拒绝获取线程
            log.error("asyncBizPool all thread is busy, msgId:{}", msgId, re);
        } catch (Exception e) {
            log.error("FundingManagerImpl.asyncSendInst error, msgId:{}", msgId, e);
        }
    }

    @Override
    public void sendInstAndDelStorageForward(List<EnvelopeDTO<GwDTO>> dtoList, String msgId, String envInfo) {
        try {
            // 切换日志上下文和环境信息
            CommonUtil.LogMDC(msgId);
            CommonUtil.setEnvInfo(envInfo);

            for (EnvelopeDTO<GwDTO> dto : dtoList) {
                RecordDTO recordDTO = (RecordDTO) dto.getSoapBody().getT();
                log.info("通知机构报文：noticeMsgId={},noticeMsgId={}", recordDTO.recMsgId(), recordDTO.recMsgTp());
                boolean sent = sendInst(dto, msgId);
                if (sent) {
                    log.info("通知机构成功，摘除存储转发：noticeMsgId={},noticeMsgId={}", recordDTO.recMsgId(), recordDTO.recMsgTp());
                    StorageForwardDO storageForwardDO = new StorageForwardDO(recordDTO.recMsgId(), recordDTO.recMsgTp());
                    storageForwardMapper.deleteByPrimaryKey(storageForwardDO);
                }
            }
        } catch (Exception e) {
            log.error("通知机构出现异常", e);
        }
    }

    public EnvelopeDTO<GwDTO> createNoticeInst185DTO(String msgId, String receiver) {
        FundAdjustProdDO fundAdjustProdDO = fundAdjustProdMapper.selectByPrimaryKey(new FundAdjustProdDO(msgId));
        HvpsTransDO hvpsTransDO = hvpsTransMapper.selectByMsgId(msgId);
        return DtoUtil.assembly185Msg(fundAdjustProdDO, hvpsTransDO, receiver);
    }

    public EnvelopeDTO<GwDTO> createNoticeInst200DTO(String msgId, String receiver, String hvpsPrcInf) {
        FundAdjustProdDO fundAdjustProdDO = fundAdjustProdMapper.selectByPrimaryKey(new FundAdjustProdDO(msgId));
        return DtoUtil.assembly200Msg(fundAdjustProdDO, receiver, hvpsPrcInf);
    }

    /**
     * 大额的saps.604通知报文，有三种情况会走到这里
     *   原报文为：注资调减、预注资调减、清零
     * @param clearReportReqDTO 大额通知DTO
     * @param orgnlHvpsTransDO 对接大额产品表原DO
     * @param orgnlFundAdjustProdDO 资金调整产品表原DO
     * @param hvpsAdjStatus 大额终态是成功还是失败
     * @return 应答对象
     */
    @Override
    public Response<BizStatusDTO> decreaseFinish(ClearReportReqDTO clearReportReqDTO, HvpsTransDO orgnlHvpsTransDO,
                                                 FundAdjustProdDO orgnlFundAdjustProdDO, HvpsAdjStatusEnum hvpsAdjStatus) {
        log.info("查询《记账指令表》中预记账记录：msgId={}", orgnlHvpsTransDO.getMsgId());
        AccountingInstrDO prepareActgInstrDO = accountingInstrMapper.selectByMsgIdPrepare(orgnlHvpsTransDO.getMsgId());
        if (null == prepareActgInstrDO) {
            log.error("预记账记录不存在，msgId={}", orgnlHvpsTransDO.getMsgId());
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        log.info("预记账记录：{}", prepareActgInstrDO);

        log.info("查询《记账指令表》中完成记账记录：msgId={},orgnlTransId={}", prepareActgInstrDO.getMsgId(), prepareActgInstrDO.getTransId());
        AccountingInstrDO finishAccInstrDO = accountingInstrMapper
                .selectByMsgIdFinish(new AccountingInstrDO(prepareActgInstrDO.getMsgId(), prepareActgInstrDO.getTransId()));
        if (null == finishAccInstrDO) {
            String transId = IdUtils.randomTransIdWithBizDt(systemStatusManager.selectCurSysDt());
            finishAccInstrDO = new AccountingInstrDO(transId, prepareActgInstrDO);
            log.info("完成记账记录不存在，插入：{}", finishAccInstrDO);
            try {
                accountingInstrMapper.insert(finishAccInstrDO);
            } catch (DuplicateKeyException e) {
                log.info("插入主键或唯一索引冲突，抛出异常，让准备金重发");
                throw new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                        WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
            }
        }

        if (!AccountingStatus.PROCESSING.equals(finishAccInstrDO.getActgSts())) {
            log.info("完成记账状态已为终态，不再调用《结算钱包》，返回成功给《准备金》");
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
        }

        log.info("调用《结算钱包》调整通知接口，hvpsAdjStatus={}, {}", hvpsAdjStatus, finishAccInstrDO);
        Response<AdjustNotifyRespDTO> actgResp = accountingManager.adjustmentResultNotify(finishAccInstrDO, hvpsAdjStatus);
        if (!actgResp.isSuccess()) {
            log.info("结算钱包返回：通讯异常，抛出异常，等待《准备金》存储转发");
            throw new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        // 统一封装《结算钱包》返回对象
        ActgAdjustRespBO actgAdjustRespBO = new ActgAdjustRespBO(actgResp.getResult());

        log.info("更新完成记账阶段的业务状态");
        List<EnvelopeDTO<GwDTO>> dtoList =
                fundingManager.updateDecreaseFinishStatus(clearReportReqDTO, orgnlHvpsTransDO,
                        orgnlFundAdjustProdDO, finishAccInstrDO, actgAdjustRespBO, hvpsAdjStatus);

        fundingManager.asyncSendInst(dtoList, orgnlFundAdjustProdDO.getMsgId(), orgnlFundAdjustProdDO.getEnvInfo());

        return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
    }

    /**
     * 预注资调增
     * @param clearReportReqDTO 大额通知报文
     * @param orgnlHvpsTransDO 原大额对接产品表DO
     * @param orgnlFundAdjustProdDO 原资金调整产品表DO
     */
    public Response<BizStatusDTO> preIncreaseFinish(ClearReportReqDTO clearReportReqDTO, HvpsTransDO orgnlHvpsTransDO,
                                              FundAdjustProdDO orgnlFundAdjustProdDO) {
        // 大额返回《已冻结待清算》
        // 原交易：预注资调增收到hvps.115报文时，已登记过《大额对接产品表》、《资金调整产品表》
        // 这里将原《大额对接产品表》、《资金调整产品表》中的要素查出来登记一笔《记账指令表》记录，并调用《结算钱包系统》调增
        AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgId(orgnlHvpsTransDO.getMsgId());
        if (null == accountingInstrDO) {
            log.error("预注资检查时登记过一笔记账指令，这里查不到抛异常，msgId={}", orgnlHvpsTransDO.getMsgId());
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                    WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        // 如果记账状态为终态，则返回准备金成功，让准备金摘除存储转发
        if (!AccountingStatus.PROCESSING.equals(accountingInstrDO.getActgSts())) {
            log.error("《记账指令表》记账状态已经为终态，返回成功，msgId={}", orgnlHvpsTransDO.getMsgId());
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
        }

        // 调用结算钱包系统，应答结果有三种：通讯异常、调减成功、调减失败
        Response<AdjustRespDTO> actgResp = accountingManager.adjust(accountingInstrDO, clearReportReqDTO); // 预注资调增
        if (!actgResp.isSuccess()) {
            // 结算钱包：通讯异常(状态不明)，抛出异常，等待准备金的存储转发
            throw new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        // 统一转换结算钱包返回对象
        ActgAdjustRespBO actgAdjustRespBO = new ActgAdjustRespBO(actgResp.getResult());

        // 更新业务状态，并组装发送机构的报文
        List<EnvelopeDTO<GwDTO>> envelopeDTO = fundingManager.updatePreIncreaseFinishStatus(clearReportReqDTO,
                orgnlHvpsTransDO, orgnlFundAdjustProdDO, accountingInstrDO, actgAdjustRespBO);

        asyncSendInst(envelopeDTO, orgnlFundAdjustProdDO.getMsgId(), orgnlFundAdjustProdDO.getEnvInfo());

        // 不管《结算钱包》返回成功，还是返回失败，都反给《准备金》成功
        return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<EnvelopeDTO<GwDTO>> updateIncreaseFinishStatus(
            IncreaseReqDTO increaseReqDTO, AccountingInstrDO accountingInstrDO, ActgAdjustRespBO actgAdjustRespBO) {
        // 1.组装《大额对接产品表》状态更新数据，更新《大额对接产品表》为《已清算》
        // 原记录查询条件
        HvpsTransDO orgnlHvpsTransDO = HvpsTransDO.builder()
                .hvpsMsgId(increaseReqDTO.getMsgId())
                .hvpsSendPty(increaseReqDTO.getSendMemberId())
                .bizSts(HvpsClearingStatusEnum.ACCEPTED.getCode()).build();

        // 更新目标状态
        HvpsTransDO hvpsTransDO = HvpsTransDO.builder()
                .bizSts(HvpsClearingStatusEnum.CLEARED.getCode())
                .sttlmDt(increaseReqDTO.getAccountingDate()).build();

        // 2.组装《资金调整产品表》状态更新数据
        // 原记录查询条件
        FundAdjustProdDO orgnlFundAdjustProdDO = FundAdjustProdDO.builder()
                .msgId(accountingInstrDO.getMsgId())
                .bizSts(ClearingStatusEnum.ACCEPTED.getCode()).build();

        // 更新目标状态
        FundAdjustProdDO fundAdjustProdDO;
        if (AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            // 结算钱包：注资调增成功，更新《资金调整产品表》为已结算
            fundAdjustProdDO = FundAdjustProdDO.builder()
                    .bizSts(ClearingStatusEnum.SETTLED.getCode())
                    .sttlmDt(actgAdjustRespBO.getAccountingDate())
                    .adjustWltId(actgAdjustRespBO.getWalletId())
                    .accountBalance(actgAdjustRespBO.getAccountBalance())
                    .accountFlag(actgAdjustRespBO.getCurrentSystemFlag())
                    .ciLimit(actgAdjustRespBO.getCiLimit())
                    .netQuota(actgAdjustRespBO.getNetQuota()).build();
        } else {
            // 结算钱包：注资调增失败，更新《资金调整产品表》为失败
            fundAdjustProdDO = FundAdjustProdDO.builder()
                    .bizSts(ClearingStatusEnum.FAILED.getCode())
                    .bizPrcCd(WholesaleErrorEnum.ACCT_REJECT.getCode())
                    .bizPrcInf(actgAdjustRespBO.getBizMsg()).build();

            log.error("大额注资调增，《结算钱包系统》记账失败，hvpsMsgId={},hvpsSendPty={},hvpsReceivePty={}," +
                            "msgId={},transId={},actgSts={},actgBizCode={},actgBizMsg={}",
                    increaseReqDTO.getMsgId(), increaseReqDTO.getSendMemberId(), increaseReqDTO.getReceiveMemberId(),
                    accountingInstrDO.getMsgId(), accountingInstrDO.getTransId(),
                    actgAdjustRespBO.getAccountingStatus(), actgAdjustRespBO.getBizCode(), actgAdjustRespBO.getBizMsg());
        }

        log.info("更新《大额对接产品表》、《资金调整产品表》、《记账指令表》状态");
        fundingManager.updateFinishStatus(orgnlHvpsTransDO, hvpsTransDO, orgnlFundAdjustProdDO, fundAdjustProdDO,
                accountingInstrDO.getTransId(), actgAdjustRespBO);

        if (!AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            log.info("《结算钱包》返回失败，不登记《存储转发表》（不通知机构）");
            return Collections.emptyList();
        }

        log.info("《结算钱包系统》返回成功，组装通知机构的dcep.185报文，并登记《存储转发表》（通知机构）");
        String receiver = InfoCacheUtil.getInstNoForHvpsClrBkNo(increaseReqDTO.getClearingMemberId());
        EnvelopeDTO<GwDTO> envelopeDTO = createNoticeInst185DTO(accountingInstrDO.getMsgId(), receiver);
        storageForwardManager.saveForInst(envelopeDTO, receiver);

        return Collections.singletonList(envelopeDTO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public List<EnvelopeDTO<GwDTO>> updatePreIncreaseFinishStatus(ClearReportReqDTO clearReportReqDTO,
                                                            HvpsTransDO orgnlHvpsTransDO, FundAdjustProdDO orgnlFundAdjustProdDO,
                                                            AccountingInstrDO accountingInstrDO, ActgAdjustRespBO actgAdjustRespBO) {
        // 《大额对接产品表》目标状态为《已冻结待清算》
        HvpsTransDO hvpsTransDO = HvpsTransDO.builder()
                .bizSts(clearReportReqDTO.getPrcStatus())
                .bizPrcCd(clearReportReqDTO.getPrcCode())
                .bizPrcInf(clearReportReqDTO.getPrcInf())
                .sttlmDt(clearReportReqDTO.getSettlementDate()).build();

        FundAdjustProdDO fundAdjustProdDO;
        if (AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            // 更新《资金调整产品表》状态为《已结算》
            fundAdjustProdDO = FundAdjustProdDO.builder()
                    .bizSts(ClearingStatusEnum.SETTLED.getCode())
                    .sttlmDt(actgAdjustRespBO.getAccountingDate())
                    .adjustWltId(actgAdjustRespBO.getWalletId())
                    .accountBalance(actgAdjustRespBO.getAccountBalance())
                    .accountFlag(actgAdjustRespBO.getCurrentSystemFlag())
                    .ciLimit(actgAdjustRespBO.getCiLimit())
                    .netQuota(actgAdjustRespBO.getNetQuota()).build();
        } else {
            // 结算钱包：预注资调增失败，几率很小，因为已经提前做了一次验证。
            // 更新《资金调整产品表》状态为《已失败》
            fundAdjustProdDO = FundAdjustProdDO.builder()
                    .bizSts(ClearingStatusEnum.FAILED.getCode())
                    .bizPrcCd(WholesaleErrorEnum.ACCT_REJECT.getCode())
                    .bizPrcInf(actgAdjustRespBO.getBizMsg()).build();

            log.error("致命错误，需紧急排查，大额预注资调增，结算钱包系统记账失败，orgnlHvpsMsgId={},orgnlHvpsSendPty={}," +
                            "orgnlMsgId={},orgnlTransId={},bizCode={},bizMsg={}",
                    clearReportReqDTO.getOrgnlMsgId(), clearReportReqDTO.getOrgnlSendPty(),
                    accountingInstrDO.getMsgId(), accountingInstrDO.getTransId(),
                    actgAdjustRespBO.getBizCode(), actgAdjustRespBO.getBizMsg());
        }

        fundingManager.updateFinishStatus(orgnlHvpsTransDO, hvpsTransDO, orgnlFundAdjustProdDO, fundAdjustProdDO,
                accountingInstrDO.getTransId(), actgAdjustRespBO);

        // 结算钱包返回失败，则不登记存储转发表（不通知机构）
        if (!AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            return Collections.emptyList();
        }

        log.info("《结算钱包系统》返回成功，大额返回成功，组装通知机构的dcep.185报文，并登记《存储转发表》（通知机构）");
        String receiver = orgnlFundAdjustProdDO.getSendPtyId();
        EnvelopeDTO<GwDTO> envelopeDTO = createNoticeInst185DTO(orgnlFundAdjustProdDO.getMsgId(), receiver);
        storageForwardManager.saveForInst(envelopeDTO, receiver);

        return Collections.singletonList(envelopeDTO);
    }

    private boolean sendInst(EnvelopeDTO<GwDTO> reqDTO, String msgId) {
        try {
            String noticeMsgId = reqDTO.getSoapBody().getT().fetchMsgId();

            log.info("调用网关开始，noticeMsgId={}, msgId:{}, reqDTO:{}", noticeMsgId, msgId, reqDTO);

            Response<EnvelopeDTO<GwDTO>> resp = gwoutService.execute(reqDTO);
            GwDTO respDTO = resp.getResult().body();

            // 应答结果校验
            if (respDTO instanceof Dcep90200101DTO
                    && noticeMsgId.equals(((Dcep90200101DTO) respDTO).getConfInf().getOrgnlMsgId())) {
                log.info("调用网关结束，应答报文为 dcep.902, noticeMsgId:{}, msgId:{}, respDTO:{}",
                        noticeMsgId, msgId, respDTO);
                return true;
            }

            log.info("调用网关结束，应答报文不是 dcep.902, noticeMsgId:{}, msgId:{}, respDTO:{}", noticeMsgId, msgId,
                    respDTO != null ? respDTO.toString() : "respDTO is null");
        } catch (Exception e) {
            log.error("调用网关异常", e);
        }

        return false;
    }
}
