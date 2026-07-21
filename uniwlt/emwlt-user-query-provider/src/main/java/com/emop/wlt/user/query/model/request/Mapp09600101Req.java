package com.emop.wlt.user.query.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 手机号格式校验 请求
 */

@Data
@ToString
public class Mapp09600101Req {

    /**
     * 手机号（含区号）
     */
    @NotBlank
    @Length(max = 35)
    private String mobileNumber;

    /**
     * 业务场景
     */
    @NotBlank
    @Pattern(regexp = "BT00|BT01", message = "业务场景格式错误")
    private String bizType;
}
