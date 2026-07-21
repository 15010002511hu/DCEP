package com.emop.wlt.user.query.api;

import com.dubbo.ldc.ZoneRouter;

/**
 * wallet-user索引查询
 *
 * @author liguangyao
 * @version 1.0.0
 * @date 2021/7/20
 * @dubbo
 */
@ZoneRouter(ZoneRouter.Type.CZ)
public interface WalletUserIndexQueryProvider {

    /**
     * 根据walletId查userId
     *
     * @param walletId
     * @return
     */
    String queryUserIdByWalletId(String walletId);

}
