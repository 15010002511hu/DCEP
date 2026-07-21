package com.emop.wlt.user.query.model.request;


import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 一码通二维码查询
 *
 * @author hanyabo
 * @date 2022/12/26 10:36 AM
 */
@Data
@ToString
public class Mapp09500101Req {

    /**
     * 用户扫码时选择的钱包
     */
    @NotBlank
    @Length(max = 34)
    private String walletId;

    /**
     * 二维码信息 url格式
     */
    @NotBlank
    @Length(max = 500)
    private String qrCode;

    /**
     * 钱包手机号
     */
    @Length(max = 35)
    private String mobileNumber;

    /**
     * 由数字澳门元App前端生成确保唯一
     */
    @NotBlank
    @Length(max = 128)
    private String userUniqueId;

}
