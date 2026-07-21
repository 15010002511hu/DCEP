package com.emop.wlt.user.management.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Geolocation {

    /**
     * 经度
     */
    private String longitude;

    /**
     * 纬度
     */
    private String latitude;

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 城市编码
     */
    private String cityCode;

    /**
     * 区
     */
    private String adCode;
}
