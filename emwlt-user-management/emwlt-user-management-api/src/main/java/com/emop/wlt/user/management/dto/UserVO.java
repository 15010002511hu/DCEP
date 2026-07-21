package com.emop.wlt.user.management.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserVO implements Serializable {

    private String userId;
    private String pwd;
    private String pwdSalt;
    private String phone;
    private String countryRegionCode;
    private String deviceId;
    private String deviceType;
    private String status;
    private Short pwdRetryTimes;
    private LocalDateTime lockDate;
    private String loginStatus;
    private LocalDateTime lastLoginTime;
    private String pushFlag;
    private String appInnerVersion;
}