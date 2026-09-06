/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.controller;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.config.BeansCondition;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.controller.listener.ChannelListener;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.service.GwinService;
import com.dcepex.trace.support.http.TraceAsyncListener;
import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author huyajun
 * @version $Id: MainServer.java, v 0.1 2019年8月6日 下午2:05:29 Administrator Exp $
 * 网关代码主入口controller
 */
@Slf4j
@WebServlet(urlPatterns = {"/dcep", "/infsync"}, asyncSupported = true, name = "dcepserver")
@BeansCondition(name = "beans.controller", havingValue = "MainController")
public class MainController extends HttpServlet {

    /**  */
    private static final long serialVersionUID = -2856286041934915203L;

    @Autowired
    GwinService channels;

    @Value("${servlet.async.timeout:3000}")
    int servlet_timeout;

    /**
     * 获取报文后验签
     *
     * @param req
     * @param resp
     */
    private void doExecute(HttpServletRequest req, HttpServletResponse resp) {
        final String signature = req.getHeader("Signature");
        final SoapHeader header = (SoapHeader) req.getAttribute("header");
        final byte[] xml = (byte[]) req.getAttribute("xml");

        // 设置Channel上下文信息
        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.SERIALIZATION, xml);
        context.setAttachment(Constant.SIGNATURE, signature);

        // 启动Servlet异步流程
        AsyncContext ctx = req.startAsync();
        try {
            ctx.setTimeout(servlet_timeout);
            ctx.addListener(new TraceAsyncListener(new ChannelListener(header, context)));
            context.setServletContext(ctx);
            channels.start(context);
        } catch (GwException e) {
            if ((GwErrorEnum.LIMITING_ERROR.getCode()).equals(e.getCode())) {
                log.error(GwErrorEnum.LIMITING_ERROR.getDescription());
            } else {
                log.error("MainServer.doExecute: {}", e);
            }
            //生成911报文
            String dcep911Xml = assembleDcep911(context, e.getCode(),
                e.getDescription(), e.getMessage());
            GwMsgUtils.writerXmlToInst(resp, dcep911Xml, (header.getSender()));
            ctx.complete();
        } catch (Throwable e) {
            log.error("MainServer.doExecute: {}", e);
            String dcep911Xml = assembleDcep911(context,
                GwErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                GwErrorEnum.UNKNOWN_EXCEPTION.getDescription(),
                e.getMessage());
            GwMsgUtils.writerXmlToInst(resp, dcep911Xml, (header.getSender()));
            ctx.complete();
        } finally {
            //运营机构的报文日志改为记录DTO信息
            LoggerUtils.logMsg(header.getSender(), signature, xml);
            MDC.clear();
        }
    }

    /**
     * 拼装911报文DTO保存至ChannelConext,
     *
     * @param context
     * @param errCode
     * @param errMesg
     * @param detail
     * @return
     */
    private String assembleDcep911(ChannelContext context, String errCode, String errMesg, String detail) {
        //获取当前报文的报文头
        SoapHeader header = (SoapHeader) context.getAttachment(Constant.SOAP_HEADER);
        //生成911报文DTO
        EnvelopeDTO<Dcep91100101DTO> dto = GwMsgUtils.dcep911(
            header.getMsgSN(),
            InfoCacheUtils.getPbocInf(),
            header.getSender(),
            errCode,
            errMesg,
            header.getSender(),
            detail
        );
        //保存911报文DTO
        context.setAttachment(Constant.DESERIALIZATION, dto);
        context.setAttachment(Constant.SOAP_HEADER, dto.getSoapHeader());

        return SoapUtils.toXml(dto);
    }

    //get方式接收报文，调用doPost()
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
        IOException {
        this.doPost(req, resp);
    }

    //post方式接收报文，最终调用doExecute()
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
        IOException {
        this.doExecute(req, resp);
    }
}

