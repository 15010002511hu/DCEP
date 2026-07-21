package com.emop.wlt.user.management.model.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 钱包基础信息
 */
@Data
@Builder
public class WalletBasicInfoVO {

    /**
     * 钱包id
     */
    private String walletId;

    /**
     * 钱包余额
     */
    private String walletBalance;

    /**
     * 钱包等级
     */
    private String walletLevel;

    /**
     * 钱包类型
     */
    private String walletType;

    /**
     * 钱包状态
     */
    private String walletStatus;

    /**
     * 钱包国家地区代码
     */
    private String countryAndRegionCode;

    /**
     * 钱包手机号码
     */
    private String mobileNumber;

    /**
     * 钱包所属运营机构编号
     */
    private String walletInstitutionCode;

    /**
     * 钱包名称
     */
    private String walletName;

    /**
     * 钱包开立时间
     */
    private LocalDateTime walletOpeningTime;

}
