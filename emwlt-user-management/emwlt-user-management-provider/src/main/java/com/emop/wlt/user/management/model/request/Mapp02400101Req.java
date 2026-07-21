package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 验证钱包短信验证码 请求报文
 */

@Data
@ToString(callSuper = true)
public class Mapp02400101Req extends FlowBaseReq {

    /**
     * 验证码
     */
    @NotBlank
    @Length(max = 6)
    private String verifyCode;

    /**
     * 手机号
     */
    @Length(max = 35)
    private String mobileNumber;

    /**
     * 钱包ID
     */
    @Length(max = 16)
    private String walletId;
}
