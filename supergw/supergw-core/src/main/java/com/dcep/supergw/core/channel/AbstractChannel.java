package com.dcep.supergw.core.channel;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.supergw.core.ChannelContext;
import com.dcep.supergw.core.ChannelContext.Status;
import com.dcep.supergw.core.action.AbstractAction;
import java.util.Objects;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author : maxinyu
 * @version : AbstractChannel.java v 0.1 2020-05-11
 * @description :
 */

public abstract class AbstractChannel implements Channel {

    private static final Logger log = LoggerFactory.getLogger(AbstractChannel.class);

    /**
     * 线程池配置
     */
    protected final Executor pool;
    protected final String channelName;

    /**
     * Channel中注册的Action以双向链表的方式组织
     * head，tail作为channel的出口，可在Channel的子类中自定义IO操作。
     */
    private final AbstractAction head = new HeadAction();
    private final AbstractAction tail = new TailAction();

    public String getChannelName() {
        return this.channelName;
    }

    public AbstractAction getHead() {
        return head;
    }

    public AbstractAction getTail() {
        return tail;
    }

    public AbstractChannel(String name) {
        this(name, ThreadPoolUtils.getThreadPoolFixSize(Runtime.getRuntime().availableProcessors(), name));
        head.setNextAction(tail);
        tail.setPreAction(head);
    }

    public AbstractChannel(String name, Executor pool) {
        this.channelName = name;
        this.pool = pool;
        this.head.setNextAction(this.tail);
        this.tail.setPreAction(this.head);
    }

    /**
     * 在链表头（head后）添加节点
     */
    protected void addFirst(AbstractAction action) {
    	action.setPreAction(head);
    	action.setNextAction(head.getNextAction());
    	head.getNextAction().setPreAction(action);
    	head.setNextAction(action);
    }

    /**
     * 在链表尾（tail前）添加节点
     */
    protected void addLast(AbstractAction action) {
        tail.getPreAction().setNextAction(action);
        action.setNextAction(tail);
        action.setPreAction(tail.getPreAction());
        tail.setPreAction(action);
    }

    /**
     * 将Context中的Action向前推进，即找到下一个链表节点
     */
    protected static ChannelContext push(ChannelContext ctx) {
        ctx.setChannelThread(Thread.currentThread());
        ctx.setAction(ctx.getCurrentAction().getNextAction());
        return ctx;
    }

    /**
     * 将Context中的Action向后推进，即找到上一个链表节点
     */
    protected static ChannelContext pull(ChannelContext ctx) {
        ctx.setChannelThread(Thread.currentThread());
        ctx.setAction(ctx.getCurrentAction().getPreAction());
        return ctx;
    }

    /**
     * 判断当前Context执行的计算是否在Channel线程中，即该Context是否发生过线程切换
     */
    public boolean inChannel(ChannelContext ctx) {
        return ctx.getChannelThread() == Thread.currentThread();
    }
    
    /**
     * 判断是否唤醒当前Action操作
     */
    public boolean invokeAction(ChannelContext ctx) {
    	return Status.ACTIVE == ctx.getStatus() || this.head == ctx.getCurrentAction();
    }

    /**
     * 响应invokeAction事件，如果在Channel线程中，则同步执行操作；如果不在Channel线程中，则异步执行操作
     */
    @Override
    public void fireInvokeAction(ChannelContext ctx) {
        if (this.inChannel(ctx)) {
            this.doInvoke(push(ctx));
        } else {
            this.pool.execute(() -> {
                this.doInvoke(push(ctx));
            });
        }
    }

    /**
     * 执行具体Action中的doInvoke计算逻辑。如果异常则执行当前Action的doException逻辑
     */
    @Override
    public void doInvoke(ChannelContext ctx) {
        if (this.invokeAction(ctx)) {
            try {
                ctx.getCurrentAction().doInvoke(ctx);
            } catch (DcepException e) {
                this.doException(ctx, e);
            } catch (RuntimeException e) {
                this.doException(ctx, new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), e));
            }
        } else {
            this.fireInvokeAction(ctx);
        }
    }

    /**
     * 响应invokeException事件，如果在Channel线程中，则同步执行操作；如果不在Channel线程中，则异步执行操作
     */
    @Override
    public void fireInvokeException(ChannelContext ctx, DcepException e) {
        if (this.inChannel(ctx)) {
            this.doException(ctx, e);
        } else {
            this.pool.execute(() -> {
                this.doInvoke(ctx);
            });
        }
    }

    /**
     * 执行具体Action中的doException计算逻辑。如果异常则记录日志
     */
    @Override
    public void doException(ChannelContext ctx, DcepException e) {
        if (this.invokeAction(ctx)) {
            log.error(ctx.getCurrentAction().getActionName() + " error: {}", e);
            ctx.getCurrentAction().doException(ctx, e);
        } else {
            this.fireInvokeException(pull(ctx), e);
        }
    }

    /**
     * 响应invokeCallBack事件，如果在Channel线程中，则同步执行操作；如果不在Channel线程中，则异步执行操作
     */
    @Override
    public void fireInvokeCallBack(ChannelContext ctx) {
        if (this.inChannel(ctx)) {
            this.doCallBack(pull(ctx));
        } else {
            this.pool.execute(() -> {
                this.doCallBack(pull(ctx));
            });
        }
    }

    /**
     * 执行具体Action中的doCallBack计算逻辑。如果异常则执行当前Action的doException逻辑
     */
    @Override
    public void doCallBack(ChannelContext ctx) {
        if (this.invokeAction(ctx)) {
            try {
                ctx.getCurrentAction().doCallBack(ctx);
            } catch (DcepException e) {
                this.doException(ctx, e);
            } catch (RuntimeException e) {
                this.doException(ctx, new DcepException(ErrorEnum.UNKNOWN_EXCEPTION.getCode(), e));
            }
        } else {
            this.fireInvokeCallBack(ctx);
        }
    }

    /**
     * 由子类自定义Channel入口的IO操作
     */
    protected void invokeHeadAction(ChannelContext ctx) {
    	ctx.fireInvokeAction();
    }

    protected void invokeHeadException(ChannelContext ctx, DcepException e) {
        throw e;
    }

    protected void invokeHeadCallBack(ChannelContext ctx) {
    }

    /**
     * 可由子类自定义Channel出口的IO操作
     */
    protected void invokeTailAction(ChannelContext ctx) {
    }

    protected void invokeTailException(ChannelContext ctx, DcepException e) {
        throw e;
    }

    protected void invokeTailCallBack(ChannelContext ctx) {
    }

    final class HeadAction extends AbstractAction {

        public HeadAction() {
            super("HeadAction");
        }

        @Override
        public void doInvoke(ChannelContext ctx) {
            invokeHeadAction(ctx);
        }

        @Override
        public void doException(ChannelContext ctx, DcepException e) {
            invokeHeadException(ctx, e);
        }

        @Override
        public void doCallBack(ChannelContext ctx) {
            invokeHeadCallBack(ctx);
            ctx.close();
        }
    }

    final class TailAction extends AbstractAction {

        public TailAction() {
            super("TailAction");
        }

        @Override
        public void doInvoke(ChannelContext ctx) {
            invokeTailAction(ctx);
        }

        @Override
        public void doException(ChannelContext ctx, DcepException e) {
            invokeTailException(ctx, e);
        }

        @Override
        public void doCallBack(ChannelContext ctx) {
            invokeTailCallBack(ctx);
        }
    }


    /**
     * 重写equal方法
     *
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractChannel)) {
            return false;
        }
        AbstractChannel that = (AbstractChannel) o;
        return Objects.equals(channelName, that.channelName);
    }


    /**
     * 重写hashcode方法
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(channelName);
    }
}
