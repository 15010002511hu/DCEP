package com.dcep.dips.wholesalepayment.manager.redo;

import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;

import java.util.HashMap;
import java.util.Map;

public interface RedoManager {
    /**
     * 报文对应的补偿处理逻辑
     */
    Map<String,RedoManager> rodo =  new HashMap<>();

    /**
     * 补偿重做
     * @param stsctrlDO
     * @param accountingInstrDO
     * @return 是否为终态，true为终态，false为非终态或未知
     */
    boolean redo(CommonStsctrlDO stsctrlDO, AccountingInstrDO accountingInstrDO);



    /**
     * 程序启动后才能调用，否则可能无法加载bean
     *
     * 1.待开发报文
     * CCP_REQUEST("dcep.131.001.01", "CCP业务发起报文"),
     * FMI_SSS_REQUEST("dcep.135.001.01", "FMI发起SSS业务申请报文"),
     * MER_SSS_REQUEST("dcep.137.001.01", "机构发起SSS业务申请报文"),
     * 2.通知报文,无需处理
     * SETTLE_NOTICE("dcep.200.001.01", "结算状态通知报文"),
     * CDT_RESPONSE("dcep.202.010.01", "付款业务应答报文"),
     * DBT_RESPONSE("dcep.212.010.01", "收款业务应答报文"),
     * RECOV_RESPONSE("dcep.222.010.01", "兑回业务应答报文"),
     * COV_RESPONSE("dcep.226.010.01","兑出业务应答报文"),
     * CDT_COV_RESPONSE("dcep.228.010.01","汇款兑出应答报文"),
     * REFUND_RESPONSE("dcep.282.010.01","消费退款业务应答报文"),
     * CRDT_ADJ_RESPONSE("dcep.802.010.01","差错贷记调账应答报文"),
     * FUND_ADJUST_NOTICE("dcep.185.001.01", "资金调整通知报文"),
     * COMMON_PROCESS_CONFIRM("dcep.900.001.01", "通用处理确认报文"),
     * MESSAGE_DISCARD_NOTICE("dcep.911.001.01", "报文丢弃通知报文"),
     * COMMON_PROCESS_COMCONF("dcep.902.001.01", "通信级确认报文"),
     * 3.转发报文,无需处理
     * ORDR_CONF_REQUEST("dcep.261.010.01", "商户订单确认申请报文"),
     * ORDR_CONF_RESULT_NOTICE("dcep.263.010.01", "消费付款结果通知报文"),
     * 4.啥子报文？
     * CDT_FUND_INCREASE("hvps.112.001.01", "大额汇兑来账请求"),
     * CDT_FUND_PRE_INCREASE("hvps.115.001.01", "大额延迟结算来账请求"),
     * CDT_FUND_DECREASE_OUT("hvps.112.001.01", "注资调减"),
     * CDT_PRE_FUND_DECREASE_OUT("hvps.118.001.01", "预注资调减"),
     * CDT_FUND_INCREASE_IN("hvps.112.001.01", "注资调增"),
     * CDT_PRE_FUND_INCREASE_IN("hvps.115.001.01", "预注资调增"),
     * 5.撤销报文无需处理
     * REVERSE_REQUREST("dcep.427.001.01", "业务撤销请求报文"),
     * REVERSE_RESPONSE("dcep.428.001.01", "业务撤销通知报文"),
     * 6.未使用
     * CDT_REQUEST_NMTBN("dcep.203.010.02", "付款业务请求报文（区块链）"),
     * DBT_REQUEST_NMTBN("dcep.213.010.02", "收款业务请求报文（区块链）"),
     *
     * @param msgTp
     * @return
     */
    static RedoManager get(String msgTp) {
        RedoManager redoManager =  rodo.get(msgTp);
        if(redoManager !=null){
            return redoManager;
        }
        return null;
    }
}
