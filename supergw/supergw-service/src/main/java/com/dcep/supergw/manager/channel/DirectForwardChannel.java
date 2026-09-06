/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.manager.channel;

import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcep.supergw.manager.channel.action.AsyncHttpsPostAction;
import com.dcep.supergw.manager.channel.action.JurisdictionAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 直接转发模式 运营机构发来报文，网关校验后直接转发给下一流程运营机构
 * @author huyajun
 * @version $Id: DirectForward.java, v 0.1 2019年8月7日 下午8:37:47 Administrator Exp $
 */
@Service("DirectForwardChannel")
@BeansCondition(name = "beans.channel", havingValue = "DirectForwardChannel")
public class DirectForwardChannel extends AbstractChannel {

    private final static String PRIORITY_PREFIX = "${channel.poolsize.directForward:5}";

    public DirectForwardChannel(@Value(PRIORITY_PREFIX) int priority) {
        super("DirectForward", priority);
    }

    @Override
    public AbstractAction getChannel() {
        return ChannelBuilder.getInstance()
            //权限校验
            .addAction(new JurisdictionAction())
            //异步POST，此处采用回调模式
            .addAction(new AsyncHttpsPostAction())
            .build();
    }

}

