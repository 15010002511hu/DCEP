package com.dcep.supergw.controller.listener;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.HttpRpcLogUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcepex.trace.support.UDT;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class ChannelListener implements AsyncListener {

    private final SoapHeader header;

    private final ChannelContext channelContext;

    private String reason = "Complete";

    public ChannelListener(SoapHeader header, ChannelContext channelContext) {
        this.header = header;
        this.channelContext = channelContext;
    }

    //收到响应报文时触发的回调函数
    @Override
    public void onComplete(AsyncEvent event) {
        try {
            LoggerUtils.mdc(header);
            String dcepResultCode = ((EnvelopeDTO<?>) channelContext.getAttachment(Constant.DESERIALIZATION))
                .body().fetchResultCode();
            String httpResultCode = "Timeout".equals(reason) ? "-" + GwErrorEnum.MANAGER_MAN_TIMEOUT.getCode() :
                "Error".equals(reason) ? "-" + GwErrorEnum.UNKNOWN_EXCEPTION.getCode() : dcepResultCode;

            // 全链路业务监控增加，用于耗时统计
            UDT.setAsyncResCode(event, httpResultCode);

            HttpRpcLogUtils.writeRpcLog((HttpServletRequest) event.getAsyncContext().getRequest(), header,
                httpResultCode);
        } catch (Exception e) {
            log.error("ChannelListener on complete error:{}", e);
        } finally {
            final long begTime = (long) event.getAsyncContext().getRequest().getAttribute("beginTime");
            channelContext.close();
            log.info("on{} cost: {} ms", reason, (System.currentTimeMillis() - begTime));
            MDC.clear();
        }
    }

    //超时回调函数
    @Override
    public void onTimeout(AsyncEvent event) {
        reason = "Timeout";
        LoggerUtils.mdc(header);
        AsyncContext context = event.getAsyncContext();
        if (channelContext.ensureActive()) {
            synchronized (context) {
                if (channelContext.ensureActive()) {
                    // 生成911报文DTO
                    EnvelopeDTO<Dcep91100101DTO> dto = GwMsgUtils.dcep911(header.getMsgSN(),
                        InfoCacheUtils.getPbocInf(), header.getSender(), GwErrorEnum.MANAGER_MAN_TIMEOUT.getCode(),
                        GwErrorEnum.MANAGER_MAN_TIMEOUT.getDescription(),
                        /*
                         * 当isRequest为false时，说明是卡在接收方导致超时的，故填receive；
                         * 当isRequest为true时，说明是接收方回了但是在网关处理时超时了，填央行号 by duz 2020-10-19
                         */
                        channelContext.isRequest() ? InfoCacheUtils.getPbocInf() : header.getReceiver(),
                        GwErrorEnum.MANAGER_MAN_TIMEOUT.getDescription());
                    // 保存911报文DTO
                    /*
                     * 避免应答报文处理线程读取到911报文数据处理异常去掉了
                     * channelContext.setAttachment(Constant.DESERIALIZATION, dto)和
                     * channelContext.setAttachment(Constant.SOAP_HEADER, dto.getSoapHeader()) by
                     * duzhong 20201014
                     */

                    GwMsgUtils.writerXmlToInst((HttpServletResponse) context.getResponse(), SoapUtils.toXml(dto),
                        header.getSender());
                    channelContext.close();
                }
            }
        }
        MDC.clear();
    }

    //出错时的处理
    @Override
    public void onError(AsyncEvent event) {
        reason = "Error";
        LoggerUtils.mdc(header);
        final long begTime = (long) event.getAsyncContext().getRequest().getAttribute("beginTime");
        log.info("onError cost: {} ms", (System.currentTimeMillis() - begTime));
        channelContext.close();
        MDC.clear();
    }

    //开始异步线程时的逻辑
    @Override
    public void onStartAsync(AsyncEvent event) {
        LoggerUtils.mdc(header);
        channelContext.getStatus().set(ChannelContext.Status.ACTIVE);
        final long begTime = (long) event.getAsyncContext().getRequest().getAttribute("beginTime");
        log.info("onStartAsync cost: {} ms", (System.currentTimeMillis() - begTime));
        MDC.clear();
    }
}
