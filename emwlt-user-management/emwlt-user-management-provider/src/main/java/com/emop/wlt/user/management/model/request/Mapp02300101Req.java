package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 请求钱包短信验证码 请求报文（非登录态）
 */

@Data
@ToString(callSuper = true)
public class Mapp02300101Req extends FlowBaseReq {

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
