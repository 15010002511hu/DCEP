package com.dcep.dips.wholesalepayment.manager.impl;

import com.alibaba.fastjson.JSON;
import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.api.AccountingService;
import com.dcep.dips.acctrans.dto.accounting.query.QueryTransferStatusReqDTO;
import com.dcep.dips.acctrans.dto.accounting.query.QueryTransferStatusRespDTO;
import com.dcep.dips.acctrans.dto.accounting.transfer.ReversalReqDTO;
import com.dcep.dips.acctrans.dto.accounting.transfer.ReversalRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.common.dto.dc412.Dcep41200101DTO;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.CommonRecordMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.CommonStsctrlMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.*;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.CommonStsctrlManager;
import com.dcep.dips.wholesalepayment.manager.SettlementManager;
import com.dcep.dips.wholesalepayment.manager.WholesaleReportManager;
import com.dcep.dips.wholesalepayment.manager.redo.RedoManager;
import com.dcep.supergw.api.GwoutService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CommonManagerImpl implements CommonManager {

    @Resource
    SystemStatusDOMapper systemStatusDOMapper;
    @Resource
    private AccountingInstrMapper accountingInstrMapper;
    @DubboReference
    private AccountingService accountingService;
    @Resource
    private SettlementManager settlementManager;
    @Resource
    private WholesaleReportManager wholesaleReportManager;
    @DubboReference //(url = "dubbo://172.21.53.15:20880")
    private GwoutService gwoutService;
    @Autowired
    private CommonRecordMapper commonRecordMapper;
    @Autowired
    private CommonStsctrlMapper commonStsctrlMapper;
    /**
     * 校验系统日期
     * @param bizDate
     * @return
     */
    @Override
    public boolean checkSystemDate(String bizDate) {
        // 从系统状态表中获取当前系统日期
        SystemStatusDO systemStatusDO = systemStatusDOMapper.selectByPrimaryKey(CommonConstant.SysCode.WHOLESALE);
        if (null == systemStatusDO ) {
            log.error("SystemStatus table not init");
            throw new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), ErrorEnum.UNKNOWN_EXCEPTION.getDescription());
        }

        if (bizDate.equals(systemStatusDO.getCurSysDt())) {
            // 业务日期等于当期系统日期时，检查通过
            return true;
        }

        // TODO SS00 常量类定义
        if ("SS00".equals(systemStatusDO.getCurSysSts()) && (bizDate.equals(
            systemStatusDO.getNextSysDt()))) {
            // 日切开始时，业务日期支持下一期系统日期，检查通过
            return true;
        }

        // 其他情况检查不通过
        log.info("checkSystemDate illegal bizDate={}", bizDate);
        return false;
    }


    @Override
    public SettlementProdDO reversal(SettlementProdDO settle,boolean endReturn){

        AccountingInstrDO accountingInstrDO = accountingInstrMapper.selectByMsgIdPrepare(settle.getMsgId());
        if(accountingInstrDO == null){
            throw new DcepException(WholesaleErrorEnum.NO_MATCH_ORIGNAL.getCode(), WholesaleErrorEnum.NO_MATCH_ORIGNAL.getDescription());
        }
        ReversalReqDTO reversalReqDTO = getReversalReqDTO(accountingInstrDO);

        try{
            log.info("CommonManager.reversal request param: {}",reversalReqDTO);
            Response<ReversalRespDTO> reversal = accountingService.reversal(reversalReqDTO);
            if (reversal==null || !reversal.isSuccess() || reversal.getResult()==null){
                log.error("CommonManager.reversal result unknow, request param: {}",reversalReqDTO);
                throw new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(), WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription());
            }

            ReversalRespDTO reversalRespDTO = reversal.getResult();
            if (ActgStsEnum.QUEUE_CANCELED.getCode().equals(reversalRespDTO.getAccountingStatus())){
                settlementManager.reversalSuccess(accountingInstrDO,settle,endReturn);
                return settle;
            }else{
                //调用结算钱包撤销，但是结算钱包返回非排队取消状态。需要先查询，再修改状态。需要看下结算钱包是否有返回？？
                RedoManager redoManager = RedoManager.get(accountingInstrDO.getMsgTp());
                if(redoManager==null){
                    log.error("StsCtrlManagerImpl redo Error MsgId:{}, RedoManager is null", settle.getMsgId());
                    return null;
                }
                CommonStsctrlDO commonStsctrlDO = commonStsctrlMapper.selectByPrimaryKey(new CommonStsctrlDO(settle.getMsgId()));
                redoManager.redo(commonStsctrlDO,accountingInstrDO);
                return settle;
            }
        }catch (DcepException e){
            log.error("CommonManager.reversal response dcep exception. MsgId={}, exception={}",settle.getMsgId(),e);
        }catch (Exception e){
            log.error("CommonManager.reversal response exception. MsgId={}, exception:",settle.getMsgId(),e);
        }
        return null;
    }

    @Override
    public AccountingInstrDO queryTransferStatus(AccountingInstrDO accountingInstrDO) {
        QueryTransferStatusReqDTO queryTransferStatusReqDTO = new QueryTransferStatusReqDTO();
        queryTransferStatusReqDTO.setTransId(accountingInstrDO.getTransId());
        queryTransferStatusReqDTO.setFromClearingSystemId(accountingInstrDO.getFromClrSysId());
        queryTransferStatusReqDTO.setFromClearingMemberId(accountingInstrDO.getFromClrMmbId());
        queryTransferStatusReqDTO.setToClearingSystemId(accountingInstrDO.getToClrSysId());
        queryTransferStatusReqDTO.setToClearingMemberId(accountingInstrDO.getToClrMmbId());
        queryTransferStatusReqDTO.setCurrency(accountingInstrDO.getCurrency());
        queryTransferStatusReqDTO.setAmount(accountingInstrDO.getAmount());
        log.debug("CommonManager.queryTransferStatus request param: {}",queryTransferStatusReqDTO);
        try {
            Response<QueryTransferStatusRespDTO> response = accountingService.queryTransferStatus(queryTransferStatusReqDTO);
            if(response == null || !response.isSuccess()){
                log.error("CommonManager.queryTransferStatus response unknow, result : {}",response);
                return null;
            }
            if(response.getResult() == null){
                log.error("CommonManager.queryTransferStatus response result is null msgId : {} , transId: {}",accountingInstrDO.getMsgId(),accountingInstrDO.getTransId());
                return null;
            }
            if(ActgStsEnum.PROCESS.getCode().equals(response.getResult().getAccountingStatus())){
                log.error("CommonManager.queryTransferStatus response status error:{} ",response.getResult().getAccountingStatus());
                return null;
            }
            log.debug("CommonManager.queryTransferStatus response success, result : {}",response);
            accountingInstrDO.setActgSts(response.getResult().getAccountingStatus());
            accountingInstrDO.setActgPrcCd(response.getErrorCode());
            accountingInstrDO.setActgPrcInf(response.getErrorMsg());
            accountingInstrDO.setActgDt(response.getResult().getAccountingDate());
            return accountingInstrDO;
        }catch (Exception e){
            log.error("CommonManager.queryTransferStatus response  exception. MsgId={}, exception:",accountingInstrDO.getMsgId(),e);
            return null;
        }
    }

    private ReversalReqDTO getReversalReqDTO(AccountingInstrDO accountingInstrDO) {
        ReversalReqDTO reversalReqDTO = new ReversalReqDTO() ;
        reversalReqDTO.setMsgId(accountingInstrDO.getMsgId());
        reversalReqDTO.setTransId(accountingInstrDO.getTransId());
        reversalReqDTO.setMsgType(accountingInstrDO.getMsgTp());
        reversalReqDTO.setSendClearingMemberId(accountingInstrDO.getSendPtyId());
        reversalReqDTO.setAccountingBizType(accountingInstrDO.getActgBizTp());
        reversalReqDTO.setAccountingBizKind(accountingInstrDO.getActgBizKind());
        reversalReqDTO.setBizPriority(accountingInstrDO.getBizPrty());
        reversalReqDTO.setSystemCode(CommonConstant.SysCode.WHOLESALE);
        reversalReqDTO.setEndToEndId(accountingInstrDO.getEndToEndId());
        reversalReqDTO.setFromClearingSystemId(accountingInstrDO.getFromClrSysId());
        reversalReqDTO.setFromClearingMemberId(accountingInstrDO.getFromClrMmbId());
        reversalReqDTO.setToClearingSystemId(accountingInstrDO.getToClrSysId());
        reversalReqDTO.setToClearingMemberId(accountingInstrDO.getToClrMmbId());
        reversalReqDTO.setCurrency(accountingInstrDO.getCurrency());
        reversalReqDTO.setAmount(accountingInstrDO.getAmount());
        reversalReqDTO.setAbstractCode(accountingInstrDO.getAbstractCd());
        reversalReqDTO.setAbstractDescription(accountingInstrDO.getAbstractDesc());
        reversalReqDTO.setFromWalletId(accountingInstrDO.getFromWlltId());
        reversalReqDTO.setToWalletId(accountingInstrDO.getToWlltId());
        return reversalReqDTO;
    }



    @Override
    public SettlementProdDO gwoutQuery(SettlementProdDO settlementProdDO) {
        try {
            EnvelopeDTO<GwDTO> query411 = DtoUtil.assembly411Msg(settlementProdDO.getRecvPtyId(), settlementProdDO);
            // 1.通过网关请求接收机构，查询交易结果
            log.info("gwoutQuery request:{}", query411);
            Response<EnvelopeDTO<GwDTO>> result = gwoutService.execute(query411);
            log.info("gwoutQuery response:{}", result);
            if(result == null || result.getResult() == null || result.getResult().body() == null){
                log.info("gwout query 411 response is null ");
                return null;
            }

            if(!result.isSuccess()){
                log.info("gwout query 411 response unknow code:{},msg:{}",result.getErrorCode(),result.getErrorMsg());
                return null;
            }

            if(!(result.getResult().body() instanceof Dcep41200101DTO)) {
               log.error("gwout 411 query response no 412 ");
                return null;
            }

            Dcep41200101DTO dcep412 = (Dcep41200101DTO) result.getResult().body();
            if (dcep412.getBizQryRef() == null || dcep412.getBizRpt() == null) {
                log.error("gwout 411 query response is null");
                return null;
            }
            if (ClearingStatusEnum.SUCCESS.getCode().equals(dcep412.getBizQryRef().getQryRs())) {
                if (ClearingStatusEnum.SUCCESS.getCode().equals(dcep412.getBizRpt().getTrnRs())) {
                    try{
                        EnvelopeDTO<GwDTO> recordDTO = (EnvelopeDTO<GwDTO>) result.getResult();
                        commonRecordMapper.insert(new CommonRecordDO(settlementProdDO.getMsgId(),recordDTO.getSoapHeader().getMsgTp(), JSON.toJSONString(recordDTO)));
                    }catch (DuplicateKeyException e){
                        //重复不做处理
                    }
                    settlementProdDO.setBizSts(ClearingStatusEnum.WAIT_SETTLE.getCode());
                    return settlementProdDO;
                } else if (ClearingStatusEnum.FAILED.getCode().equals(dcep412.getBizRpt().getTrnRs())) {
                    if(MsgTpEnum.ORDR_CONF_RESPONSE.getCode().equals(settlementProdDO.getMsgTp())){
                        //262查询失败，不处理，直接返回。
                        return null;
                    }
                    settlementProdDO.setBizSts(ClearingStatusEnum.FAILED.getCode());
                    return settlementProdDO;
                }
            }
            log.error("gwout 411 query response unknow status query_status:{}, trx_status:{}",dcep412.getBizQryRef().getQryRs(),dcep412.getBizRpt().getTrnRs());
            return null;

        }  catch (Exception e) {
            log.error("gwout query Exception error", e);
            return null;
        }
    }
}
