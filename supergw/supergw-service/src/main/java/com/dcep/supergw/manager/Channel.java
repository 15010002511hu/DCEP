package com.dcep.supergw.manager;

import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.manager.bo.ChannelContext;

public interface Channel {

    void invokeAction(ChannelContext context);

    void invokeException(ChannelContext context, GwException e);

    void invokeCallBack(ChannelContext context);

}
