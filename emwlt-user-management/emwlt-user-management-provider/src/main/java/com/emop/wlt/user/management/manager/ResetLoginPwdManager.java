package com.emop.wlt.user.management.manager;

import com.emop.wlt.common.constant.WalletConstant;
import com.emop.wlt.common.enums.WalletLevelEnum;
import com.emop.wlt.common.enums.WalletStatusEnum;
import com.emop.wlt.user.management.model.UserWalletInfoDTO;
import com.emop.wlt.user.wallet.api.UserWalletQueryProvider;
import com.emop.wlt.user.wallet.dto.CustomerIdInfoDTO;
import com.emop.wlt.user.wallet.dto.WalletInfoDTO;
import com.google.common.collect.Lists;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
public class ResetLoginPwdManager {
    @DubboReference
    private UserWalletQueryProvider userWalletQueryProvider;

    /**
     * 获取用户最高等级钱包信息
     */
    public UserWalletInfoDTO getUserHighestWalletInfo(String userId) {

        List<WalletInfoDTO> walletInfoDTOList = userWalletQueryProvider.queryUserWalletFromInst(userId);
        if (CollectionUtils.isEmpty(walletInfoDTOList)) {
            return UserWalletInfoDTO.builder().walletTotalStatus(WalletConstant.NO_WALLET).build();
        }
        List<UserWalletInfoDTO> userWalletInfoDTOList = Lists.newArrayList();
        walletInfoDTOList.stream()
                .filter(walletInfoDTO -> !WalletStatusEnum.LOGOUT.getCode().equals(walletInfoDTO.getWalletBasicInfo().getWalletStatus()))
                .forEach(walletInfoDTO -> userWalletInfoDTOList.add(UserWalletInfoDTO.builder()
                    .walletId(walletInfoDTO.getWalletBasicInfo().getWalletId())
                    .userId(userId)
                    .walletLevel(walletInfoDTO.getWalletBasicInfo().getWalletLevel())
                    .walletType(walletInfoDTO.getWalletBasicInfo().getWalletType())
                    .idType(Optional.ofNullable(walletInfoDTO.getCustomerIdInfo()).map(CustomerIdInfoDTO::getIdType).orElse(null))
                    .idNumber(Optional.ofNullable(walletInfoDTO.getCustomerIdInfo()).map(CustomerIdInfoDTO::getIdNumber).orElse(null))
                    .build()));
        //按等级排一下序
        userWalletInfoDTOList.sort(Comparator.comparing(UserWalletInfoDTO::getWalletLevel));
        UserWalletInfoDTO userWalletInfoDTO = userWalletInfoDTOList.get(0);
        if (userWalletInfoDTO.getWalletLevel().equals(WalletLevelEnum.HIGH_LEVEL.getCode()) ||
                userWalletInfoDTO.getWalletLevel().equals(WalletLevelEnum.MIDDLE_LEVEL.getCode())) {
            userWalletInfoDTO.setWalletTotalStatus(WalletConstant.REAL_NAME_WALLET);
            return userWalletInfoDTO;
        }
        userWalletInfoDTO.setWalletTotalStatus(WalletConstant.NO_REAL_NAME_WALLET);
        return userWalletInfoDTO;
    }
}
