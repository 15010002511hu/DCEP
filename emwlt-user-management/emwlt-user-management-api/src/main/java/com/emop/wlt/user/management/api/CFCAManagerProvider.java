package com.emop.wlt.user.management.api;

import com.dubbo.ldc.ZoneRouter;

/**
 * @author bobo
 * @Description: cfca内部操作接口
 * @date 2022/3/29
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface CFCAManagerProvider {

    /**
     * 解密cfca密码
     */
    String decryptPassWord(String pwdEnc);
}
