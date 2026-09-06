package com.dcep.supergw.core;

import com.dcep.common.exception.DcepException;
import com.dcep.supergw.core.action.AbstractAction;
import com.dcep.supergw.core.channel.AbstractChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : maxinyu
 * @version : ChannelContext.java v 0.1 2020-05-11
 * @description :
 */
public class ChannelContext implements Context {


    /**
     * ChannelContext的本质就是存放各种数据的容器，同时它也是一个事件接口
     * attachment是容器，Context接口定义了事件接口
     */
    private final static int DEFAULT_LENGTH = 10;
    private Map<String, Object> attachment = new HashMap<>(DEFAULT_LENGTH);

    //通道现场即dto + action
    private AbstractChannel channel;
    private AbstractAction action;
    private volatile Thread channelThread;
    private volatile Status status = Status.ACTIVE;
    
    public Status getStatus() {
    	return this.status;
    }
    
    public ChannelContext setStatus(Status status) {
    	this.status = status;
    	return this;
    }

    public void setAttachment(String key, Object value) {
        this.attachment.put(key, value);
    }

    public Object getAttachment(String key) {
        return this.attachment.get(key);
    }

    public void close() {
        this.attachment.clear();
        this.status = Status.INACTIVE;
    }

    /**
     *  产生invokeAction事件，并让channel处理事件信息
     */
    @Override
    public void fireInvokeAction() {
        channel.fireInvokeAction(this);
    }

    /**
     *  产生invokeException事件，并让channel处理事件信息
     */
    @Override
    public void fireInvokeException(DcepException e) {
        channel.fireInvokeException(this, e);
    }

    /**
     *  产生invokeCallBack事件，并让channel处理事件信息
     */
    @Override
    public void fireInvokeCallBack() {
        channel.fireInvokeCallBack(this);
    }

    public Thread getChannelThread() {
        return this.channelThread;
    }

    public AbstractChannel getChannel() {
        return this.channel;
    }

    public AbstractAction getCurrentAction() {
        return this.action;
    }

    public void setChannelThread(Thread thread) {
        this.channelThread = thread;
    }

    public void setChannel(AbstractChannel channel) {
        this.channel = channel;
        this.action = channel.getHead();
    }

    public void setAction(AbstractAction action) {
        this.action = action;
    }

    /**
     * 异步线程之间标识该通道是否活动
     */
    public enum Status {
    	//活动的
        ACTIVE,
        //不活动的
        INACTIVE,
        //异常
        EXCEPTION
    }
}
