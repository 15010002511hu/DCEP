package com.emop.wlt.user.management.model.response;

import com.emop.wlt.common.model.flow.FlowBaseResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 验证银行账户短信验证码 应答报文
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class Mapp02800101Resp extends FlowBaseResp {

    private String signingNumber;

    private String bankCode;
}
