package com.emop.wlt.user.management.model.response;

import com.emop.wlt.common.model.flow.FlowBaseResp;
import lombok.Data;
import lombok.ToString;

/**
 * 登录
 */

@Data
@ToString
public class Mapp10300101Resp extends FlowBaseResp {

    /**
     * 用户token
     */
    private String token;

    /**
     * token过期时间
     */
    private String expireSeconds;

    /**
     * app启动刷新token间隔 单位 秒
     */
    private String refreshIntervalSeconds;

    /**
     * userID
     */
    private String userId;
    /**
     * 账户手机号
     */
    private String mobileNumber;
}
