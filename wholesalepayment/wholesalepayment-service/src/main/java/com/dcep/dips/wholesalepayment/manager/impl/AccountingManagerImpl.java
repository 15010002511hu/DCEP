package com.dcep.dips.wholesalepayment.manager.impl;

import com.alibaba.fastjson.JSONObject;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.api.AccountingService;
import com.dcep.dips.acctrans.api.QueryService;
import com.dcep.dips.acctrans.constants.Constant;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustNotifyReqDTO;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustNotifyRespDTO;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustReqDTO;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustRespDTO;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferReqDTO;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.acctrans.dto.liquid.QueryWalletDetailReqDTO;
import com.dcep.dips.acctrans.dto.liquid.QueryWalletDetailRespDTO;
import com.dcep.dips.common.constant.ChnlSysEnum;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.common.enums.ClearingProdErrorEnum;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dcep.dips.wholesalepayment.enums.*;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * 结算钱包系统管理类
 */
@Component
@Slf4j
public class AccountingManagerImpl implements AccountingManager {

    @DubboReference
    private QueryService queryService;

    @DubboReference
    private AccountingService accountingService;

    @Autowired
    private AccountingInstrMapper accountingInstrMapper;

    @Autowired
    private SystemStatusDOMapper systemStatusMapper;

    @Override
    public Response<QueryWalletDetailRespDTO> queryWalletDetail(QueryWalletDetailReqDTO detailReqDTO) {
        Response<QueryWalletDetailRespDTO> response;
        try {
            log.info("查询钱包详情，请求结算钱包：{}", detailReqDTO);
            response = queryService.queryWalletDetail(detailReqDTO);
            log.info("查询钱包详情，结算钱包应答：{}", response);
        } catch (DcepException e) {
            log.error("结算钱包应答：抛出异常DcepException。", e);
            return new Response<>(false, null, e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("结算钱包应答：抛出异常Exception。", e);
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        if (null == response) {
            log.error("结算钱包应答：为空。");
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        return response;
    }

    @Override
    public Response<AdjustRespDTO> adjust(AccountingInstrDO accountingInstrDO, ClearReportReqDTO clearReportReqDTO) {
        // 组结算钱包系统请求报文
        AdjustReqDTO adjustReqDTO = new AdjustReqDTO();
        adjustReqDTO.setTransId(accountingInstrDO.getTransId());
        adjustReqDTO.setMsgId(accountingInstrDO.getMsgId());
        adjustReqDTO.setEndToEndId(accountingInstrDO.getEndToEndId());
        adjustReqDTO.setProductCode(null);
        adjustReqDTO.setEventCode(null);
        adjustReqDTO.setAdjustType(accountingInstrDO.getMgmtTp());

        // 注资调减、预注资调减（结算钱包冻结）
        if (ActgMgmtTpEnum.CAP_INJECT_DECR.getCode().equals(accountingInstrDO.getMgmtTp())
                || ActgMgmtTpEnum.PRE_INJECT_DECR.getCode().equals(accountingInstrDO.getMgmtTp())) {
            adjustReqDTO.setWalletId(accountingInstrDO.getFromWlltId());
            adjustReqDTO.setClearingSystemId(accountingInstrDO.getFromClrSysId());
            adjustReqDTO.setClearingMemberId(accountingInstrDO.getFromClrMmbId());

            adjustReqDTO.setOtherWalletId(accountingInstrDO.getToWlltId());
            adjustReqDTO.setOtherClearingSystemId(accountingInstrDO.getToClrSysId());
            adjustReqDTO.setOtherClearingMemberId(accountingInstrDO.getToClrMmbId());
        }

        // 注资调增、预注资调增（结算钱包直接记账）
        if (ActgMgmtTpEnum.CAP_INJECT_INCR.getCode().equals(accountingInstrDO.getMgmtTp())
                || ActgMgmtTpEnum.PRE_INJECT_INCR.getCode().equals(accountingInstrDO.getMgmtTp())) {
            adjustReqDTO.setWalletId(accountingInstrDO.getToWlltId());
            adjustReqDTO.setClearingSystemId(accountingInstrDO.getToClrSysId());
            adjustReqDTO.setClearingMemberId(accountingInstrDO.getToClrMmbId());

            adjustReqDTO.setOtherWalletId(accountingInstrDO.getFromWlltId());
            adjustReqDTO.setOtherClearingSystemId(accountingInstrDO.getFromClrSysId());
            adjustReqDTO.setOtherClearingMemberId(accountingInstrDO.getFromClrMmbId());
        }

        // 如果是预注资调整的大额604<已冻结待清算>通知，则将大额的运行状态和通知报文的创建时间发送结算钱包
        // 由结算钱包决定是注入A账户，还是B账户。
        if (ActgMgmtTpEnum.PRE_INJECT_INCR.getCode().equals(accountingInstrDO.getMgmtTp())) {
            adjustReqDTO.setHvpsSysSts(clearReportReqDTO.getHvpsSysSts());
            adjustReqDTO.setHvpsCreDtTm(clearReportReqDTO.getHvpsCreDtTm());
        }

        adjustReqDTO.setCurrency(accountingInstrDO.getCurrency());
        adjustReqDTO.setAmount(accountingInstrDO.getAmount());
        adjustReqDTO.setAbstractCode(accountingInstrDO.getAbstractCd());
        adjustReqDTO.setAbstractDescription(accountingInstrDO.getAbstractDesc());

        Response<AdjustRespDTO> response;
        try {
            log.info("请求结算钱包：{}, {}", getAdjustTypeDesc(adjustReqDTO.getAdjustType()), adjustReqDTO);
            response = accountingService.adjust(adjustReqDTO);
            log.info("结算钱包应答：{}", response);
        } catch (DcepException e) {
            log.error("结算钱包应答：抛出异常DcepException。", e);
            return new Response<>(false, null, e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("结算钱包抛出异常Exception，通讯异常。", e);
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        if (null == response) {
            log.error("结算钱包应答：为空。");
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }
        return response;
    }

    @Override
    public Response<AdjustNotifyRespDTO> adjustmentResultNotify(AccountingInstrDO finishAccInstrDO, HvpsAdjStatusEnum hvpsAdjStatus) {
        AdjustNotifyReqDTO reqDTO = new AdjustNotifyReqDTO();
        reqDTO.setTransId(finishAccInstrDO.getTransId());
        reqDTO.setMsgId(finishAccInstrDO.getMsgId());
        reqDTO.setOrgnlTransId(finishAccInstrDO.getOrgnlTransId());
        reqDTO.setStatus(hvpsAdjStatus.getCode());

        reqDTO.setAdjustType(finishAccInstrDO.getMgmtTp());

        reqDTO.setOtherWalletId(InfoCacheUtil.getPbocDcepWlltId());
        reqDTO.setOtherClearingSystemId(ChnlSysEnum.DCEP.getCode());
        reqDTO.setOtherClearingMemberId(InfoCacheUtil.getPbocInf());

        reqDTO.setEndToEndId(finishAccInstrDO.getTransId());
        reqDTO.setProductCode(null);
        reqDTO.setEventCode(null);
        reqDTO.setAbstractCode(finishAccInstrDO.getAbstractCd());
        reqDTO.setAbstractDescription(finishAccInstrDO.getAbstractDesc());

        Response<AdjustNotifyRespDTO> response;
        try {
            log.info("请求结算钱包：{}, {}", getAdjustTypeDesc(reqDTO.getAdjustType()), reqDTO);
            response = accountingService.adjustmentResultNotify(reqDTO);
            log.info("结算钱包应答：{}", response);
        } catch (DcepException e) {
            log.error("结算钱包应答：抛出异常DcepException。", e);
            return new Response<>(false, null, e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("结算钱包应答：抛出异常Exception。", e);
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        if (null == response) {
            log.error("结算钱包应答：为空。");
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        return response;
    }

    private String getAdjustTypeDesc(String adjustType) {
        String desc = ActgMgmtTpEnum.getDesc(adjustType);
        return String.format("adjustType=%s(%s)", adjustType, desc);
    }

    @Override
    public Response<TransferRespDTO> transfer(AccountingInstrDO accountingInstrDO, String useCurrentSystemFlag,String settlementControlFlag) {
        return transfer(accountingInstrDO, null, useCurrentSystemFlag, settlementControlFlag);
    }

    @Override
    public Response<TransferRespDTO> transfer(AccountingInstrDO accountingInstrDO, String sttlmDt, String useCurrentSystemFlag,String settlementControlFlag) {
        // 如果是重试，已经是终态的话，直接返回
        if (ActgStsEnum.SUCCESS.getCode().equals(accountingInstrDO.getActgSts()) || ActgStsEnum.FAILED.getCode().equals(accountingInstrDO.getActgSts())) {
            TransferRespDTO adjustRespDTO = new TransferRespDTO();
            adjustRespDTO.setAccountingDate(accountingInstrDO.getActgDt());
            adjustRespDTO.setAccountingStatus(accountingInstrDO.getActgSts());
            adjustRespDTO.setBizCode(accountingInstrDO.getActgPrcCd());
            adjustRespDTO.setBizMsg(accountingInstrDO.getActgPrcInf());
            return new Response<>(true, adjustRespDTO);
        }

        // 组结算钱包系统请求报文
        TransferReqDTO transferReqDTO = new TransferReqDTO();
        transferReqDTO.setTransId(accountingInstrDO.getTransId());
        transferReqDTO.setMsgId(accountingInstrDO.getMsgId());
        transferReqDTO.setMsgType(accountingInstrDO.getMsgTp());
        transferReqDTO.setSendClearingMemberId(accountingInstrDO.getSendPtyId());
        transferReqDTO.setAccountingBizType(accountingInstrDO.getActgBizTp());
        transferReqDTO.setAccountingBizKind(accountingInstrDO.getActgBizKind());
        transferReqDTO.setBizPriority(accountingInstrDO.getBizPrty());
        transferReqDTO.setSystemCode(CommonConstant.SysCode.WHOLESALE);
        transferReqDTO.setEndToEndId(accountingInstrDO.getEndToEndId()); // 可以填写交易流水号
        transferReqDTO.setFromWalletId(accountingInstrDO.getFromWlltId());
        transferReqDTO.setFromClearingSystemId(accountingInstrDO.getFromClrSysId());
        transferReqDTO.setFromClearingMemberId(accountingInstrDO.getFromClrMmbId());
        transferReqDTO.setToWalletId(accountingInstrDO.getToWlltId());
        transferReqDTO.setToClearingSystemId(accountingInstrDO.getToClrSysId());
        transferReqDTO.setToClearingMemberId(accountingInstrDO.getToClrMmbId());
        transferReqDTO.setCurrency(accountingInstrDO.getCurrency());
        transferReqDTO.setAmount(accountingInstrDO.getAmount());
        transferReqDTO.setAbstractCode(accountingInstrDO.getAbstractCd());
        transferReqDTO.setAbstractDescription(accountingInstrDO.getAbstractDesc());
        transferReqDTO.setUseCurrentSystemFlag(useCurrentSystemFlag);
        transferReqDTO.setSettlementControlFlag(settlementControlFlag);
        transferReqDTO.setAccountingDate(sttlmDt);//此处为结算日期
        // 1. 调用结算钱包记账
        Response<TransferRespDTO> response;
        try {
            log.info("请求结算钱包:{}", JSONObject.toJSONString(transferReqDTO));
            response = accountingService.transfer(transferReqDTO);
            log.info("结算钱包响应:{}", JSONObject.toJSONString(response));
        } catch (DcepException e) {
            log.error("结算钱包抛出异常DcepException，通讯异常，错误信息: code={}, message={}", e.getCode(), e.getMessage());
            return new Response<>(false, null, e.getCode(), e.getMessage());
        } catch (Exception e) {
            log.error("结算钱包抛出异常Exception，通讯异常，错误信息: message={}", e.getMessage(), e);
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        if (null == response) {
            log.error("结算钱包返回空应答，通讯异常。");
            return new Response<>(false, null,
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                    WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        return response;
    }

    /**
     * 更新记账指令表
     *
     * @param accountingInstrDO
     * @return
     */
    @Override
    public int updateAccountingInstr(AccountingInstrDO accountingInstrDO) {
        return accountingInstrMapper.updateAccountingInstr(accountingInstrDO);
    }

    @Override
    public Response<TransferRespDTO> transfer(AccountingInstrDO accountingInstrDO) {
        return transfer(accountingInstrDO,null,"00100000"); // todo 暂定默认值，后续需要修改
    }




    @Override
    public AccountingInstrDO commonTransfer(AccountingInstrDO accountingInstrDO, String sttlmDt, String useCurrentSystemFlag,String settlementControlFlag) {
        // 如果是重试，已经是终态的话，直接返回
        if (ActgStsEnum.SUCCESS.getCode().equals(accountingInstrDO.getActgSts())
                || ActgStsEnum.FAILED.getCode().equals(accountingInstrDO.getActgSts())) {
            return accountingInstrDO;
        }

        // 组结算钱包系统请求报文
        TransferReqDTO transferReqDTO = new TransferReqDTO();
        transferReqDTO.setTransId(accountingInstrDO.getTransId());
        transferReqDTO.setMsgId(accountingInstrDO.getMsgId());
        transferReqDTO.setMsgType(accountingInstrDO.getMsgTp());
        transferReqDTO.setSendClearingMemberId(accountingInstrDO.getSendPtyId());
        transferReqDTO.setAccountingBizType(accountingInstrDO.getActgBizTp());
        transferReqDTO.setAccountingBizKind(accountingInstrDO.getActgBizKind());
        transferReqDTO.setBizPriority(accountingInstrDO.getBizPrty());
        transferReqDTO.setSystemCode(CommonConstant.SysCode.WHOLESALE);
        transferReqDTO.setEndToEndId(accountingInstrDO.getEndToEndId()); // 可以填写交易流水号
        transferReqDTO.setFromWalletId(accountingInstrDO.getFromWlltId());
        transferReqDTO.setFromClearingSystemId(accountingInstrDO.getFromClrSysId());
        transferReqDTO.setFromClearingMemberId(accountingInstrDO.getFromClrMmbId());
        transferReqDTO.setToWalletId(accountingInstrDO.getToWlltId());
        transferReqDTO.setToClearingSystemId(accountingInstrDO.getToClrSysId());
        transferReqDTO.setToClearingMemberId(accountingInstrDO.getToClrMmbId());
        transferReqDTO.setCurrency(accountingInstrDO.getCurrency());
        transferReqDTO.setAmount(accountingInstrDO.getAmount());
        transferReqDTO.setAbstractCode(accountingInstrDO.getAbstractCd());
        transferReqDTO.setAbstractDescription(accountingInstrDO.getAbstractDesc());
        transferReqDTO.setUseCurrentSystemFlag(useCurrentSystemFlag);
        transferReqDTO.setSettlementControlFlag(settlementControlFlag);
        transferReqDTO.setAccountingDate(sttlmDt);//此处为结算日期
        // 1. 调用结算钱包记账

        try {
            log.info("请求结算钱包:{}", JSONObject.toJSONString(transferReqDTO));
            Response<TransferRespDTO> response = accountingService.transfer(transferReqDTO);
            log.info("结算钱包响应:{}", JSONObject.toJSONString(response));
            if (null == response || !response.isSuccess()) {
                log.error("结算钱包返回错误，通讯异常。");
                return null;
            }
            if (null == response.getResult()){
                log.error("结算钱包返回为空，通讯异常。");
                return null;
            }
            if(ActgStsEnum.PROCESS.getCode().equals(response.getResult().getAccountingStatus())){
                log.error("结算钱包返回状态未知：{}，通讯异常。",response.getResult().getAccountingStatus());
                return null;
            }

            accountingInstrDO.setActgSts(response.getResult().getAccountingStatus());
            accountingInstrDO.setActgPrcCd(response.getErrorCode());
            accountingInstrDO.setActgPrcInf(response.getErrorMsg());
            accountingInstrDO.setActgDt(response.getResult().getAccountingDate());
            return accountingInstrDO;

        } catch (Exception e) {
            log.error("结算钱包抛出异常Exception，通讯异常，错误信息: message={}", e.getMessage(), e);
            return null;
        }
    }
}
