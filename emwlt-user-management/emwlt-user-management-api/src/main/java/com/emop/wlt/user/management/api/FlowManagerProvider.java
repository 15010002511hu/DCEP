package com.emop.wlt.user.management.api;

import com.dubbo.ldc.ZoneRouter;

/**
 * 流程相关服务
 */

@ZoneRouter(ZoneRouter.Type.GZ)
public interface FlowManagerProvider {
    /**
     * 查询contextNo,已验证成功的场景
     */
    String selectSuccessVerifySceneType(String contextNo);
}
