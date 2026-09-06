package com.dcep.supergw.manager.bo;

import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.record.Timer;
import com.dcep.supergw.manager.channel.AbstractChannel;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.servlet.AsyncContext;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Setter
@Getter
@Slf4j
public class ChannelContext {

    private AsyncContext servletContext;
    private AbstractChannel channel;
    private Map<String, Object> attachment = new HashMap<>();
    //当前执行的Action
    private AbstractAction action;
    private boolean exceptionCatched = false;
    private boolean isRequest = true;
    private Timer timer = Timer.getTimer();
    //通道状态
    private AtomicReference<Status> status = new AtomicReference<>(Status.ACTIVE);

    public ChannelContext() {
    }

    public void setChannel(AbstractChannel channel) {
        this.channel = channel;
        //设置好channel中第一个action
        this.action = channel.getRoot();
    }

    public boolean ensureActive() {
        return this.status.get().name().equals(Status.ACTIVE.name());
    }

    public boolean setRequest(boolean isRequest) {
        this.isRequest = isRequest;
        return this.isRequest;
    }

    public void close() {
        if (channel != null) {
            log.info(channel.getChannelName() + " costed: {} ms", timer.cost());
        }
        if (this.status.compareAndSet(Status.ACTIVE, Status.INACTIVE)) {
            try {
                this.servletContext.complete();
            } catch (Exception e) {
                log.error("AsyncContext.complete error: {}", e);
            }
        }
    }

    public void fireInvokeChannel() {
        log();
        //发起channel调用
        channel.invokeChannel(this);
    }

    public void fireInvokeChannel(GwException e) {
        log();
        //发起channel Exception调用
        channel.invokeChannel(this, e);
    }

    //记录日志，并继续channel中下一个action的调用
    public void fireInvokeAction() {
        log();
        channel.invokeAction(this);
    }

    public void fireInvokeException(GwException e) {
        channel.invokeException(this, e);
    }

    public void fireInvokeCallBack() {
        log();
        channel.invokeCallBack(this);
    }

    private void log() {
        log.info(action.getActionName() + " costed: {} ms", timer.timeing());
        if (log.isDebugEnabled()) {
            try {
                log.debug(action.getActionName() + " params: {}", this.getAttachment(Constant.DESERIALIZATION));
            } catch (Throwable e) {
                log.error("JoinPoint error: {}", e);
            }
        }
    }

    public void setAttachment(String key, Object value) {
        this.attachment.put(key, value);
    }

    public Object getAttachment(String key) {
        return this.attachment.get(key);
    }

    /**
     * 异步线程之间标识该通道是否活动
     */
    public enum Status {
        //活动的
        ACTIVE,
        //不活动的
        INACTIVE
    }

}
