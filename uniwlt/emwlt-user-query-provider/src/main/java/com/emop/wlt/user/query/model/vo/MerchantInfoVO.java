package com.emop.wlt.user.query.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class MerchantInfoVO {

    /**
     * 商户名称（全称）
     */
    private String merchantName;

    /**
     * 商户简称
     */
    private String merchantShortName;

    /**
     * 商户号
     */
    private String merchantNumber;

    /**
     * 收款运营机构号
     */
    private String payeeInstitutionCode;

    /**
     * 商户服务机构号
     * 如果是运营机构的直连商户，传运营机构编号；如果是商户通过受理机构接入，传受理机构编号
     * 用于区分商户是直连还是间联
     */
    private String merchantInstitutionCode;

    /**
     * 商户类别代码
     */
    private String merchantCategoryCode;

}
