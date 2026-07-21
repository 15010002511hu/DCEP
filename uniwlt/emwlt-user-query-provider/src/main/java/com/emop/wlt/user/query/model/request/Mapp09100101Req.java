package com.emop.wlt.user.query.model.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 公管查询-多语言参数和协议参数 请求
 */

@Data
@ToString
public class Mapp09100101Req {

    /**
     * 手机号（带区号）
     */
    @Length(max = 35)
    private String mobileNumber;

    /**
     * 语言
     */
    @NotBlank
    @Length(max = 32)
    private String language;

    /**
     * App版本
     */
    @NotBlank
    @Length(max = 32)
    private String appVersion;

    /**
     * element版本号
     */
    @Length(max = 32)
    private String elementReleaseId;

    /**
     * 协议版本号
     */
    @Length(max = 32)
    private String protocolReleaseId;

    /**
     * 隐私协议号
     */
    @NotBlank
    @Length(max = 32)
    private String appPrivacyProtocolNo;
}
