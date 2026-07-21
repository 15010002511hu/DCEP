package com.emop.wlt.user.management.api;

import com.dubbo.ldc.ZoneRouter;

@ZoneRouter(ZoneRouter.Type.GZ)
public interface UserPasswordProvider {

    void checkUserPwd(String userId, String userPwd);

    void updateUserPwd(String userId, String userPwd);

    void decryptAndUpdateUserPwd(String userId, String encUserPwd);

}
