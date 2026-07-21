package com.emop.wlt.user.management.model;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String mobileNumber;

    private String deviceType;

    private String deviceId;

    private String deviceName;
}
