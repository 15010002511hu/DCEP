/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager;


import com.dcep.dips.common.dto.dc412.Dcep41200101DTO;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;

public interface RecordManager {

    RecordDTO resume(String msgTp, String msgId);

    Dcep41200101DTO resumeQryRsp(String msgId);
    
}
