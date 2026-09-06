package com.dcep.dips.wholesalepayment.manager.redo.impl;

import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.MsgTpEnum;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.PaymentManager;
import com.dcep.dips.wholesalepayment.manager.redo.SettleRedoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 网关付款类（贷记）
 */
@Slf4j
@Service
public class DbtrSettleRedoManagerImpl extends SettleRedoManager {

    @Resource
    private PaymentManager paymentManager;
    @Resource
    private CommonManager commonManager;
    @Resource
    private AccountingManager accountingManager;

    @PostConstruct
    public void register() {
        rodo.put(MsgTpEnum.FI_CDT.getCode(), this);
        rodo.put(MsgTpEnum.FI_RETUNE.getCode(), this);
        rodo.put(MsgTpEnum.CDT_REQUEST.getCode(), this);
        rodo.put(MsgTpEnum.RECOV_REQUEST.getCode(), this);
        rodo.put(MsgTpEnum.CDT_COV_REQUREST.getCode(), this);
        rodo.put(MsgTpEnum.REFUND_REQUREST.getCode(), this);
        rodo.put(MsgTpEnum.CRDT_ADJ_REQUREST.getCode(), this);
    }

    @Override
    protected boolean presume(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO, SettlementProdDO settle) {
        // 向结算钱包发起查询
        if (ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())) {
            AccountingInstrDO queryResult = commonManager.queryTransferStatus(accountingInstrDO);
            if(queryResult != null) {
                paymentManager.finished(queryResult);
            }
        }
        return false;
    }

    @Override
    protected boolean process(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO,SettlementProdDO settle) {
        if(ClearingStatusEnum.WAIT_SETTLE.getCode().equals(settle.getBizSts())) {
            Response<TransferRespDTO> result = accountingManager.transfer(accountingInstrDO);
            if (!result.isSuccess()) {
                // 调用结算钱包失败，直接返回，待计时控制组件处理
                log.info("transfer fail transId:{}", accountingInstrDO.getTransId());
                return false;
            }
            paymentManager.clearing(accountingInstrDO,result.getResult());
        }

        // 如调用结算钱包成功，同样返回false，由下次补偿幂等return
        return false;
    }
}
