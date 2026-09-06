/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.service.impl;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.ValidateUtils;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.api.ChainService;
import com.dcep.dips.wholesalepayment.aspect.InnerReq;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ZerooutCtrlDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dal.model.SystemStatusDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustRespDTO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.enums.ZerooutPrcStsEnum;
import com.dcep.dips.wholesalepayment.manager.ChainManager;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.rpc.RpcContext;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@DubboService

public class ChainServiceImpl implements ChainService {

    @NacosValue(value = "${msgid_date_interval}", autoRefreshed = true)
    private String MSGID_DATE_INTERVAL;

    @Resource
    private ChainManager chainManager;
    @Resource
    private SettlementProdMapper settlementProdMapper;
    @Resource
    private AccountingInstrMapper accountingInstrMapper;
    @Resource
    private ZerooutCtrlDOMapper zerooutCtrlDOMapper;
    @Resource
    private SystemStatusDOMapper systemStatusDOMapper;
    @Resource
    private CommonManager commonManager;

    @Override
    @InnerReq
    public Response<OnChainAdjustRespDTO> onChainAdjust(OnChainAdjustReqDTO onChainAdjustReqDTO) throws DcepException {
        log.info("ChainService.onChainAdjust start: {}", onChainAdjustReqDTO);
        try {
            ValidateUtils.validate(onChainAdjustReqDTO);
        }catch (DcepException e){
            log.error("ChainService.onChainAdjust validate error: ", e);
            //此处直接抛出e会导致消费者反序列化失败，原因是抛出的异常带有ConstraintViolationException类，反射时构造方法Null指针异常。所以通过response返回，或者抛出不带ConstraintViolationException的异常。
            return new Response(e.getCode(),e.getMessage());
        }
        WholesaleErrorEnum errorEnum = checkBusinessInfo(onChainAdjustReqDTO);
        if(errorEnum!=null){
            return new Response<>(false,null,errorEnum.getCode(),errorEnum.getDescription());
        }

        AccountingInstrDO accountingInstrDO = null;
        try {
            accountingInstrDO = chainManager.record(onChainAdjustReqDTO);
        } catch (DuplicateKeyException e) {
            //2.1 插入数据重复逻辑处理
            return handleDuplicateData(onChainAdjustReqDTO);
        }
        return chainManager.transfer(accountingInstrDO,onChainAdjustReqDTO.getExpectedSettlementDate(),onChainAdjustReqDTO.getUseCurrentSystemFlag());
    }

    /**
     * 业务检查
     * @param onChainAdjustReqDTO
     * @return
     */
    private WholesaleErrorEnum checkBusinessInfo(OnChainAdjustReqDTO onChainAdjustReqDTO) {

        // 1. 报文标识号中的日期校验
        String currentDate = LocalDateTime.now().toString();
        if (!CheckUtil.checkClrMsgIdAndCreDtTm(currentDate, onChainAdjustReqDTO.getMsgId(),Integer.valueOf(MSGID_DATE_INTERVAL))) {
            log.info("msgId date is illegal: msgId={}, currentDate={}, msgId_date_interval={}", onChainAdjustReqDTO.getMsgId(),currentDate,MSGID_DATE_INTERVAL);
            return  WholesaleErrorEnum.MSGID_DATE_ILLEGAL;
        }

        //系统状态检查-报文中系统工作日是否与当前系统的匹配
        String msgIdDateStr = onChainAdjustReqDTO.getMsgId().substring(0, 8); // 报文标识号前8位日期
        boolean dateCheck = commonManager.checkSystemDate(msgIdDateStr);
        if (!dateCheck) {
            return WholesaleErrorEnum.MSGID_DATE_ILLEGAL;
        }
        SystemStatusDO systemStatusDO = systemStatusDOMapper.selectByPrimaryKey(CommonConstant.SysCode.WHOLESALE);
        boolean dateCheck1 = commonManager.checkSystemDate(onChainAdjustReqDTO.getExpectedSettlementDate());
        if (!dateCheck1) {
            String errorMsg = "期望结算日期不在正常范围内，当前记账日期: " + systemStatusDO.getCurSysDt();
            throw new DcepException(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), errorMsg);
        }

        //清零状态检查。
        ZerooutCtrlDO zerooutCtrlDO = zerooutCtrlDOMapper.selecPrcSts(systemStatusDO.getCurSysDt(),Constant.BCSP);
        if (zerooutCtrlDO != null && ZerooutPrcStsEnum.PROCESS.getCode().equals(zerooutCtrlDO.getPrcSts())) {
           return WholesaleErrorEnum.SYSTEM_STATE_ILLEGAL;
        }
        return null;
    }




    private Response<OnChainAdjustRespDTO> handleDuplicateData(OnChainAdjustReqDTO onChainAdjustReqDTO) {
        // 1.幂等后判断是第几步 若第二步则为transid生成重复情况，直接抛异常
        if (Constant.INSERT_STEP_TWO.equals(RpcContext.getContext().get(Constant.PREPARE_INSERT_STEP))) {
            log.error("ChainService.onChainAdjust duplicate: msgId={}, insertStep={}", onChainAdjustReqDTO.getMsgId(), Constant.INSERT_STEP_TWO);
            throw new DcepException(ErrorEnum.BUSI_DB_KEYWORD_REPEAT.getCode(),
                    ErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription());
        }
        // 2.若第一步，select原交易, 并对原交易核心要素幂等要素进行检查
        SettlementProdDO origSettlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(onChainAdjustReqDTO.getMsgId()));

        // 2.2.幂等要素(核心业务要素)，如不匹配,报异常
        if (!CheckUtil.idempotentMatch(onChainAdjustReqDTO, origSettlementProdDO)) {
            throw new DcepException(ErrorEnum.NO_MATCH_ORIGNAL.getCode(),ErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }

        if(!ClearingStatusEnum.WAIT_SETTLE.getCode().equals(origSettlementProdDO.getBizSts())){
            OnChainAdjustRespDTO onChainAdjustRespDTO = new OnChainAdjustRespDTO();
            onChainAdjustRespDTO.setBizStatus(origSettlementProdDO.getBizSts());
            onChainAdjustRespDTO.setSettlementDate(origSettlementProdDO.getSttlmDt());
            onChainAdjustRespDTO.setBizProcessCode(origSettlementProdDO.getBizPrcCd());
            onChainAdjustRespDTO.setBizProcessInfo(origSettlementProdDO.getBizPrcInf());
            return new Response<>(onChainAdjustRespDTO);
        }

        AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgIdPrepare(onChainAdjustReqDTO.getMsgId());
        return  chainManager.transfer(accountingInstrDO,origSettlementProdDO.getSttlmDt(),onChainAdjustReqDTO.getUseCurrentSystemFlag());
    }
}
