package com.dcep.supergw.manager.channel;

import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcep.supergw.manager.channel.action.AsyncHttpsPostAction;
import com.dcep.supergw.manager.channel.action.AuthorizationAction;
import com.dcep.supergw.manager.channel.action.JurisdictionAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("AuthorizationChannel")
public class AuthorizationChannel extends AbstractChannel {

    private final static String PRIORITY_PREFIX = "${channel.poolsize.authorization:2}";

    public AuthorizationChannel(@Value(PRIORITY_PREFIX) int priority) {
        super("AuthorizationChannel", priority);
    }

    @Override
    public AbstractAction getChannel() {
        return ChannelBuilder.getInstance()
            //权限校验
            .addAction(new JurisdictionAction())
            //预授权模式
            .addAction(new AuthorizationAction())
            //异步POST，此处采用回调模式
            .addAction(new AsyncHttpsPostAction())
            .build();
    }
}
