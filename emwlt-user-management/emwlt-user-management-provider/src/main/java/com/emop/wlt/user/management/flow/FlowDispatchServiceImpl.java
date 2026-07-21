package com.emop.wlt.user.management.flow;

import com.emop.wlt.flow.api.FlowGZDispatchService;
import com.emop.wlt.flow.dispatch.AbstractFlowDispatchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author shelphi
 * @description: 流程节点本地路由服务
 * @date 2024年03月14日
 */
@DubboService(group = "user-management")
@Slf4j
public class FlowDispatchServiceImpl extends AbstractFlowDispatchService implements FlowGZDispatchService {
}
