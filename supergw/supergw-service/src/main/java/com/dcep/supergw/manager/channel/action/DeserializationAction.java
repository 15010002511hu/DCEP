package com.dcep.supergw.manager.channel.action;

import com.dcep.supergw.common.utils.SerializationUtil;
import com.dcep.supergw.core.ChannelContext;
import com.dcep.supergw.core.action.AbstractAction;
import com.dcep.supergw.core.constant.Contexts;

/**
 * 反序列化流程 1、正常为反序列化 2、反向为序列化 3、需自定义序列化工具，并传入Action中，后续改为Java SPI方式获取
 */
public class DeserializationAction<T> extends AbstractAction {

    //SPI
    private final SerializationUtil<T> util;

    public DeserializationAction(SerializationUtil<T> util) {
        super("DeserializationAction");
        this.util = util;
    }

    @Override
    public void doInvoke(ChannelContext ctx) {
        ctx.setAttachment(Contexts.DESERIALIZATION,
            util.deserialization((String) ctx.getAttachment(Contexts.SERIALIZATION),
                (Class<?>) ctx.getAttachment(Contexts.DTO_CLASS)));
        ctx.fireInvokeAction();


    }
}
