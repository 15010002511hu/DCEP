package com.emop.wlt.user.management.api;

import com.dubbo.ldc.ZoneRouter;
import com.emop.wlt.user.pojo.SystemMessage;

@ZoneRouter(ZoneRouter.Type.GZ)
public interface SystemMessageManageProvider {

    boolean save(SystemMessage systemMessage);
}
