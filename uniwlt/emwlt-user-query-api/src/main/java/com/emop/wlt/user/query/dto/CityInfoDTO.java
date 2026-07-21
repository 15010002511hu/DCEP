package com.emop.wlt.user.query.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author liguangyao
 */
@Data
public class CityInfoDTO implements Serializable {

    /**
     * GPS 坐标
     */
    String gpsCoordinate;

    /**
     * 城市文本信息
     */
    String cityName;

    /**
     * cityCode 城市Code
     */
    String cityCode;
}
