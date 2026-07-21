package com.emop.wlt.user.management.api;

import com.dubbo.ldc.ZoneRouter;

@ZoneRouter(value = ZoneRouter.Type.GZ)
public interface WalletUserIndexManagementProvider {

    /**
     * 插入钱包用户索引
     *
     * @param userId
     * @param walletId
     * @return
     */
    int insert(String walletId, String userId);

    /**
     * 删除钱包用户索引
     *
     * @param userId
     * @param walletId
     * @return
     */
    int remove(String walletId, String userId);
}
