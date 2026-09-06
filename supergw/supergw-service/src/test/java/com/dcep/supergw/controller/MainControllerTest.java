package com.dcep.supergw.controller;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.sentinel.DcepFlowProcess;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapHeaderUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.controller.listener.ChannelListener;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.service.GwinService;
import java.io.IOException;
import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
@RunWith(JMockit.class)
public class MainControllerTest {

    /**
     * 待测试的类
     */
    @Tested
    MainController controller;

    /**
     * mock需要的工具类
     */
    @Mocked
    SoapHeaderUtils soapHeaderUtils;

    @Mocked
    LoggerUtils loggerUtils;

    @Injectable
    GwinService channels;

    @Injectable
    int servlet_timeout = 3000;

    @Mocked
    HttpServletRequest request;

    @Mocked
    HttpServletResponse response;

    @Mocked
    ChannelContext context;

    @Mocked
    AsyncContext asyncContext;

    @Mocked
    ChannelListener listener;

    @Mocked
    SoapHeader header;

    @Injectable
    Logger logger;

    @Mocked
    GwMsgUtils gwMsgUtils;

    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Mocked
    SoapUtils soapUtils;

    @Mocked
    DcepFlowProcess dcepFlowProcess;

    @Before
    public void init() {
        header.setVer("01");
        header.setSender("0200");
        header.setReceiver("0000");
        header.setMsgSN("00000");

    }

    private void mocked() {
        new Expectations() {
            {
                request.getHeader("Signature");
                result = "abc";
                request.getAttribute("header");
                result = header;
                request.getAttribute("xml");
                result = "xxx".getBytes();
                context.getAttachment("soapheader");
                result = header;
                soapUtils.toXml((EnvelopeDTO) any);
                result = "xxxx";
            }
        };
    }

    /**
     * 测试doGet方法
     */
    @Test
    public void test_doGet() {
        new Expectations() {
            {
                request.getHeader("Signature");
                result = "abc";
                request.getAttribute("header");
                result = header;
                request.getAttribute("xml");
                result = "xxx".getBytes();
            }
        };
        try {
            controller.doGet(request, response);
        } catch (ServletException e) {
        } catch (IOException e) {
        }
    }

    /**
     * 测试doExcute方法 限流异常
     */
    @Test
    public void test_doExcute_LIMITING() {
        mocked();
        new Expectations() {
            {
                channels.start((ChannelContext) any);
                result = new GwException(GwErrorEnum.LIMITING_ERROR);
            }
        };
        try {
            controller.doPost(request, response);
        } catch (ServletException e) {

        } catch (IOException e) {

        }
    }

    /**
     * 测试doExcute方法 其他Gw异常
     */
    @Test
    public void test_doExcute_GwException() {
        mocked();
        new Expectations() {
            {
                channels.start((ChannelContext) any);
                result = new GwException(GwErrorEnum.UNKNOWN_EXCEPTION);
            }
        };
        try {
            controller.doPost(request, response);
        } catch (ServletException e) {

        } catch (IOException e) {

        }
    }

    /**
     * 测试doExcute方法 其他Gw异常
     */
    @Test
    public void test_doExcute_Exception() {
        mocked();
        new Expectations() {
            {
                channels.start((ChannelContext) any);
                result = new RuntimeException(GwErrorEnum.UNKNOWN_EXCEPTION.getDescription());
            }
        };
        try {
            controller.doPost(request, response);
        } catch (ServletException e) {

        } catch (IOException e) {

        }
    }


    /**
     * 测试doExcute方法
     */
    @Test
    public void test_doExcute_normal() {
        new Expectations() {
            {
                request.getHeader("Signature");
                result = "abc";
                request.getAttribute("header");
                result = header;
                request.getAttribute("xml");
                result = "xxx".getBytes();
            }
        };
        try {
            controller.doPost(request, response);
        } catch (ServletException e) {

        } catch (IOException e) {

        }
    }


    /**
     * 测试doPost方法 BlockingExcetion
     */
    @Test
    public void test_doPost_BlockException() {
        new Expectations() {
            {
                request.getAttribute("header");
                result = header;
            }
        };

        new Expectations() {
            {
                try {
                    dcepFlowProcess.obtainEntry(header);
                    result = new FlowException("xxxx");
                } catch (BlockException e) {

                }
            }
        };

        try {
            controller.doPost(request, response);
        } catch (ServletException e) {

        } catch (IOException e) {

        }
    }

    /**
     * 测试doPost方法 Exception
     */
    @Test
    public void test_doPost_Exception() {
        new Expectations() {
            {
                request.getAttribute("header");
                result = header;
                try {
                    dcepFlowProcess.obtainEntry(header);
                    result = new RuntimeException("xxxx");
                } catch (BlockException e) {

                }
            }
        };
        try {
            controller.doPost(request, response);
        } catch (ServletException e) {

        } catch (IOException e) {

        }
    }
}
