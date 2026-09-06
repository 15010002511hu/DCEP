package com.dcep.dips.wholesalepayment.service.impl;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.dips.acctrans.constants.Constant.AccountingStatus;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustRespDTO;
import com.dcep.dips.wholesalepayment.api.FundingService;
import com.dcep.dips.wholesalepayment.aspect.GwReq;
import com.dcep.dips.wholesalepayment.aspect.InnerReq;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.bo.ActgAdjustRespBO;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.FundAdjustProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.HvpsTransMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.FundAdjustProdDO;
import com.dcep.dips.wholesalepayment.dal.model.HvpsTransDO;
import com.dcep.dips.wholesalepayment.dto.BizStatusDTO;
import com.dcep.dips.wholesalepayment.dto.FundingDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutReqDTO;
import com.dcep.dips.wholesalepayment.dto.acctrans.ZeroOutRespDTO;
import com.dcep.dips.wholesalepayment.dto.funding.IncreaseReqDTO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.ClearingCenterManager;
import com.dcep.dips.wholesalepayment.manager.FundingManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

@Slf4j
@DubboService
public class FundingServiceImpl implements FundingService {

    @NacosValue(value = "${credttm_interval}", autoRefreshed = true)
    private String CREDTTM_INTERVAL;

    @NacosValue(value = "${msgid_date_interval}", autoRefreshed = true)
    private String MSGID_DATE_INTERVAL;

    @Autowired
    private HvpsTransMapper hvpsTransMapper;

    @Autowired
    private FundAdjustProdMapper fundAdjustProdMapper;

    @Autowired
    private AccountingInstrMapper accountingInstrMapper;

    @Autowired
    private FundingManager fundingManager;

    @Autowired
    private AccountingManager accountingManager;

    @Autowired
    private ClearingCenterManager clearingCenterManager;

    @NacosValue(value = "${hvps_force_env_info}", autoRefreshed = true)
    private String HVPS_FORCE_ENV_INFO;

    /**
     * 跟准备金约定的 PR01-失败、PR10-成功
     * @param increaseReqDTO 请求对象
     * @return 应答对象
     * @throws DcepException 异常
     */
    @Override
    @InnerReq
    public Response<BizStatusDTO> increase(IncreaseReqDTO increaseReqDTO) throws DcepException {
        log.info("收到准备金请求-大额来账业务-{}，hvpsMsgTp={}, hvpsSendPtyId={}, hvpsMsgId={}, hvpsSendClrPtyId={}",
                getHvpsMsgDesc(increaseReqDTO.getMsgTp()), increaseReqDTO.getMsgTp(),
                increaseReqDTO.getSendMemberId(), increaseReqDTO.getMsgId(), increaseReqDTO.getClearingMemberId());

        // 由于对接大额系统的《准备金》只有一套测试环境
        // 而大额注资请求的112、115报文，处理成功后，需要将185报文通知机构
        // 此时需要将《准备金》过来的 envInfo 改写为机构需要的环境信息
        // 注意：生产环境不能配置（生产环境需要将nacos中的HVPS_ENV_INFO变量删除）
        if (!StringUtils.isEmpty(HVPS_FORCE_ENV_INFO)) {
            log.info("设置环境信息：{}", HVPS_FORCE_ENV_INFO);
            CommonUtil.setEnvInfo(HVPS_FORCE_ENV_INFO);
        }

        ValidateUtils.validate(increaseReqDTO);

        // 预注资调增，单独处理
        if (MsgTpEnum.CDT_FUND_PRE_INCREASE.getCode().equals(increaseReqDTO.getMsgTp())) {
            return preIncrease(increaseReqDTO);
        }

        // 数据落《大额对接产品表》、《资金调整产品表》、《记账指令表》、《档案表》
        AccountingInstrDO accountingInstrDO = null;
        try {
            log.info("FundingManagerImpl.increaseRecord start");
            Response<AccountingInstrDO> response = fundingManager.increaseRecord(increaseReqDTO);
            log.info("FundingManagerImpl.increaseRecord end, isSuccess={}", response.isSuccess());
            accountingInstrDO = response.getResult();
        } catch (DuplicateKeyException ex) { // 幂等idempotent后判断业务要素是否和原交易匹配，抛出异常，等待准备金重发
            String msgId = checkHvpsIncreaseIdempotent(increaseReqDTO);

            // 查询需要发送给结算钱包系统的transId
            accountingInstrDO = accountingInstrMapper.selectByMsgId(msgId);
        }

        // 如果《结算钱包》已经成功，则返回《准备金》成功，《准备金》摘除存储转发
        if (AccountingStatus.SUCCESS.equals(accountingInstrDO.getActgSts())) {
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
        }

        // 如果《结算钱包》已经失败，则返回《准备金》失败，《准备金》组hvps.112退汇报文
        if (AccountingStatus.FAILED.equals(accountingInstrDO.getActgSts())) {
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.FAILED.getCode(),
                    accountingInstrDO.getActgPrcInf(), accountingInstrDO.getActgPrcInf()));
        }

        // 调用结算钱包系统，应答结果有三种：通讯异常、调增成功、调增失败
        Response<AdjustRespDTO> actgResp = accountingManager.adjust(accountingInstrDO, null); // 注资调增
        if (!actgResp.isSuccess()) {
            // 结算钱包：通讯异常(状态不明)，抛异常，等待《准备金》存储转发
            throw new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        // 统一转换结算钱包返回对象
        ActgAdjustRespBO actgAdjustRespBO = new ActgAdjustRespBO(actgResp.getResult());

        // 更新业务状态
        List<EnvelopeDTO<GwDTO>> dtoList = fundingManager.updateIncreaseFinishStatus(increaseReqDTO, accountingInstrDO, actgAdjustRespBO);
        if (dtoList.isEmpty()) {
            log.info("《结算钱包》返回失败，返回给《准备金》失败，由《准备金》组hvps.112退汇报文");
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.FAILED.getCode(),
                    actgAdjustRespBO.getBizCode(), actgAdjustRespBO.getBizMsg()));
        }

        log.info("切换链路，后续链路标识msgSN为：msgId={}", accountingInstrDO.getMsgId());
        CommonUtil.LogMDC(accountingInstrDO.getMsgId());

        fundingManager.asyncSendInst(dtoList, accountingInstrDO.getMsgId(), null);

        return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
    }

    private String checkHvpsIncreaseIdempotent(IncreaseReqDTO increaseReqDTO) {
        if (Constant.INSERT_STEP_TWO.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
            log.error("运营中心生成的：transId重复，insertStep={}, 抛异常，让准备金重发", Constant.INSERT_STEP_TWO);
            throw new DcepException(WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(),
                    WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
        }

        HvpsTransDO orgnlHvpsTransDO = hvpsTransMapper.selectByPrimaryKey(new HvpsTransDO(increaseReqDTO));
        if (null == orgnlHvpsTransDO) {
            log.error("运营中心生成的：msgId重复，insertStep={}, 抛异常，让准备金重发", Constant.INSERT_STEP_ONE);
            throw new DcepException(WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(),
                    WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
        }

        log.info("大额业务重复，hvpsSendPty={}, hvpsMsgId={}", increaseReqDTO.getSendMemberId(), increaseReqDTO.getMsgId());
        CheckUtil.checkHvpsIncreaseIdempotent(increaseReqDTO, orgnlHvpsTransDO);

        return orgnlHvpsTransDO.getMsgId();
    }

    private Response<BizStatusDTO> preIncrease(IncreaseReqDTO increaseReqDTO) {
        // 检查不通过，不登记
        Response<BizStatusDTO> result = fundingManager.checkHvpsBusinessInfo(increaseReqDTO);
        if (result != null) {
            return result;
        }

        // 数据落《大额对接产品表》、《资金调整产品表》、《记账指令表》、《档案表》
        try {
            log.info("FundingManagerImpl.increaseRecord start");
            Response<AccountingInstrDO> response = fundingManager.increaseRecord(increaseReqDTO);
            log.info("FundingManagerImpl.increaseRecord end, isSuccess={}", response.isSuccess());
        } catch (DuplicateKeyException ex) {
            checkHvpsIncreaseIdempotent(increaseReqDTO);
        }

        log.info("返回给《准备金》成功，由《准备金》组《已确认》的hvps.117报文");
        return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
    }

    private Response<BizStatusDTO> testHvpsReport(ClearReportReqDTO clearReportReqDTO) {
        log.warn("默认返回成功：SUCC");
        return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode(), "DCEPI0000", ""));
    }

    /**
     * 跟准备金约定的 PR01-失败、PR10-成功
     */
    @Override
    @InnerReq
    public Response<BizStatusDTO> hvpsReport(ClearReportReqDTO clearReportReqDTO) throws DcepException {
        log.info("收到准备金请求-大额清算通知-原报文为{}，原报文信息：orgnlHvpsMsgTp={}, orgnlHvpsSendPty={}, orgnlHvpsMsgId={}",
                getOrgnlMsgDesc(clearReportReqDTO.getOrgnlMsgTp()), clearReportReqDTO.getOrgnlMsgTp(),
                clearReportReqDTO.getOrgnlSendPty(), clearReportReqDTO.getOrgnlMsgId());

        ValidateUtils.validate(clearReportReqDTO);

        log.info("《大额对接产品表》查询原记录：orgnlHvpsSendPty={},orgnlHvpsMsgId={}",
                clearReportReqDTO.getOrgnlSendPty(), clearReportReqDTO.getOrgnlMsgId());
        HvpsTransDO orgnlHvpsTransDO = hvpsTransMapper.selectByPrimaryKey(new HvpsTransDO(clearReportReqDTO));
        if (null == orgnlHvpsTransDO) {
            log.error("《大额对接产品表》查不到原记录：orgnlHvpsSendPty={},orgnlHvpsMsgId={}",
                    clearReportReqDTO.getOrgnlSendPty(), clearReportReqDTO.getOrgnlMsgId());
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        log.info("《大额对接产品表》原记录：{}", orgnlHvpsTransDO);

        log.info("切换链路，后续链路标识msgSN为：msgId={}", orgnlHvpsTransDO.getMsgId());

        CommonUtil.LogMDC(orgnlHvpsTransDO.getMsgId());

        log.info("收到准备金请求-大额清算通知，原报文信息：orgnlHvpsMsgTp={}, orgnlHvpsSendPty={}, orgnlHvpsMsgId={}",
                clearReportReqDTO.getOrgnlMsgTp(), clearReportReqDTO.getOrgnlSendPty(), clearReportReqDTO.getOrgnlMsgId());

        log.info("前置链路标识msgSN为：{}.{}", clearReportReqDTO.getOrgnlSendPty(), clearReportReqDTO.getOrgnlMsgId());

        log.info("《资金调整产品表》查询原记录：msgId={}", orgnlHvpsTransDO.getMsgId());
        FundAdjustProdDO orgnlFundAdjustProdDO =
                fundAdjustProdMapper.selectByPrimaryKey(new FundAdjustProdDO(orgnlHvpsTransDO.getMsgId()));
        if (null == orgnlFundAdjustProdDO) {
            log.error("《资金调整产品表》查不到原记录：msgId={}", orgnlHvpsTransDO.getMsgId());
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        log.info("《资金调整产品表》原记录：{}", orgnlFundAdjustProdDO);

        log.info("大额清算通知状态：{}{}，原业务大额状态为：{}{}，原注资调整类别为：{}{}",
                clearReportReqDTO.getPrcStatus(), HvpsClearingStatusEnum.getDesc(clearReportReqDTO.getPrcStatus()),
                orgnlHvpsTransDO.getBizSts(), HvpsClearingStatusEnum.getDesc(orgnlHvpsTransDO.getBizSts()),
                orgnlFundAdjustProdDO.getAdjustTp(), HvpsAdjTypEnum.getDesc(orgnlFundAdjustProdDO.getAdjustTp()));

        // 通知报文检查
        Response<BizStatusDTO> checkResult = CheckUtil.checkHvpsReportInfo(clearReportReqDTO, orgnlHvpsTransDO);
        if (checkResult != null) {
            return checkResult;
        }

        // 依据原调整类别处理
        if (HvpsAdjTypEnum.DECREASE.getCode().equals(orgnlFundAdjustProdDO.getAdjustTp())
                || HvpsAdjTypEnum.ZERO_OUT.getCode().equals(orgnlFundAdjustProdDO.getAdjustTp())) {
            // 原调整类别：注资调减、余额清零
            return decreaseFinish(clearReportReqDTO, orgnlHvpsTransDO, orgnlFundAdjustProdDO);
        }

        if (HvpsAdjTypEnum.PRE_DECREASE.getCode().equals(orgnlFundAdjustProdDO.getAdjustTp())) {
            // 原调整类别：预注资调减
            return preDecreaseFinish(clearReportReqDTO, orgnlHvpsTransDO, orgnlFundAdjustProdDO);
        }

        if (HvpsAdjTypEnum.PRE_INCREASE.getCode().equals(orgnlFundAdjustProdDO.getAdjustTp())) {
            // 原调整类别：预注资调增
            return preIncreaseFinish(clearReportReqDTO, orgnlHvpsTransDO, orgnlFundAdjustProdDO);
        }

        throw new DcepException(WholesaleErrorEnum.BUSI_NOT_SUPPORTED.getCode(),
                WholesaleErrorEnum.BUSI_NOT_SUPPORTED.getDescription());
    }

    /**
     * 预注资调增（saps.604通知）
     * @return Response对象
     */
    private Response<BizStatusDTO> preIncreaseFinish(ClearReportReqDTO clearReportReqDTO, HvpsTransDO orgnlHvpsTransDO,
                                               FundAdjustProdDO orgnlFundAdjustProdDO) {
        // 原报文编号为: hvps.115，回复hvps.117时状态为：PR10-已确认
        // 通知报文中业务状态可能出现：
        // 1. PR09-已拒绝，表示失败
        // 2. PR16-已冻结待清算，表示成功
        // 3. PR04-已清算，表示成功后，大额日间自动清算

        // 1. 大额返回《已清算》
        if (HvpsClearingStatusEnum.CLEARED.getCode().equals(clearReportReqDTO.getPrcStatus())) {
            if (HvpsClearingStatusEnum.FREEZE_WAIT_CLEAR.getCode().equals(orgnlHvpsTransDO.getBizSts())) {
                // 原状态为《已冻结待清算》，则更新为《已清算》
                HvpsTransDO hvpsTransDO = HvpsTransDO.builder()
                        .bizSts(clearReportReqDTO.getPrcStatus())
                        .bizPrcCd(clearReportReqDTO.getPrcCode())
                        .bizPrcInf(clearReportReqDTO.getPrcInf())
                        .sttlmDt(clearReportReqDTO.getSettlementDate()).build();

                fundingManager.updateFinishStatus(orgnlHvpsTransDO, hvpsTransDO,
                        null, null, null, null);
                log.info("大额清算通知状态：PR04已清算，原业务大额状态为：PR16已冻结待清算，正常时序流转，《大额对接产品表》更新为《已清算》");

                return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
            } else {
                log.error("大额清算通知状态：PR04已清算，原业务大额状态既不是：PR16已冻结待清算、也不是：PR04已清算，而是：{}，出现乱序。", orgnlHvpsTransDO.getBizSts());
                throw new DcepException(WholesaleErrorEnum.BUSI_DUPLICATION_STS.getCode(),
                        WholesaleErrorEnum.BUSI_DUPLICATION_STS.getDescription());
            }
        }

        // 2. 大额返回《已拒绝》
        if (HvpsClearingStatusEnum.REJECTED.getCode().equals(clearReportReqDTO.getPrcStatus())) {
            if (HvpsClearingStatusEnum.ACCEPTED.getCode().equals(orgnlHvpsTransDO.getBizSts())) {
                // 原状态是《已确认》，则更新状态为《已拒绝》
                HvpsTransDO hvpsTransDO = HvpsTransDO.builder()
                        .bizSts(clearReportReqDTO.getPrcStatus())
                        .bizPrcCd(clearReportReqDTO.getPrcCode())
                        .bizPrcInf(clearReportReqDTO.getPrcInf()).build();

                FundAdjustProdDO fundAdjustProdDO = FundAdjustProdDO.builder()
                        .bizSts(ClearingStatusEnum.FAILED.getCode())
                        .bizPrcCd(WholesaleErrorEnum.HVPS_REJECT.getCode())
                        .bizPrcInf(clearReportReqDTO.getPrcInf()).build();

                // 预注资调整检查时，登记过一笔记账指令，状态为处理中，这里修改为失败。
                AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgId(orgnlHvpsTransDO.getMsgId());
                if (null == accountingInstrDO) {
                    log.error("预注资检查时登记过一笔记账指令，这里查不到抛异常，msgId={}", orgnlHvpsTransDO.getMsgId());
                    throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(),
                            WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
                }
                ActgAdjustRespBO actgAdjustRespBO = new ActgAdjustRespBO(AccountingStatus.FAILED,
                        WholesaleErrorEnum.BUSI_REJT.getCode(), "预注资调增，大额通知失败，未调用结算钱包");

                fundingManager.updateFinishStatus(orgnlHvpsTransDO, hvpsTransDO,
                        orgnlFundAdjustProdDO, fundAdjustProdDO, accountingInstrDO.getTransId(), actgAdjustRespBO);

                log.info("大额清算通知状态：PR09已拒绝，原业务大额状态为：PR10已确认，《大额对接产品表》更新为《已拒绝》、《资金调整产品表》更新为《已失败》，《记账指令表》更新为《失败》");
                return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
            } else {
                log.error("大额清算通知状态：PR09已拒绝，原业务大额状态既不是：PR10已确认、也不是：PR09已拒绝，而是：{}，出现乱序。", orgnlHvpsTransDO.getBizSts());
                throw new DcepException(WholesaleErrorEnum.HVPS_DISORDER.getCode(),
                        WholesaleErrorEnum.HVPS_DISORDER.getDescription());
            }
        }

        // 大额清算通知状态：非PR16已冻结待清算
        if (!HvpsClearingStatusEnum.FREEZE_WAIT_CLEAR.getCode().equals(clearReportReqDTO.getPrcStatus())) {
            log.error("大额清算通知的状态不支持：{}", clearReportReqDTO.getPrcStatus());
            throw new DcepException(WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getCode(),
                    WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getDescription());
        }

        if (!HvpsClearingStatusEnum.ACCEPTED.getCode().equals(orgnlHvpsTransDO.getBizSts())) {
            log.error("大额清算通知状态：PR16已冻结待清算，原业务大额状态既不是：PR10已确认、也不是：PR16已冻结待清算，而是：{}，出现乱序。", orgnlHvpsTransDO.getBizSts());
            throw new DcepException(WholesaleErrorEnum.HVPS_DISORDER.getCode(),
                    WholesaleErrorEnum.HVPS_DISORDER.getDescription());
        }

        log.info("大额清算通知状态：PR16已冻结待清算，并且原业务大额状态为：RP10已确认，正常时序流转");
        return fundingManager.preIncreaseFinish(clearReportReqDTO, orgnlHvpsTransDO, orgnlFundAdjustProdDO);
    }

    @Override
    @InnerReq
    public Response<ZeroOutRespDTO> zeroOutApply(ZeroOutReqDTO zeroOutReqDTO) throws DcepException {
        log.info("接收到《结算钱包》的清零请求，transId: {}", zeroOutReqDTO.getTransId());

        ValidateUtils.validate(zeroOutReqDTO);

        // 1. 数据落《资金调整产品表》、《账务指令表》、《档案表》、《存储转发表》
        FundAdjustProdDO fundAdjustProdDO;
        try {
            log.info("FungindManagerImpl.zeroOutRecord start");
            Response<FundAdjustProdDO> response = fundingManager.zeroOutRecord(zeroOutReqDTO);
            log.info("FungindManagerImpl.zeroOutRecord end");
            fundAdjustProdDO = response.getResult();
        } catch (DuplicateKeyException ex) {
            log.info(Constant.DUPLICATION_EXCEPTION_LOG, zeroOutReqDTO.getTransId());

            String msgId = (String) RpcContext.getContext().get(Constant.PREPARE_INSERT_MSGID);

            if (Constant.INSERT_STEP_TWO.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
                log.error("运营中心生成的：msgId重复，msgId={}, insertStep={}，直接抛异常，让准备金重发", msgId, Constant.INSERT_STEP_TWO);
                throw new DcepException(WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(),
                        WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
            }

            AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByPrimaryKey(new AccountingInstrDO(zeroOutReqDTO));
            if (null == accountingInstrDO) {
                log.error("运营中心生成的：msgId重复，msgId={}, insertStep={}，直接抛异常，让结算钱包重发", msgId, Constant.INSERT_STEP_ONE);
                throw new DcepException(WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(),
                        WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
            }

            log.info("结算钱包生成的：transId重复，进行幂等检查");
            return CheckUtil.checkAccTransZerooutIdempotent(zeroOutReqDTO, accountingInstrDO);
        }

        // 异步发送大额系统，并返回成功给结算钱包
        clearingCenterManager.asyncSendHvps(zeroOutReqDTO, fundAdjustProdDO);

        // 组装应答报文
        ZeroOutRespDTO zeroOutRespDTO =  new ZeroOutRespDTO();
        zeroOutRespDTO.setTransId(zeroOutReqDTO.getTransId());
        zeroOutRespDTO.setMsgId(fundAdjustProdDO.getMsgId());
        zeroOutRespDTO.setEndToEndId(fundAdjustProdDO.getMsgId());
        zeroOutRespDTO.setPrcStatus(ClearingStatusEnum.SUCCESS.getCode());
        log.info("返回给《结算钱包》的清零应答，msgId={}, {}", zeroOutRespDTO.getMsgId(), zeroOutRespDTO);
        return new Response<>(true, zeroOutRespDTO);
    }

    @Override
    @GwReq
    public Response<EnvelopeDTO<GwDTO>> decrease(EnvelopeDTO<GwDTO> gwReqDTO) throws DcepException {
        FundingDTO fundingDTO = (FundingDTO) gwReqDTO.body();
        log.info("收到机构调减请求：msgId={}, {}", fundingDTO.msgId(), fundingDTO);

        // 业务检查
        checkDecreaseBizInfo(fundingDTO);

        // 数据落《资金调整产品表》、《账务指令表》、《档案表》、《存储转发表》
        AccountingInstrDO accountingInstrDO;
        try {
            log.info("FundingManagerImpl.decreaseRecord start msgId:{}, msgTp:{}", fundingDTO.msgId(), fundingDTO.msgTp());
            Response<AccountingInstrDO> response = fundingManager.decreaseRecord(gwReqDTO);
            log.info("FundingManagerImpl.decreaseRecord end msgId:{}, msgTp:{}, isSuccess:{}",
                    fundingDTO.msgId(), fundingDTO.msgTp(), response.isSuccess());
            accountingInstrDO = response.getResult();
        } catch (DuplicateKeyException ex) { // 幂等idempotent后判断业务要素是否和原交易匹配，如果不匹配报911错误，否则返原处理状态
            log.info(Constant.DUPLICATION_EXCEPTION_LOG, fundingDTO.msgId(), fundingDTO.msgTp());

            // 发送结算钱包的交易流水号(transId)重复，抛异常返回机构911报文
            if (Constant.INSERT_STEP_TWO.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
                log.error("transId insert repeat: msgId={}, insertStep={}", fundingDTO.msgId(), Constant.INSERT_STEP_TWO);
                throw new DcepException(WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(),
                        WholesaleErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
            }

            // msgId报文标识号重复，已经终态 或 报文要素检查不通过，抛异常返回机构911报文
            FundAdjustProdDO origFundAdjustProdDO =
                    fundAdjustProdMapper.selectByPrimaryKey(new FundAdjustProdDO(fundingDTO.msgId()));
            CheckUtil.checkDecreaseIdempotent(fundingDTO, origFundAdjustProdDO);

            // 查询需要发送给《结算钱包系统》记账指令
            accountingInstrDO = accountingInstrMapper.selectByMsgIdPrepare(fundingDTO.msgId());
        }

        // 调用结算钱包系统，应答结果有三种：通讯异常、业务成功、业务失败
        log.info("调用《结算钱包》进行调减冻结");
        Response<AdjustRespDTO> actgResp = accountingManager.adjust(accountingInstrDO, null); // 注资调减、预注资调减
        if (!actgResp.isSuccess()) {
            // 结算钱包：通讯异常（状态不明）
            // 抛异常返回机构911报文，等待存储转发重发，或者机构重发
            throw new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        ActgAdjustRespBO actgAdjustRespBO = new ActgAdjustRespBO(actgResp.getResult());

        log.info("更新《记账指令表》、《资金调整产品表》状态");
        FundAdjustProdDO fundAdjustProdDO =
                fundingManager.updateDecreasePrepareStatus(fundingDTO, accountingInstrDO, actgAdjustRespBO);

        if (AccountingStatus.SUCCESS.equals(actgAdjustRespBO.getAccountingStatus())) {
            clearingCenterManager.asyncSendHvps(gwReqDTO);
        }

        // 返回运营机构900报文
        return DtoUtil.assembly900Msg(gwReqDTO, fundAdjustProdDO);
    }

    /**
     * 注资调减、余额清零，清算结果通知
     * @param clearReportReqDTO 通知DTO对象
     * @param orgnlHvpsTransDO 大额对接产品表DO
     * @param orgnlFundAdjustProdDO 资金调整大额
     * @return 处理结果
     */
    private Response<BizStatusDTO> decreaseFinish(ClearReportReqDTO clearReportReqDTO, HvpsTransDO orgnlHvpsTransDO,
                                            FundAdjustProdDO orgnlFundAdjustProdDO) {
        // 原报文编号为: hvps.112，发送给hvps时初始为：PR10-已确认
        // 通知报文中业务状态可能出现：
        // 1. PR12-清算排队，非终态
        // 2. PR09-已拒绝，失败
        // 3. PR18-已退回，失败
        // 4. PR04-已清算，成功

        // 1. 接收到大额非终态通知：PR12-清算排队
        if (HvpsClearingStatusEnum.CLEAR_QUEUED.getCode().equals(clearReportReqDTO.getPrcStatus())) {
            log.error("致命错误，大额系统排队，运营中心头寸不足，请紧急排查，clearReportReqDTO={}", clearReportReqDTO);
            return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
        }

        // 2. 接收到大额终态通知：PR09-已拒绝、PR18-已退回、PR04-已清算
        if (!HvpsClearingStatusEnum.ACCEPTED.getCode().equals(orgnlHvpsTransDO.getBizSts())) {
            log.error("重复收到大额清算通知，并且两次通知状态不一致，抛出异常");
            throw new DcepException(WholesaleErrorEnum.BUSI_DUPLICATION_STS.getCode(),
                    WholesaleErrorEnum.BUSI_DUPLICATION_STS.getDescription());
        }

        // 3. 大额已清算为成功，否则为失败
        HvpsAdjStatusEnum hvpsAdjStatus = HvpsAdjStatusEnum.FAILED;
        if (HvpsClearingStatusEnum.CLEARED.getCode().equals(clearReportReqDTO.getPrcStatus())) {
            hvpsAdjStatus = HvpsAdjStatusEnum.SUCCESS;
        }

        log.error("原《大额对接产品表》状态是：PR10已确认，第一次收到大额的清算通知");
        return fundingManager.decreaseFinish(clearReportReqDTO, orgnlHvpsTransDO, orgnlFundAdjustProdDO, hvpsAdjStatus);
    }

    /**
     * 预注资调减，清算结果通知
     * @param clearReportReqDTO 通知DTO对象
     * @param orgnlHvpsTransDO 大额对接产品表DO
     * @param orgnlFundAdjustProdDO 资金调整大额
     * @return 处理结果
     */
    private Response<BizStatusDTO> preDecreaseFinish(ClearReportReqDTO clearReportReqDTO, HvpsTransDO orgnlHvpsTransDO,
                                               FundAdjustProdDO orgnlFundAdjustProdDO) {
        // 原报文编号为: hvps.118，发送给hvps时初始为：PR10-已确认
        // 通知报文中业务状态可能出现：
        // 1. PR09-已拒绝，表示预注资调减失败
        // 2. PR18-已退回，表示预注资调减成功
        // 3. PR04-已清算，表示预注资调减成功后，大额日间清算成功

        if (clearReportReqDTO.getPrcStatus().equals(HvpsClearingStatusEnum.CLEARED.getCode())
                && orgnlHvpsTransDO.getBizSts().equals(HvpsClearingStatusEnum.RETURNED.getCode())) {
            log.info("大额清算通知状态为：PR04已清算，原业务大额状态为：PR18已退回，只更新《大额对接产品表》状态");
            HvpsTransDO hvpsTransDO = HvpsTransDO.builder()
                    .bizSts(clearReportReqDTO.getPrcStatus())
                    .bizPrcCd(clearReportReqDTO.getPrcCode())
                    .bizPrcInf(clearReportReqDTO.getPrcInf())
                    .sttlmDt(clearReportReqDTO.getSettlementDate()).build();

            fundingManager.updateFinishStatus(orgnlHvpsTransDO, hvpsTransDO,
                    null, null, null, null);

            return new Response<>(new BizStatusDTO(ClearingStatusEnum.SETTLED.getCode()));
        }

        if (clearReportReqDTO.getPrcStatus().equals(HvpsClearingStatusEnum.CLEARED.getCode())) {
            log.error("大额清算通知状态为：PR04已清算，但原业务大额状态不是：PR18已退回，报文乱序，抛出异常");
            throw new DcepException(WholesaleErrorEnum.HVPS_DISORDER.getCode(),
                    WholesaleErrorEnum.HVPS_DISORDER.getDescription());
        }

        if (!HvpsClearingStatusEnum.ACCEPTED.getCode().equals(orgnlHvpsTransDO.getBizSts())) {
            log.error("原业务大额状态已不是：PR10已确认，大额通知报文重复，并且两次通知状态不一致，抛异常");
            throw new DcepException(WholesaleErrorEnum.HVPS_DISORDER.getCode(),
                    WholesaleErrorEnum.HVPS_DISORDER.getDescription());
        }

        // 大额已退回为成功，否则为失败
        HvpsAdjStatusEnum hvpsAdjStatus = HvpsAdjStatusEnum.FAILED;
        if (HvpsClearingStatusEnum.RETURNED.getCode().equals(clearReportReqDTO.getPrcStatus())) {
            hvpsAdjStatus = HvpsAdjStatusEnum.SUCCESS;
        }

        log.error("原业务大额状态为：PR10已确认，第一次收到清算通知");
        return fundingManager.decreaseFinish(clearReportReqDTO, orgnlHvpsTransDO, orgnlFundAdjustProdDO, hvpsAdjStatus);
    }

    /**
     * 注资、预注资调减申请，业务检查
     *
     * @param fundingDTO 资金调整DTO
     */
    private void checkDecreaseBizInfo(FundingDTO fundingDTO) {
        CheckUtil.checkCommonDateTime(fundingDTO, CREDTTM_INTERVAL, MSGID_DATE_INTERVAL);
        if (!InfoCacheUtil.checkInstState(fundingDTO.adjustPtyId())) {
            throw new DcepException(WholesaleErrorEnum.SENDER_STATE_ILLEGAL.getCode(),
                    WholesaleErrorEnum.SENDER_STATE_ILLEGAL.getDescription());
        }
    }

    private String getHvpsMsgDesc(String msgTp) {
        if (MsgTpEnum.CDT_FUND_INCREASE_IN.getCode().equals(msgTp)) {
            return MsgTpEnum.CDT_FUND_INCREASE_IN.getDescription();
        }

        if (MsgTpEnum.CDT_PRE_FUND_INCREASE_IN.getCode().equals(msgTp)) {
            return MsgTpEnum.CDT_PRE_FUND_INCREASE_IN.getDescription();
        }

        return msgTp;
    }

    private String getOrgnlMsgDesc(String orgnlMsgTp) {
        if (MsgTpEnum.CDT_PRE_FUND_INCREASE_IN.getCode().equals(orgnlMsgTp)) {
            return MsgTpEnum.CDT_PRE_FUND_INCREASE_IN.getDescription();
        }

        if (MsgTpEnum.CDT_FUND_DECREASE_OUT.getCode().equals(orgnlMsgTp)) {
            return MsgTpEnum.CDT_FUND_DECREASE_OUT.getDescription();
        }
        if (MsgTpEnum.CDT_PRE_FUND_DECREASE_OUT.getCode().equals(orgnlMsgTp)) {
            return MsgTpEnum.CDT_PRE_FUND_DECREASE_OUT.getDescription();
        }
        return orgnlMsgTp;
    }
}
