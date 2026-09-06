package com.dcep.supergw.core.channel;

import com.dcep.common.exception.DcepException;
import com.dcep.supergw.core.ChannelContext;
import com.dcep.supergw.core.action.Action;

public interface Channel extends Action {
	
    void fireInvokeAction(ChannelContext context);

    void fireInvokeException(ChannelContext context, DcepException e);

    void fireInvokeCallBack(ChannelContext context);
}
