package com.emop.wlt.user.query.api;

import com.dubbo.ldc.ZoneRouter;

@ZoneRouter(value = ZoneRouter.Type.CZ)
public interface CidIndexQueryProvider {

    /**
     * 根据证件信息查询CID
     */
    String queryCidByIdInfo(String identityType, String identityNumber);
}
