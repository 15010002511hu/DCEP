package com.dcep.dips.wholesalepayment.manager.redo.impl;

import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.common.utils.CommonUtil;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.PaymentManager;
import com.dcep.dips.wholesalepayment.manager.redo.SettleRedoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 网关收款类（借记）
 */
@Slf4j
@Service
public class CdtrSettleRedoManagerImpl extends SettleRedoManager {

    @Resource
    private PaymentManager paymentManager;
    @Resource
    private CommonManager commonManager;
    @Resource
    private AccountingManager accountingManager;


    @PostConstruct
    public void register() {
        rodo.put(MsgTpEnum.DBT_REQUEST.getCode(), this);
        rodo.put(MsgTpEnum.COV_REQUREST.getCode(), this);
        rodo.put(MsgTpEnum.ORDR_CONF_RESPONSE.getCode(), this);
    }

    @Override
    protected boolean presume(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {
        if(ClearingStatusEnum.PROCESS.getCode().equals(settle.getBizSts())){
            //超过一定时间，推定失败
            paymentManager.presumeFail(settle,accountingInstrDO);
        } else if(ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())){
            AccountingInstrDO queryResult = commonManager.queryTransferStatus(accountingInstrDO);
            if(queryResult != null) {
                paymentManager.finished(queryResult);
            }
        }
        //业务逻辑会删除，所以不管成功失败均返回false
        return false;
    }

    @Override
    protected boolean process(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO,SettlementProdDO settle) {
        if(ClearingStatusEnum.PROCESS.getCode().equals(settle.getBizSts())){
            settle = commonManager.gwoutQuery(settle);
            if(settle == null){
                return false;
            }
            if(ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())) {
                if (settlementProdMapper.updateBizSts(new SettlementProdDO(accountingInstrDO.getMsgId(), ClearingStatusEnum.WAIT_SETTLE), ClearingStatusEnum.PROCESS.getCode()) != 1) {
                    log.error("更新结算产品表状态失败，msgId:{}", accountingInstrDO.getMsgId());
                    return false;
                }
            }
        }
        if(ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())){
            Response<TransferRespDTO> result = accountingManager.transfer(accountingInstrDO);
            if (!result.isSuccess()) {
                // 调用结算钱包失败，直接返回，待计时控制组件处理
                log.info("transfer fail transId:{}", accountingInstrDO.getTransId());
                return false;
            }
            paymentManager.clearing(accountingInstrDO,result.getResult());
        }else if(ClearingStatusEnum.FAILED.getCode().equals(settle.getBizSts())){
            paymentManager.payReject(settle,accountingInstrDO);
        }

        //业务逻辑会删除，所以返回false
        return false;
    }
}
