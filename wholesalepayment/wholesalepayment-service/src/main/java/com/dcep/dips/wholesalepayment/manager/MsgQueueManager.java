package com.dcep.dips.wholesalepayment.manager;

import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;

public interface MsgQueueManager {
    /**
     * 终态将交易推送至kafk
     */
    void sendMsg();
}
