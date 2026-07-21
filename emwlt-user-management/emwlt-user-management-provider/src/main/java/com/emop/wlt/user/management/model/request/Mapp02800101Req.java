package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 验证银行账户短信验证码 请求报文
 */

@Data
@ToString(callSuper = true)
public class Mapp02800101Req extends FlowBaseReq {

    /**
     * 验证码
     */
    @Length(max = 6)
    @NotBlank
    private String verifyCode;

    /**
     * 钱包ID
     */
    @Length(max = 16)
    private String walletId;

}
