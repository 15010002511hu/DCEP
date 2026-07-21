package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 登录校验
 */

@Data
@ToString(callSuper = true)
public class Mapp10200101Req extends FlowBaseReq {

    /**
     * 手机号（带区号）
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;

    /**
     * 生物指纹开通标识
     */
    @NotBlank
    @Pattern(regexp = "true|false")
    private String ifaaOpenFlag;

}
