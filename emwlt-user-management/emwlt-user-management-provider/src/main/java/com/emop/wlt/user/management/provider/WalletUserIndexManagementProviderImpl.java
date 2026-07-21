package com.emop.wlt.user.management.provider;

import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.entity.WalletUserIndex;
import com.emop.wlt.user.management.api.WalletUserIndexManagementProvider;
import com.emop.wlt.user.management.constant.DatabaseConstant;
import com.emop.wlt.user.service.impl.WalletUserIndexDBService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import java.time.LocalDateTime;

@DubboService
@AllArgsConstructor
@Slf4j
public class WalletUserIndexManagementProviderImpl implements WalletUserIndexManagementProvider {

    private final WalletUserIndexDBService walletUserIndexDBService;


    @Override
    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public int insert(String walletId, String userId) {
        log.info("存wid-uid索引,入参:{}-{}", walletId, userId);
        WalletUserIndex walletUserIndex = new WalletUserIndex();
        walletUserIndex.setUserId(userId);
        walletUserIndex.setWalletId(walletId);
        walletUserIndex.setCreateDatetime(LocalDateTime.now());
        walletUserIndex.setUpdateDatetime(LocalDateTime.now());
        return walletUserIndexDBService.insert(walletUserIndex);
    }

    @Override
    @DDS(value = DatabaseConstant.USER_MANAGEMENT_DB_KEY, rule = SingleRule.class)
    public int remove(String walletId, String userId) {
        return walletUserIndexDBService.removeByWalletIdAndUserId(walletId, userId);
    }
}
