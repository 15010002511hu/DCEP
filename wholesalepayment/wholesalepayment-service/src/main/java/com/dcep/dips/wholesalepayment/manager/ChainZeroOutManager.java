package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutReportDTO;

public interface ChainZeroOutManager {

    /**
     * 记录清零控制表
     * @param zeroOutCtrl
     */
    int recordZeroOutCtrl(ZerooutCtrlDO zeroOutCtrl);

    /**
     * 更新清零空表更新记账指令表
     * @param zeroOutReportDto
     * @return
     */
    void recordZOCtrlAndAcctInstr(ZeroOutReportDTO zeroOutReportDto,ZerooutCtrlDO zerooutCtrlDO);

    /**
     * 按msgid获取原交易
     * @param msgId
     * @return
     */
    ZerooutCtrlDO selectByMsgId(String msgId);

    /**
     * 按任务编号获取原交易
     * @param taskId
     * @return
     */
    ZerooutCtrlDO selectByTaskId(String taskId);

    /**
     * 按系统日期,清零系统标识查询清零获取原交易
     * @param sysdt
     * @return
     */
    ZerooutCtrlDO selectBySysDtId(String sysdt,String id);

    /**
     * 清零数据更新
     * @param zerooutCtrlDO
     * @return
     */
    void updateByPrimaryKey(ZerooutCtrlDO zerooutCtrlDO);

    /**
     * 调用记账结算系统
     * @param accountingInstrDO
     * @return
     */
    Response<TransferRespDTO> transfer(AccountingInstrDO accountingInstrDO);

}
