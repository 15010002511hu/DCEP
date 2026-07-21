package com.emop.wlt.user.query.service.impl;

import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.wlt.user.entity.WalletUserIndex;
import com.emop.wlt.user.query.constant.DatabaseConstant;
import com.emop.wlt.user.query.service.WalletUserIndexQueryService;
import com.emop.wlt.user.service.impl.WalletUserIndexDBService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class WalletUserIndexQueryServiceImpl implements WalletUserIndexQueryService {

    @Autowired
    WalletUserIndexDBService walletUserIndexDBService;

    @Override
    @DDS(value = DatabaseConstant.USER_QUERY_DATABASE, rule = SingleRule.class)
    public String selectByWalletId(String walletId) {
        String userId = StringUtils.EMPTY;
        WalletUserIndex walletUserIndex = walletUserIndexDBService.selectById(walletId);
        if (Objects.nonNull(walletUserIndex)) {
            userId = walletUserIndex.getUserId();
        }
        return userId;
    }

}
