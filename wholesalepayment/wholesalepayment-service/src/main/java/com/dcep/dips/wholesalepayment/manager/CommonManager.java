package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;

public interface CommonManager {

    boolean checkSystemDate(String bizDate);


    /**
     * 结算钱包排队撤销
     * @param settle
     * @param endReturn 日终退回和机构主动撤销调用
     */
    SettlementProdDO reversal(SettlementProdDO settle,boolean endReturn);


    /**
     * 结算钱包交易状态查询
     * @param accountingInstrDO
     */
    AccountingInstrDO queryTransferStatus(AccountingInstrDO accountingInstrDO);


    /**
     * 查询机构的确认状态
     * @param settlementProdDO
     * @return
     */
    SettlementProdDO gwoutQuery(SettlementProdDO settlementProdDO);

}
