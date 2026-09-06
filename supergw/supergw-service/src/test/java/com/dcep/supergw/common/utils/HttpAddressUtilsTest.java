package com.dcep.supergw.common.utils;

import com.dcep.common.enums.MessageTypeEnum;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import mockit.Capturing;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * @author nieyanping
 * @version HttpRpcLogUtilsTest.java, v 0.1, 2019/12/10 16:54
 * @description TODO
 */
@SpringBootTest
@RunWith(JMockit.class)
public class HttpAddressUtilsTest {

    HttpAddressUtils httpAddressUtils = null;

    private SoapHeader header;

    private String resultCode = "PR00";

    @Capturing
    HttpServletRequest httpRequest;

    @Mocked
    NetworkInterface networkInterface;

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

    @Before
    public void init() {
        new MockUp<EnviromentUtils>(EnviromentUtils.class) {
            @Mock
            public String get(String key) {
                return "supergw";
            }
        };
        httpAddressUtils = new HttpAddressUtils();

        header = new SoapHeader();
        header.setVer("01");
        header.setMsgSN("201912040021933788415372917960000001");
        header.setMsgTp(MessageTypeEnum.DCEP_933_001_01.getCode());
        header.setSender("C1010211000012");
        header.setReceiver("00000000000000");
        header.setSndDtTm(DcepDateUtils.getDcepDateStrNow());
    }


    @Test
    public void testClientIpAddr1() {
        HttpServletRequest request = (HttpServletRequest) event.getAsyncContext().getRequest();

        String clientIp = httpAddressUtils.clientIpAddr(request);
        System.out.println(clientIp);
    }

    /**
     *
     */
    @Test
    public void testClientIpAddr2() {
        MockUp<HttpServletRequest> proxy = new MockUp<HttpServletRequest>() {
            @Mock
            public String getHeader(String str) {
                return "unknown,192.168.30.101";
            }
        };
        HttpServletRequest httpServletRequest = proxy.getMockInstance();

        new MockUp<EnviromentUtils>(EnviromentUtils.class) {
            @Mock
            public String get(String key) {
                return "supergw";
            }
        };
        httpAddressUtils.clientIpAddr(httpServletRequest);
    }


    /**
     * 分支测试/iP长度小于或等于15
     */
    @Test
    public void testClientIpAddr3() {
        new Expectations() {
            {
                httpRequest.getHeader("X-Forwarded-For");
                result = "192.168.30";
            }
        };
        new MockUp<EnviromentUtils>(EnviromentUtils.class) {
            @Mock
            public String get(String key) {
                return "supergw";
            }
        };

        httpAddressUtils.clientIpAddr(httpRequest);

    }
//    @Test
//  public void testRouteChannel() throws NoSuchMethodException,  IllegalAccessException, IllegalArgumentException, InvocationTargetException {
//      
//      Method method = httpAddressUtils.getClass().getDeclaredMethod("test", String.class);
//      method.setAccessible(true);
//      method.invoke(httpAddressUtils,"");
//      method.invoke(httpAddressUtils,"1");
//      method.invoke(httpAddressUtils,"unknown");
//    }

    /**
     * 分支测试/iP长度为0
     */
    @Test
    public void testClientIpAddr4() {
        new Expectations() {
            {
                httpRequest.getHeader(anyString);
                result = "";
            }
        };
        new MockUp<EnviromentUtils>(EnviromentUtils.class) {
            @Mock
            public String get(String key) {
                return "supergw";
            }
        };

        httpAddressUtils.clientIpAddr(httpRequest);

    }

    /**
     * 分支测试/ip为unknown
     */
    @Test
    public void testClientIpAddr5() {
        new Expectations() {
            {
                httpRequest.getHeader(anyString);
                result = "192.168.30";
            }
        };
        new MockUp<EnviromentUtils>(EnviromentUtils.class) {
            @Mock
            public String get(String key) {
                return "supergw";
            }
        };

        httpAddressUtils.clientIpAddr(httpRequest);

    }

    /**
     * 分支测试/ip为unknown
     */
    @Test
    public void testClientIpAddr7() {
        new Expectations() {
            {
                httpRequest.getHeader(anyString);
                result = "unknown";
            }
        };
        new MockUp<EnviromentUtils>(EnviromentUtils.class) {
            @Mock
            public String get(String key) {
                return "supergw";
            }
        };

        httpAddressUtils.clientIpAddr(httpRequest);

    }

    @Test
    public void testClientIpAddr6() {
        new Expectations() {
            {
                httpRequest.getHeader(anyString);
                result = "192.168.30.11111112";
            }
        };
        new MockUp<String>(String.class) {
            @Mock
            public String[] split(String regex) {
                return null;
            }
        };

        httpAddressUtils.clientIpAddr(httpRequest);

    }


    @Test
    public void testLocalIpAddr_success() {
        String localIp = httpAddressUtils.localIpAddr();
        System.out.println(localIp);
    }

    @Test
    public void testLocalIpAddr_fail() {
        new MockUp<InetAddress>(InetAddress.class) {
            @Mock
            public InetAddress getLocalHost() throws UnknownHostException {
                throw new UnknownHostException();
            }
        };
        new MockUp<NetworkInterface>(NetworkInterface.class) {
            @Mock
            public Enumeration<NetworkInterface> getNetworkInterfaces() throws SocketException {
                throw new SocketException();
            }
        };
        httpAddressUtils.localIpAddr();
    }

    /**
     * 测试 localIpAddr  networkInterfaces.hasMoreElements() = false, StringUtils.isEmpty = fasle
     */
    @Test
    public void testLocalIpAddr_NetWorkInterfacesElementsNon() {
        new MockUp<Enumeration>(Enumeration.class) {
            @Mock
            boolean hasMoreElements() {
                return true;
            }
        };
        new MockUp<StringUtils>() {
            @Mock
            public boolean isEmpty(final CharSequence cs) {
                return false;
            }
        };

        String localIp = httpAddressUtils.localIpAddr();
    }


}