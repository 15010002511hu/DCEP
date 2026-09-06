/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.manager;

import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.manager.bo.ChannelContext;

/**
 *
 * @author huyajun
 * @version $Id: GwChannel.java, v 0.1 2019年8月9日 上午10:03:27 Administrator Exp $
 */
public interface Action {

    void doInvoke(ChannelContext context);

    void doException(ChannelContext context, GwException e);

    void doCallBack(ChannelContext context);

}
