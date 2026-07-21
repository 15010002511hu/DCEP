package com.emop.wlt.user.management.model.response;

import com.emop.wlt.common.model.flow.FlowBaseResp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 登录校验
 */

@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class Mapp10200101Resp extends FlowBaseResp {

    /**
     * 是否设置密码标识
     * true:设置，false:未设置
     */
    private String setPasswordFlag;
}
