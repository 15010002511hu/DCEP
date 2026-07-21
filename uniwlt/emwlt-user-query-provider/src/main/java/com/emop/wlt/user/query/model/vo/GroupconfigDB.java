package com.emop.wlt.user.query.model.vo;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author caojinman
 * @description UserConfigData
 * @date 2020-09-23
 */
@Data
@ToString
@Builder
public class GroupconfigDB {

    //用户组ID
    private String groupId;
    //功能组键值
    private String moduleKey;
    //功能键值
    private String functionKey;
    //功能配置
    private String config;
    //用户组版本
    private String version;
    //用户组更新时间
    private LocalDateTime updateTime;
    //硬钱包-机构支持的设备型号
    private String offlineWallet;
    //硬钱包-是否支持设备型号开关，若为0则offlineWallet无意义，若为1再判断offlineWallet
    private String offlineWalletSwitch;
}
