package com.emop.wlt.user.management.provider;

import com.emop.wlt.flow.entity.FlowContext;
import com.emop.wlt.flow.manager.FlowContextManager;
import com.emop.wlt.user.management.api.FlowManagerProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
@DubboService
@Slf4j
public class FlowManagerProviderImpl implements FlowManagerProvider {
    @Autowired
    private FlowContextManager flowContextManager;
    @Override
    public String selectSuccessVerifySceneType(String contextNo) {
        FlowContext flowContext = flowContextManager.getForkCacheFlowContext(contextNo);

        return flowContext == null?null:flowContext.getVerifySceneType();
    }
}
