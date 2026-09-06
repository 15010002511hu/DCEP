package com.dcep.supergw.controller.filter;


import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.sentinel.AbstractFlowProcess;
import com.dcep.supergw.common.sentinel.DcepFlowProcess;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.HttpRpcLogUtils;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowFilter implements Filter {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        if (!(servletRequest instanceof HttpServletRequest) || !(servletResponse instanceof HttpServletResponse)) {
            throw new ServletException("FlowFilter just supports HTTP requests");
        }
        SoapHeader header = (SoapHeader) servletRequest.getAttribute("header");
        AbstractFlowProcess process = new DcepFlowProcess();
        String xml = new String((byte[]) servletRequest.getAttribute("xml"));
        try {
            // 调用process获取entrys
            process.obtainEntry(header);
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (BlockException e) {
            log.error("发生限流异常:{},soapHeader:{},xml:{}", e, header, xml);
            HttpRpcLogUtils
                .writeRpcLog((HttpServletRequest) servletRequest, header, GwErrorEnum.LIMITING_ERROR.getCode());
            // 回写911报文
            GwMsgUtils.writerXmlToInst((HttpServletResponse) servletResponse, GwMsgUtils
                .dcep911(header, GwErrorEnum.LIMITING_ERROR.getCode(), GwErrorEnum.LIMITING_ERROR.getDescription(),
                    GwErrorEnum.LIMITING_ERROR.getDescription()), header.getSender());
        } catch (Exception e) {
            HttpRpcLogUtils
                .writeRpcLog((HttpServletRequest) servletRequest, header, GwErrorEnum.UNKNOWN_EXCEPTION.getCode());
            // 发生未知异常，应答机构S999报文
            GwMsgUtils.writerXmlToInst((HttpServletResponse) servletResponse, GwMsgUtils
                    .dcep911(header, GwErrorEnum.UNKNOWN_EXCEPTION.getCode(),
                        GwErrorEnum.UNKNOWN_EXCEPTION.getDescription(), GwErrorEnum.UNKNOWN_EXCEPTION.getDescription()),
                header.getSender());
        } finally {
            // 调用process释放entrys
            process.releaseEntry();
        }


    }
}
