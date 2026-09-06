package com.dcep.supergw.common.exception;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.manager.bo.ChannelContext;


public class ExceptionUtil {

    public static void handleException(ChannelContext context, BlockException ex) {
        throw new GwException(GwErrorEnum.LIMITING_ERROR, ex);
    }
}
