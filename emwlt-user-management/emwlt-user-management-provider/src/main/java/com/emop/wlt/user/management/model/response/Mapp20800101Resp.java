package com.emop.wlt.user.management.model.response;

import lombok.Data;
import lombok.ToString;

/**
 * 用户信息查询（非登录）
 */
@Data
@ToString
public class Mapp20800101Resp {

    /**
     * 是否设置密码标识
     * true:设置，false:未设置
     */
    private Boolean setPasswordFlag;
}
