package com.emop.wlt.user.query.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 查询帮助分类列表 请求
 */
@Data
@ToString
public class Mapp20200101Req {

    /**
     * 手机号
     */
    @Length(max = 35)
    private String mobileNumber;

    /**
     * App版本
     */
    @NotBlank
    @Length(max = 32)
    private String appVersion;

    /**
     * 语言
     */
    @NotBlank
    @Pattern(regexp = "zh-CN|zh-HK|en-US|pt-PT")
    private String language;

    /**
     * 系统
     */
    @NotBlank
    @Pattern(regexp = "iOS|android")
    private String platform;
}
