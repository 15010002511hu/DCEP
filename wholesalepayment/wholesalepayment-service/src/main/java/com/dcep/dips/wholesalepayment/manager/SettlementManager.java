package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;

/**
 * 结算处理逻辑
 */
public interface SettlementManager {


    /**
     * 运营机构发起交易登记组件
     * 同一本地事务插入：结算产品表(交易幂等表)、记账指令表(交易流水表)、档案表(交易档案表)、超时控制表(交易控制表)
     * @param gwReqDTO
     */
//    Response<AccountingInstrDO> record(EnvelopeDTO<GwDTO> gwReqDTO);




    /**
     * 交易失败登记
     * @param
     */
//    Response<StorageForwardDO> fail(SettlementProdDO settle,AccountingInstrDO accountingInstrDO,boolean out);


    /**
     * 撤销成功。
     * @param accountingInstrDO
     * @param settle
     * @param endReturn 是否为日终退回  true是
     * @return
     */
    StorageForwardDO reversalSuccess(AccountingInstrDO accountingInstrDO, SettlementProdDO settle,boolean endReturn);

    /**
     * 调用钱包结算完成
     * @param accountingInstrDO
     * @return
     */
    void settleFinshed(AccountingInstrDO accountingInstrDO);

}
