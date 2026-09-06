package com.dcep.supergw.controller.filter;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.Aplication;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@RunWith(JMockit.class)
@SpringBootTest(classes = Aplication.class)
public class SoapFilterTest {

    private MockHttpServletRequest request = new MockHttpServletRequest();
    private ServletResponse response = new MockHttpServletResponse();
    private FilterChain chain = new MockFilterChain();

    private SoapFilter filter = new SoapFilter();


    @Mocked
    LoggerUtils loggerUtils;

    @Test
    public void testDoFilter() throws IOException, ServletException {

        new MockUp<LoggerUtils>(LoggerUtils.class) {
            @Mock
            public void MDC(SoapHeader header) {

            }
        };
        request.setContent(TestMsgUtils.dcep_401().getBytes());
        filter.doFilter(request, response, chain);
    }

}
