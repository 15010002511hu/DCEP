package com.dcep.supergw.core.action;

import com.dcep.common.exception.DcepException;
import com.dcep.supergw.core.ChannelContext;
import com.dcep.supergw.core.constant.Contexts;

public abstract class StructAction<T> extends AbstractAction {
	
	public StructAction(String actionName) {
		super(actionName);
	}

    /**
     * 由子类自定义业务操作，默认让数据向前传递
     */
	@SuppressWarnings("unchecked")
	@Override
    final public void doInvoke(ChannelContext context) {
        this.doInvoke(context, (T) context.getAttachment(Contexts.DESERIALIZATION));
    }

    /**
     * 由子类自定义异常操作
     */
    @SuppressWarnings("unchecked")
	@Override
    final public void doException(ChannelContext context, DcepException e) {
    	this.doException(context, e, (T) context.getAttachment(Contexts.DESERIALIZATION));
    }

    /**
     * 由子类自定义异常操作，默认让数据向后传递
     */
    @SuppressWarnings("unchecked")
	@Override
    final public void doCallBack(ChannelContext context) {
        this.doCallBack(context, (T) context.getAttachment(Contexts.DESERIALIZATION));
    }

    /**
     * 由子类自定义业务操作，默认让数据向前传递
     */
    abstract public void doInvoke(ChannelContext context, T data);

    /**
     * 由子类自定义异常操作
     */
    abstract public void doException(ChannelContext context, DcepException e, T data);

    /**
     * 由子类自定义异常操作，默认让数据向后传递
     */
    abstract public void doCallBack(ChannelContext context, T data);

}
