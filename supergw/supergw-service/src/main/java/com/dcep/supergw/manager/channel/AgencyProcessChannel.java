package com.dcep.supergw.manager.channel;

import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcep.supergw.manager.channel.action.HttpsPostAction;
import com.dcep.supergw.manager.channel.action.JurisdictionAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 代理模式 网关作为内部dubbo服务发布在nacos,内部系统调用网关dubbo服务,网关校验后转发给运营机构
 */
@Service("AgencyProcessChannel")
@BeansCondition(name = "beans.channel", havingValue = "AgencyProcessChannel")
public class AgencyProcessChannel extends AbstractChannel {

    private final static String PRIORITY_PREFIX = "${channel.poolsize.agencyProcess:1}";

    public AgencyProcessChannel(@Value(PRIORITY_PREFIX) int priority) {
        super("AgencyProcess", priority);
    }

    @Override
    public AbstractAction getChannel() {
        return ChannelBuilder.getInstance()
            //权限校验
            .addAction(new JurisdictionAction())
            //发送给运营机构，同步https
            .addAction(new HttpsPostAction())
            .build();
    }

}
