package com.emop.wlt.user.management.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDTO {

    private String userId;

    private String mobileNumber;

    private String deviceId;

    private String deviceType;

    private String innerVersion;
    private String countryAndRegionCode;
}
