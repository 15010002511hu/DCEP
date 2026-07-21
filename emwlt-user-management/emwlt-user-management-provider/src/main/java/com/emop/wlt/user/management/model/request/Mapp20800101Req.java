package com.emop.wlt.user.management.model.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 用户信息查询（非登录态） 请求
 */
@Data
@ToString
public class Mapp20800101Req {

    /**
     * 手机号（带区号）
     */
    @NotBlank
    @Length(max = 35)
    private String mobileNumber;
}
