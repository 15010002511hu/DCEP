package com.dcep.supergw.controller.listener;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.EnviromentUtils;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.HttpRpcLogUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapHeaderUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.DirectForwardChannel;
import java.lang.reflect.Field;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest
@RunWith(JMockit.class)
public class ChannelListenerTest {

    @Mocked
    private DirectForwardChannel channel1;
    @Mocked
    private DirectForwardChannel channel2;
    @Mocked
    LoggerUtils loggerUtils;
    @Mocked
    EnviromentUtils enviromentUtils;
//	@Mocked
//	HttpRpcLogUtils httpRpcLogUtils;


    private ChannelListener listener1;
    private ChannelListener listener2;
    private ChannelListener listener3;

    @Before
    public void init() {
        SoapHeader header1 = SoapUtils.getSoapHeaderBean(TestMsgUtils.dcep_401().getBytes());
        ChannelContext context1 = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_401());
        channel1 = new DirectForwardChannel(0);
        context1.setChannel(channel1);
        listener1 = new ChannelListener(header1, context1);

        SoapHeader header2 = SoapUtils.getSoapHeaderBean(TestMsgUtils.dcep_202().getBytes());
        ChannelContext context2 = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_202());
        context1.setChannel(channel1);
        context1.setRequest(false);
        listener2 = new ChannelListener(header2, context2);

        SoapHeader header3 = SoapUtils.getSoapHeaderBean(TestMsgUtils.dcep_401().getBytes());
        ChannelContext context3 = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_401());
        context1.setChannel(channel1);
        context3.close();
        listener3 = new ChannelListener(header3, context3);


    }

    private AsyncEvent event = new AsyncEvent(new AsyncContext() {
        @Override
        public ServletRequest getRequest() {
            ServletRequest request = new MockHttpServletRequest();
            request.setAttribute("beginTime", System.currentTimeMillis());
            return request;
        }

        @Override
        public ServletResponse getResponse() {
            ServletResponse response = new MockHttpServletResponse();
            return response;
        }

        @Override
        public boolean hasOriginalRequestAndResponse() {
            return false;
        }

        @Override
        public void dispatch() {

        }

        @Override
        public void dispatch(String s) {

        }

        @Override
        public void dispatch(ServletContext servletContext, String s) {

        }

        @Override
        public void complete() {

        }

        @Override
        public void start(Runnable runnable) {

        }

        @Override
        public void addListener(AsyncListener asyncListener) {

        }

        @Override
        public void addListener(AsyncListener asyncListener, ServletRequest servletRequest,
            ServletResponse servletResponse) {

        }

        @Override
        public <T extends AsyncListener> T createListener(Class<T> aClass) throws ServletException {
            return null;
        }

        @Override
        public void setTimeout(long l) {

        }

        @Override
        public long getTimeout() {
            return 0;
        }
    });

    @Test
    public void testOnComplete() {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };

        listener1.onComplete(event);
    }

    @Test
    public void testOnComplete1() throws NoSuchFieldException, IllegalAccessException {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };

        new MockUp<HttpRpcLogUtils>(HttpRpcLogUtils.class) {
            @Mock
            public void writeRpcLog(HttpServletRequest request, SoapHeader soapHeader, String resultCode) {

            }
        };

        Class<ChannelListener> ChannelListenerClass = ChannelListener.class;
        Field signClinetFiled = ChannelListenerClass.getDeclaredField("reason");

        signClinetFiled.setAccessible(true);
        signClinetFiled.set(listener1, "Timeout");
        listener1.onComplete(event);
    }

    @Test
    public void testOnComplete2() throws NoSuchFieldException, IllegalAccessException {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };
        Class<ChannelListener> ChannelListenerClass = ChannelListener.class;
        Field signClinetFiled = ChannelListenerClass.getDeclaredField("reason");

        signClinetFiled.setAccessible(true);
        signClinetFiled.set(listener1, "Error");
        listener1.onComplete(event);
    }

    @Test
    public void testOnError() {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };
        listener1.onError(event);
    }

    @Test
    public void testOnTimeout1() {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };

        new MockUp<SoapHeaderUtils>(SoapHeaderUtils.class) {
            @Mock
            public void setPbocSignSn(EnvelopeDTO dto) {

            }
        };
        new MockUp<GwMsgUtils>(GwMsgUtils.class) {
            @Mock
            public void writerXmlToInst(HttpServletResponse resp, String xml, String inst) {

            }
        };

        listener1.onTimeout(event);
    }

    @Test
    public void testOnTimeout2() {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };

        new MockUp<SoapHeaderUtils>(SoapHeaderUtils.class) {
            @Mock
            public void setPbocSignSn(EnvelopeDTO dto) {

            }
        };
        new MockUp<GwMsgUtils>(GwMsgUtils.class) {
            @Mock
            public void writerXmlToInst(HttpServletResponse resp, String xml, String inst) {

            }
        };
        listener2.onTimeout(event);
    }

    @Test
    public void testOnTimeout3() {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };

        new MockUp<SoapHeaderUtils>(SoapHeaderUtils.class) {
            @Mock
            public void setPbocSignSn(EnvelopeDTO dto) {

            }
        };
        new MockUp<GwMsgUtils>(GwMsgUtils.class) {
            @Mock
            public void writerXmlToInst(HttpServletResponse resp, String xml, String inst) {

            }
        };
        listener3.onTimeout(event);
    }

    @Test
    public void testOnStartAsync() {
        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };

        listener1.onStartAsync(event);
    }

}
