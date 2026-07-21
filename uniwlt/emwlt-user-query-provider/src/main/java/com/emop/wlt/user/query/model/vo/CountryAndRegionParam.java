package com.emop.wlt.user.query.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CountryAndRegionParam {

    /**
     * 国家或地区区号
     * e.g. +853
     */
    private String code;

    /**
     * 国家或地区编码
     * e.g. MO
     */
    private String countryCode;

    /**
     * 国家或地区首字母
     */
    private String firstLetter;

    /**
     * 是否允许注册
     * 1：允许
     * 0：不允许
     */
    private String isAllowRegister;

    /**
     * 是否允许转账
     * 1：允许
     * 0：不允许
     */
    private String isAllowTransfer;

    /**
     * 国家或地区名称
     */
    private String name;

}
