package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 验证支付密码 请求报文
 */
@Data
@ToString(callSuper = true)
public class Mapp02900101Req extends FlowBaseReq {

    /**
     * 钱包ID
     */
    @NotBlank
    @Length(max = 16)
    private String walletId;

    /**
     * 支付密码密文
     */
    @NotBlank
    @Length(max = 512)
    private String pwdEnc;
}
