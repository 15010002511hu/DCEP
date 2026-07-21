package com.emop.wlt.user.query.api;

import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.user.query.dto.vo.UserVO;

/**
 * 用户信息查询服务
 *
 * @author bobo
 * @version 1.0.0
 * @date 2021/7/13
 * @dubbo
 */
@ZoneRouter(value = ZoneRouter.Type.CZ)
public interface UserProvider {

    /**
     * @param userId
     * @return
     */
    UserVO selectByPrimaryKey(String userId);

    /**
     * @param landAls
     * @return
     */
    UserVO selectByLandAls(String landAls);

    /**
     * 根据手机号查询用户id
     *
     * @param phone
     * @return
     */
    String selectUserIdByPhone(String phone);
}
