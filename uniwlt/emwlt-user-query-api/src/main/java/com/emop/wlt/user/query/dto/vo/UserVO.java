package com.emop.wlt.user.query.dto.vo;

import java.io.Serializable;
import java.util.Date;
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
    private String email;
    private String deviceId;
    private String deviceType;
    private String status;
    private Short pwdRetryTimes;
    private Date lockDate;
    private String loginStatus;
    private Date lastLoginTime;
    private String pushFlag;
    private String assistPhone;
    private String assistEmail;
    private String assistEmailStatus;
}