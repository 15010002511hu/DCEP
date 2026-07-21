package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 请求银行账户短信验证码 请求报文
 */

@Data
@ToString(callSuper = true)
public class Mapp02700101Req extends FlowBaseReq {

    /**
     * 钱包ID
     */
    @Length(max = 16)
    private String walletId;

    /**
     * 账户号码
     */
    @Length(max = 32)
    @NotBlank
    private String accountNumber;

    /**
     * 账户银行机构代码
     */
    @Length(max = 14)
    @NotBlank
    private String bankCode;

    /**
     * 账户预留手机号
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;

    /**
     * 账户类型
     */
    @NotBlank
    private String accountType;
}
