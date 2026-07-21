package com.emop.wlt.user.query.provider;

import com.emop.wlt.user.query.api.WalletUserIndexQueryProvider;
import com.emop.wlt.user.query.service.WalletUserIndexQueryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liguangyao
 */
@DubboService
public class WalletUserIndexQueryProviderImpl implements WalletUserIndexQueryProvider {

    @Autowired
    private WalletUserIndexQueryService walletUserIndexQueryService;

    @Override
    public String queryUserIdByWalletId(String walletId) {
        return walletUserIndexQueryService.selectByWalletId(walletId);
    }

}
