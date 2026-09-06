/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dal.model.CommonStsctrlDO;

/**
 * 计时控制组件
 * @author hx.zhaolei
 */
public interface CommonStsctrlManager {

    /**
     * 日切时，对结算排队业务调用结算钱包服务，执行退回处理
     * @param currentSystemDate 系统工作日
     * @return
     */
    Response<String>  endReturn(String currentSystemDate) throws DcepException;

    /**
     * 用于对非终态交易进行计时补偿处理--全部
     */
    void processAll();

    /**
     * 对非终态交易且处于锁定状态的交易，超过一定时间后进行处理
     */
    void processLockedAll();

    /**
     * 用于对非终态交易进行计时补偿处理--单个
     * @param commonStsctrlDO
     * @return
     */
    void process(CommonStsctrlDO commonStsctrlDO);

}
