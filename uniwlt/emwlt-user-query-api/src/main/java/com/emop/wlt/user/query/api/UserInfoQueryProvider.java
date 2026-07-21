package com.emop.wlt.user.query.api;

import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.user.query.dto.UserInfoDTO;

/**
 * 用户信息查询服务
 */
@ZoneRouter(value = ZoneRouter.Type.CZ)
public interface UserInfoQueryProvider {

    /**
     * 根据用户id查询用户信息
     */
    UserInfoDTO selectByUserId(String userId);
}
