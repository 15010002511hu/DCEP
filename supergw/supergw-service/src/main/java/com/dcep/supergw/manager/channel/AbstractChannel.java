package com.dcep.supergw.manager.channel;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.ThreadPoolUtils;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.manager.Channel;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcepex.trace.support.async.TraceExecutorService;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

/**
 * 通道：由具体的实例实现动作的编排，将线程池和执行过程统一
 */
@Getter
@Slf4j
public abstract class AbstractChannel implements Channel {

    private final int PRIORITY;
    private final int POOL_SIZE = Constant.CPU_NO * Constant.THREADS_PER_CPU;
    private final ExecutorService pool;
    private final String channelName;
    private final AbstractAction root;
    //Channel头节点
    private final AbstractAction headAction;
    //Channel尾节点
    private final AbstractAction tailAction;

    public AbstractChannel(String name) {
        this.PRIORITY = 1;
        this.channelName = name;
        // 配合全链路监控修改，修改为全链路监控定义的线程池
        this.pool = new TraceExecutorService(ThreadPoolUtils.getThreadPoolFixSize(POOL_SIZE * PRIORITY, channelName));
        //建立action调用链.head节点和tail节点主要为了调起docallback链
        this.headAction = new HeadAction();
        this.tailAction = new TailAction();
        //构造业务逻辑action节点
        this.root = initChannel();
    }

    public AbstractChannel(String name, int priority) {
        this.PRIORITY = priority;
        this.channelName = name;
//        this.pool = ThreadPoolUtils.getThreadPoolFixSize(POOL_SIZE * PRIORITY, channelName);
        // 配合全链路监控修改，修改为全链路监控定义的线程池
        this.pool = new TraceExecutorService(ThreadPoolUtils.getThreadPoolFixSize(POOL_SIZE * PRIORITY, channelName));
        this.headAction = new HeadAction();
        this.tailAction = new TailAction();
        this.root = initChannel();
    }

    public AbstractAction getNextAction(ChannelContext context) {
        AbstractAction next = context.getAction().getNextAction();
        context.setAction(next);
        return next;
    }

    protected AbstractAction initChannel() {
        return ChannelBuilder.getInstance()
            .addAction(this.headAction)
            .addAction(getChannel())
            .addAction(this.tailAction)
            .build();
    }

    public AbstractAction getPreAction(ChannelContext context) {
        AbstractAction pre = context.getAction().getPreAction();
        context.setAction(pre);
        return pre;
    }

    //响应context中发起的channel调用
    public void invokeChannel(ChannelContext context) {
        this.pool.execute(() -> {
            LoggerUtils.mdc((SoapHeader) context.getAttachment(Constant.SOAP_HEADER));
            //根据channel上action调用链顺序依次调用action
            invokeAction(context);
            MDC.clear();
        });
    }

    //响应context发生异常时的exception调用
    public void invokeChannel(ChannelContext context, GwException e) {
        this.pool.execute(() -> {
            LoggerUtils.mdc((SoapHeader) context.getAttachment(Constant.SOAP_HEADER));
            invokeException(context, e);
            MDC.clear();
        });
    }

    @Override
    public void invokeAction(ChannelContext context) {
        //获取action调用链中下一个action
        final AbstractAction action = getNextAction(context);
        try {
            if (context.ensureActive()) {
                //具体的action逻辑的调用
                action.doInvoke(context);
            }
        } catch (GwException e) {
            context.fireInvokeException(e);
        } catch (Throwable e) {
            log.error(action.getActionName() + " doInvoke error: {}", e);
            context.fireInvokeException(new GwException(e.getMessage(), e));
        }
    }

    @Override
    public void invokeException(ChannelContext context, GwException e) {
        final AbstractAction action = context.getAction();
        try {
            if (context.ensureActive()) {
                action.doException(context, e);
            }
        } catch (Throwable t) {
            log.error(action.getActionName() + " doException error: {}", t);
        } finally {
            log.error(action.getActionName() + " error: {}", e);
        }
    }

    @Override
    public void invokeCallBack(ChannelContext context) {
        final AbstractAction action = getPreAction(context);
        try {
            if (context.ensureActive()) {
                action.doCallBack(context);
            }
        } catch (GwException e) {
            context.fireInvokeException(e);
        } catch (Throwable e) {
            //打印日志堆栈信息
            context.fireInvokeException(new GwException(e.getMessage(), e));
        }
    }

    /**
     * Channel编排，建立action调用链
     */
    public abstract AbstractAction getChannel();

    /**
     * Channl头节点，用于处理Https I/O操作
     */
    class HeadAction extends AbstractAction {

        public HeadAction() {
            super("HeadAction");
        }

        @Override
        public void doException(ChannelContext context, GwException e) {
            //获取当前报文的报文头
            SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);
            //生成911报文DTO
            EnvelopeDTO<Dcep91100101DTO> dto = GwMsgUtils.dcep911(
                header.getMsgSN(),
                InfoCacheUtils.getPbocInf(),
                header.getReceiver(),
                e.getCode(),
                e.getDescription(),
                InfoCacheUtils.getPbocInf(),
                e.getMessage()
            );
            //保存911报文DTO
            context.setAttachment(Constant.DESERIALIZATION, dto);
            context.setAttachment(Constant.SOAP_HEADER, dto.getSoapHeader());

            doCallBack(context);
        }

        @Override
        public void doCallBack(ChannelContext context) {
            AsyncContext asyncContext = context.getServletContext();
            if (context.ensureActive()) {
                synchronized (asyncContext) {
                    if (context.ensureActive()) {
                        try {
                            //获取报文DTO
                            EnvelopeDTO<?> dto = (EnvelopeDTO<?>) context.getAttachment(Constant.DESERIALIZATION);
                            GwMsgUtils
                                .writerXmlToInst((HttpServletResponse) asyncContext.getResponse(), SoapUtils.toXml(dto),
                                    dto.getSoapHeader().getReceiver());
                        } finally {
                            //asyncContext.complete();
                            // 2021.6.7 提升并发性能，避免ChannelListener.onComplete产生锁竞争
                            context.close();
                        }
                    }
                }
            }
        }
    }

    /**
     * Channl尾节点，用于调整业务链处理方向
     */
    class TailAction extends AbstractAction {

        public TailAction() {
            super("TailAction");
        }

        @Override
        public void doInvoke(ChannelContext context) {
            this.doCallBack(context);
        }
    }

    /**
     * 重写equal方法
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
     */
    @Override
    public int hashCode() {
        return Objects.hash(channelName);
    }
}
