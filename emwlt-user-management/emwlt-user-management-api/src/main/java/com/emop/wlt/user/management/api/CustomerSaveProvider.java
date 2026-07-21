package com.emop.wlt.user.management.api;

import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.user.management.dto.CustomerSaveDTO;

@ZoneRouter(value = ZoneRouter.Type.GZ)
public interface CustomerSaveProvider {

    /**
     * 保存实名信息
     */
    String saveCustomer(CustomerSaveDTO in);
}
