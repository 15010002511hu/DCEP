package com.emop.wlt.user.query.service;

import com.emop.wlt.user.entity.CidIndex;

public interface CidIndexManagementService {

    /**
     * 根据证件信息查询CID
     */
    CidIndex getCidByIdentityInfo(String identityType, String identityNumber);
}
