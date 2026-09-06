/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.service.impl;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.annotation.Clearing;
import com.dcep.dips.wholesalepayment.api.ReverseService;
import com.dcep.dips.wholesalepayment.aspect.GwReq;
import com.dcep.dips.wholesalepayment.common.utils.CheckUtil;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.ClearingDTO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.SettlementManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.dao.DuplicateKeyException;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@DubboService
public class ReverseServiceImpl implements ReverseService {

    @Resource
    private SettlementProdMapper settlementProdMapper;
    @Resource
    private CommonManager commonManager;
    @Resource
    private AccountingInstrMapper accountingInstrMapper;
    @Resource
    private SettlementManager settlementManager;

    @NacosValue(value = "${msgid_date_interval}", autoRefreshed = true)
    private String MSGID_DATE_INTERVAL;
    @NacosValue(value = "${credttm_interval}", autoRefreshed = true)
    String CREDTTM_INTERVAL;

    @Override
    @GwReq
    public Response<EnvelopeDTO> process(EnvelopeDTO<GwDTO> envelopeDTO) throws DcepException {

        ClearingDTO clearingDTO = (ClearingDTO) envelopeDTO.body();
        Clearing clearing = clearingDTO.getClass().getAnnotation(Clearing.class);
        log.info("ReverseService.process start msgId: {}, param: {}", clearingDTO.getClrMsgId(),envelopeDTO);
        SettlementProdDO settlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(clearingDTO.recOrgnlMsgId()));
        WholesaleErrorEnum errorEnum = checkBusinessInfo(envelopeDTO,settlementProdDO);
        if(errorEnum!=null){
            return DtoUtil.assembly428Msg(envelopeDTO,settlementProdDO,ClearingStatusEnum.FAILED.getCode());
        }

        try{
            if (settlementProdDO == null){
                log.error("ReverseService.process msgId: {}, reversal failed: source settle is not exist ", clearingDTO.getClrMsgId());
                //原交易不存在，插入一条失败的交易流水
                settlementProdDO = new SettlementProdDO(clearingDTO,clearing, ClearingStatusEnum.FAILED.getCode());
                settlementProdDO.setMsgId(clearingDTO.recOrgnlMsgId());
                settlementProdDO.setMsgTp(clearingDTO.recOrgnlMsgTp());
                settlementProdMapper.insert(settlementProdDO);

            }
        }catch (DuplicateKeyException e){
            settlementProdDO = settlementProdMapper.selectByPrimaryKey(new SettlementProdDO(clearingDTO.recOrgnlMsgId()));
        }

        if(ClearingStatusEnum.FAILED.getCode().equals(settlementProdDO.getBizSts())
                || ClearingStatusEnum.PRESUME_FAILED.getCode().equals(settlementProdDO.getBizSts())
                ||ClearingStatusEnum.CANCELLED.getCode().equals(settlementProdDO.getBizSts())
                ||ClearingStatusEnum.DAYEND_RETURN.getCode().equals(settlementProdDO.getBizSts())) {
            return DtoUtil.assembly428Msg(envelopeDTO,settlementProdDO,ClearingStatusEnum.SUCCESS.getCode());
        }else if(ClearingStatusEnum.PROCESS.getCode().equals(settlementProdDO.getBizSts())){
            //直接撤销
            AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgIdPrepare(settlementProdDO.getMsgId());
            settlementManager.reversalSuccess(accountingInstrDO,settlementProdDO,false);
            return DtoUtil.assembly428Msg(envelopeDTO, settlementProdDO, ClearingStatusEnum.SUCCESS.getCode());
        }else if(ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settlementProdDO.getBizSts())
                || ClearingStatusEnum.SETTLE_QUEUE.getCode().equals(settlementProdDO.getBizSts())){
                //****调用撤销*****
                SettlementProdDO settle = commonManager.reversal(settlementProdDO, false);
                if (settle == null) {
                    log.error("ReverseService.process msgId: {}, reversal failed: ", clearingDTO.getClrMsgId());
                    return DtoUtil.assembly428Msg(envelopeDTO, settlementProdDO, ClearingStatusEnum.FAILED.getCode());
                }
                if (ClearingStatusEnum.CANCELLED.getCode().equals(settlementProdDO.getBizSts())
                        || ClearingStatusEnum.FAILED.getCode().equals(settlementProdDO.getBizSts())
                        || ClearingStatusEnum.DAYEND_RETURN.getCode().equals(settlementProdDO.getBizSts())) {
                    return DtoUtil.assembly428Msg(envelopeDTO, settlementProdDO, ClearingStatusEnum.SUCCESS.getCode());
                }
        }

        // 其他状态(NONE,PRESUME_SUCCESS,SUCCESS,PARTY_PROCESS,ACCEPTED,SETTLED)，返回失败
        log.error("ReverseService.process msgId: {}, reversal failed: bizSts is illegal {}", clearingDTO.getClrMsgId(),settlementProdDO.getBizSts());
        return DtoUtil.assembly428Msg(envelopeDTO,settlementProdDO,ClearingStatusEnum.FAILED.getCode());


    }



    /**
     * 业务检查
     * @param envelopeDTO
     * @return
     */
    private WholesaleErrorEnum checkBusinessInfo(EnvelopeDTO<GwDTO> envelopeDTO,SettlementProdDO settlementProdDO) {
        ClearingDTO clearingDTO = (ClearingDTO) envelopeDTO.body();

        // 报文发送时间校验
        if (!CheckUtil.checkClrCreDtTm(clearingDTO.clrCreDtTm(), Long.valueOf(CREDTTM_INTERVAL))) {
            log.info("ReverseService process creDtTm is illegal: msgId={}, creDtTm={}, creDtTm_interval={}", clearingDTO.getClrMsgId(), clearingDTO.clrCreDtTm(), CREDTTM_INTERVAL);
            throw new DcepException(ErrorEnum.CREDTTM_ILLEGAL.getCode(),ErrorEnum.CREDTTM_ILLEGAL.getDescription());
        }

        // 1. 报文标识号中的日期校验
        String currentDate = LocalDateTime.now().toString();
        if (!CheckUtil.checkClrMsgIdAndCreDtTm(currentDate, clearingDTO.getClrMsgId(),Integer.valueOf(MSGID_DATE_INTERVAL))) {
            log.info("ReverseService process msgId date is illegal: msgId={}, currentDate={}, msgId_date_interval={}", clearingDTO.getClrMsgId(),currentDate,MSGID_DATE_INTERVAL);
            throw new DcepException(WholesaleErrorEnum.MSGID_DATE_ILLEGAL.getCode(),WholesaleErrorEnum.MSGID_DATE_ILLEGAL.getDescription());
        }

        //检查运营机构状态
        if (!InfoCacheUtil.checkInstState(clearingDTO.clrSendPtyId())) {
            log.info("ReverseService.process msgId: {},  sender state illegal, sendPtyId:{}", clearingDTO.getClrMsgId(),clearingDTO.clrSendPtyId());
            return WholesaleErrorEnum.SENDER_STATE_ILLEGAL;
        }

        //系统状态检查-报文中系统工作日是否与当前系统的匹配
//        String msgIdDateStr = clearingDTO.getClrMsgId().substring(0, 8); // 报文标识号前8位日期
//        boolean dateCheck = commonManager.checkSystemDate(msgIdDateStr);
//        if (!dateCheck) {
//            return WholesaleErrorEnum.MSGID_DATE_ILLEGAL;
//        }
        if( settlementProdDO!=null&& !checkReverse(settlementProdDO.getMsgTp())){
            log.error("ReverseService.process msgId: {}, reversal failed: bizTp is illegal {}", clearingDTO.getClrMsgId(),settlementProdDO.getBizTp());
            return WholesaleErrorEnum.BUSI_NOT_SUPPORTED;
        }
        //262报文发起机构为业务报文的接受机构.所以不进行校验
//        if(settlementProdDO!=null && !StrUtil.equals(settlementProdDO.getSendPtyId(),clearingDTO.clrSendPtyId())){
//            log.error("ReverseService.process msgId: {}, reversal failed: sendPtyId is illegal source:{},target:{}", clearingDTO.getClrMsgId(),clearingDTO.clrSendPtyId(),settlementProdDO.getSendPtyId());
//            return WholesaleErrorEnum.NO_MATCH_ORIGNAL;
//        }

        return null;
    }

    //原业务类型是否允许进行业务撤销
    private boolean checkReverse(String msgTp) {
        if(MsgTpEnum.FI_CDT.getCode().equals(msgTp) || MsgTpEnum.FI_RETUNE.getCode().equals(msgTp) || MsgTpEnum.CCP_REQUEST.getCode().equals(msgTp)
            ||MsgTpEnum.FMI_SSS_REQUEST.getCode().equals(msgTp) || MsgTpEnum.MER_SSS_REQUEST.getCode().equals(msgTp)
                || MsgTpEnum.CDT_REQUEST.getCode().equals(msgTp) ||  MsgTpEnum.DBT_REQUEST.getCode().equals(msgTp) || MsgTpEnum.RECOV_REQUEST.getCode().equals(msgTp)
                || MsgTpEnum.COV_REQUREST.getCode().equals(msgTp) || MsgTpEnum.CDT_COV_REQUREST.getCode().equals(msgTp) || MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(msgTp)
                || MsgTpEnum.REFUND_REQUREST.getCode().equals(msgTp) || MsgTpEnum.CRDT_ADJ_REQUREST.getCode().equals(msgTp)){
            return true;
        }
        return false;
    }
}
