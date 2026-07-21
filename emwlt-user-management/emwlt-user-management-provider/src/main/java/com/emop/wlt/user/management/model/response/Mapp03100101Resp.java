package com.emop.wlt.user.management.model.response;

import lombok.Builder;
import lombok.Data;

/**
 * cfca随机数和公钥响应类
 */
@Data
@Builder
public class Mapp03100101Resp {

    /**
     * 钱包后台服务端生成的随机数
     */
    private String serverRandom;

    /**
     * 钱包后台返回的登录密码公钥
     */
    private String pwdPublicKey;

}
