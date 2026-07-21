package com.emop.wlt.user.management.model;

import lombok.Builder;
import lombok.Data;

/**
 * 用户与钱包合并信息
 */
@Data
@Builder
public class UserWalletInfoDTO {
    private String userId;
    private String walletId;

    private String walletLevel;

    private String walletType;

    private String walletStatus;
    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 证件类型
     */
    private String idType;
    /**
     * 钱包总体状态
     * 0：无钱包
     * 1：有钱包
     * 2：有实名钱包
     */
    private String walletTotalStatus;
}
