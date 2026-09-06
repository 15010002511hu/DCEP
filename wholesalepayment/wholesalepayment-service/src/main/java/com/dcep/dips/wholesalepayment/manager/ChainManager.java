package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustRespDTO;

/**
 * @author hx.zhaolei
 */
public interface ChainManager {

    /**
     * 链上交易登记
     * @param onChainAdjustReqDTO
     */
    AccountingInstrDO record(OnChainAdjustReqDTO onChainAdjustReqDTO);

    /**
     * 调用钱包结算
     * @param accountingInstrDO
     * @param sttlmDt 结算日期
     * @param useCurrentSystemFlag A/B系统
     * @return
     */
    Response<OnChainAdjustRespDTO> transfer(AccountingInstrDO accountingInstrDO,String sttlmDt, String useCurrentSystemFlag);
}
