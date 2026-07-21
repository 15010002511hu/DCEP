package com.emop.wlt.user.query.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO implements Serializable {

    private String userId;
    private String pwd;
    private String pwdSalt;
    private String phone;
    private String email;
    private String deviceId;
    private String deviceType;
    private String status;
    private Short pwdRetryTimes;
    private LocalDateTime lockDate;
    private String loginStatus;
    private LocalDateTime lastLoginTime;
    private String pushFlag;
    private String countryRegionCode;
}