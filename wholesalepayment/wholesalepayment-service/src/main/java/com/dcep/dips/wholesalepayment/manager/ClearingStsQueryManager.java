/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager;

import com.dcep.dips.common.dto.dc411.Dcep41100101DTO;
import com.dcep.dips.common.dto.dc412.Dcep41200101DTO;

public interface ClearingStsQueryManager {

    /**
     * 机构向中心发送的411报文，查询中心清算状态，中心向机构返回412报文
     *
     * @param req
     * @return
     */
    Dcep41200101DTO queryClearingStsFromOrg(Dcep41100101DTO req);
}
