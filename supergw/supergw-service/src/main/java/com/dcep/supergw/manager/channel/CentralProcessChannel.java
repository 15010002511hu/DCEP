package com.dcep.supergw.manager.channel;

import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcep.supergw.manager.channel.action.InvokeServiceAction;
import com.dcep.supergw.manager.channel.action.JurisdictionAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author nieyanping
 * @version CentralProcessImpl.java, v 0.1, 2019/8/22 18:25
 * @description A -> C 模式，运营机构发来报文，网关校验后调用内部服务dobbo服务。
 */
@Service("CentralProcessChannel")
@BeansCondition(name = "beans.channel", havingValue = "CentralProcessChannel")
public class CentralProcessChannel extends AbstractChannel {

    private final static String PRIORITY_PREFIX = "${channel.poolsize.centralProcess:5}";

    public CentralProcessChannel(@Value(PRIORITY_PREFIX) int priority) {
        super("CentralProcess", priority);
    }

    @Override
    public AbstractAction getChannel() {
        return ChannelBuilder.getInstance()
            //权限校验
            .addAction(new JurisdictionAction())
            //调用Dubbo服务
            .addAction(new InvokeServiceAction())
            .build();
    }
}