package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;

/**
 * @author wangxiaoyu
 */
public interface PaymentManager {
    void record(EnvelopeDTO<GwDTO> gwReqDTO, AccountingInstrDO accountingInstrDO);
    void clearing(AccountingInstrDO accountingInstrDO, TransferRespDTO transferRespDTO);
    boolean debitConfirm(EnvelopeDTO<GwDTO> gwRespDTO, AccountingInstrDO accountingInstrDO);
    Response<AccountingInstrDO> resultReportProcess(EnvelopeDTO<GwDTO> gwReqDTO);
    void fail(EnvelopeDTO<GwDTO> gwReqDTO, ErrorEnum errorEnum);

    /**
     * 推定失败
     * @param settlementProdDO
     * @param accountingInstrDO
     */
    void presumeFail(SettlementProdDO settlementProdDO,AccountingInstrDO accountingInstrDO);
    /**
     * 付款方拒绝
     * @param settlementProdDO
     * @param accountingInstrDO
     */
    void payReject(SettlementProdDO settlementProdDO,AccountingInstrDO accountingInstrDO);

    /**
     * 交易完成、失败、排队
     * @param accountingInstrDO
     */
    void finished(AccountingInstrDO accountingInstrDO);

}
