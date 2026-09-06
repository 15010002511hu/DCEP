package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustNotifyRespDTO;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustRespDTO;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.acctrans.dto.liquid.QueryWalletDetailReqDTO;
import com.dcep.dips.acctrans.dto.liquid.QueryWalletDetailRespDTO;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dto.hvps.ClearReportReqDTO;
import com.dcep.dips.wholesalepayment.enums.HvpsAdjStatusEnum;

/**
 * 结算钱包系统管理接口
 */
public interface AccountingManager {

    Response<QueryWalletDetailRespDTO> queryWalletDetail(QueryWalletDetailReqDTO detailReqDTO);
    Response<AdjustRespDTO> adjust(AccountingInstrDO accountingInstrDO, ClearReportReqDTO clearReportReqDTO);

    Response<AdjustNotifyRespDTO> adjustmentResultNotify(AccountingInstrDO finishAccInstrDO, HvpsAdjStatusEnum hvpsAdjStatus);
    Response<TransferRespDTO> transfer(AccountingInstrDO accountingInstrDO);

    /**
     * 调用钱包记账业务请求
     * @param accountingInstrDO
     * @param useCurrentSystemFlag 使用系统标志  A(使用A系统)/B(使用B系统)/N(不使用当前系统标志)/空(默认，使用当前系统标志)
     * @param settlementControlFlag 结算控制标志
     * @return
     */
    Response<TransferRespDTO> transfer(AccountingInstrDO accountingInstrDO,String useCurrentSystemFlag,String settlementControlFlag);
    /**
     * 调用钱包记账业务请求
     * @param accountingInstrDO
     * @param sttlmDt 结算日期
     * @param useCurrentSystemFlag 使用系统标志  A(使用A系统)/B(使用B系统)/N(不使用当前系统标志)/空(默认，使用当前系统标志)
     * @param settlementControlFlag 结算控制标志
     * @return
     */
    Response<TransferRespDTO> transfer(AccountingInstrDO accountingInstrDO, String sttlmDt, String useCurrentSystemFlag,String settlementControlFlag);

    /**
     * 更新记账指令表
     * @param accountingInstrDO
     * @return
     */
    int updateAccountingInstr(AccountingInstrDO accountingInstrDO);

    /**
     * 调用钱包记账业务请求()
     * @param accountingInstrDO
     * @param sttlmDt 结算日期
     * @param useCurrentSystemFlag 使用系统标志  A(使用A系统)/B(使用B系统)/N(不使用当前系统标志)/空(默认，使用当前系统标志)
     * @param settlementControlFlag 结算控制标志
     * @return AccountingInstrDO 未知状态返回为null
     */
    AccountingInstrDO commonTransfer(AccountingInstrDO accountingInstrDO, String sttlmDt, String useCurrentSystemFlag,String settlementControlFlag);
}
