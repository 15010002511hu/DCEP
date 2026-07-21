package com.emop.wlt.user.query.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 查询问题详情 请求
 */
@Data
@ToString
public class Mapp20300101Req {

    @Length(max = 35)
    private String mobileNumber;

    /**
     * 问题编号
     * questionType=QT00时必传
     */
    @Length(max = 256)
    private String questionNo;

    /**
     * 问题类型
     * QT00:通用
     * QT01:硬钱包
     * QT02:非登录态常见问题
     */
    @NotBlank
    @Pattern(regexp = "QT00|QT01|QT02")
    private String questionType;

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
