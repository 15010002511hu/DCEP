package com.emop.wlt.user.management.api;

import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.user.management.dto.UserVO;


@ZoneRouter(value = ZoneRouter.Type.GZ)
public interface UserProvider {

    /**
     * @param userId
     * @return
     */
    UserVO selectByPrimaryKey(String userId);

    /***
     * 注销用户
     * @param phone
     * @param userId
     */
    void deactivateUser(String phone, String userId);

    /**
     * 挂失用户
     *
     * @param userId
     */
    void reportAsLost(String userId);

    /**
     * 解除挂失
     *
     * @param userId
     */
    void cancelLoss(String userId);

}
