package com.dcep.supergw.manager.channel;

import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcep.supergw.manager.channel.action.AsyncHttpsPostAction;
import com.dcep.supergw.manager.channel.action.ClearingServiceAction;
import com.dcep.supergw.manager.channel.action.JurisdictionAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


//清算模式，跨机构场景，运营机构发来账务交易报文，校验后调用交易转接服务，然后发送给运营机构
@Service("ClearingChannel")
@BeansCondition(name = "beans.channel", havingValue = "ClearingChannel")
public class ClearingChannel extends AbstractChannel {

    private final static String PRIORITY_PREFIX = "${channel.poolsize.clearing:10}";

    public ClearingChannel(@Value(PRIORITY_PREFIX) int priority) {
        super("Clearing", priority);
    }

    @Override
    public AbstractAction getChannel() {
        return ChannelBuilder.getInstance()
            //权限校验
            .addAction(new JurisdictionAction())
            //预清算
            .addAction(new ClearingServiceAction())
            //异步POST，此处采用回调模式
            .addAction(new AsyncHttpsPostAction())
            .build();
    }
}

