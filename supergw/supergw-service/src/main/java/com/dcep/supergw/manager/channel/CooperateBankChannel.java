package com.dcep.supergw.manager.channel;

import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcep.supergw.manager.channel.action.DirectClearingAction;
import com.dcep.supergw.manager.channel.action.JurisdictionAction;
import com.dcep.supergw.manager.channel.action.PostWithSigAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// 同业银行模式
@Service("CooperateBankChannel")
@BeansCondition(name = "beans.channel", havingValue = "CooperateBankChannel")
public class CooperateBankChannel extends AbstractChannel {

    public CooperateBankChannel(@Value("${channel.poolsize.cooperateBank:5}") int priority) {
        super("CooperateBankChannel", priority);
    }

    @Override
    public AbstractAction getChannel() {
        return ChannelBuilder.getInstance()
            // 报文权限校验
            .addAction(new JurisdictionAction())
            // 调用交易转接
            .addAction(new DirectClearingAction())
            // 调钱柜
            .addAction(new PostWithSigAction()) // 异步
            // 调用交易转接
            .addAction(new DirectClearingAction())
            // 调合作银行
            .addAction(new PostWithSigAction()) // 异步
            // 调用交易转接
            .addAction(new DirectClearingAction())
            .build();
    }

}
