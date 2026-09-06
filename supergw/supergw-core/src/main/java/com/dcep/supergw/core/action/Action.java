package com.dcep.supergw.core.action;

import com.dcep.common.exception.DcepException;
import com.dcep.supergw.core.ChannelContext;

public interface Action {
	
    void doInvoke(ChannelContext context);

    void doException(ChannelContext context, DcepException e);

    void doCallBack(ChannelContext context);
}
