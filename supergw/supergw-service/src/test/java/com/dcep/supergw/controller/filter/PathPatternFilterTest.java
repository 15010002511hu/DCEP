/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.controller.filter;

import java.util.Vector;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.exception.GwException;
import mockit.Expectations;
import mockit.Mocked;

public class PathPatternFilterTest {

    @Mocked
    private HttpServletRequest request;

    @Mocked
    private ServletResponse response;

    @Mocked
    private FilterChain chain;

    @Mocked
    private FilterConfig filterConfig;

    @Test
    public void test_doFilter_succ() {
        PathPatternFilter filter = new PathPatternFilter();

        new Expectations() {
            {
                filterConfig.getInitParameter("/infsync");
                result = "^dcep\\.466\\.\\d{3}\\.\\d{2}$";

                filterConfig.getInitParameterNames();
                Vector<String> v = new Vector<>();
                v.add("/infsync");
                result = v.elements();

                request.getAttribute("header");
                result = new SoapHeader("01", "", "dcep.466.001.01", "", "", "");

                request.getServletPath();
                result = "/infsync";
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.init(filterConfig);
                filter.doFilter(request, response, chain);
            }
        });
    }

    @Test
    public void test_doFilter_succ_1() {
        PathPatternFilter filter = new PathPatternFilter();

        new Expectations() {
            {
                filterConfig.getInitParameter("/dcep");
                result = "^dcep\\.(?!466|991)\\d{3}\\.\\d{3}\\.\\d{2}$";

                filterConfig.getInitParameterNames();
                Vector<String> v = new Vector<>();
                v.add("/dcep");
                result = v.elements();

                request.getAttribute("header");
                result = new SoapHeader("01", "", "dcep.301.001.01", "", "", "");

                request.getServletPath();
                result = "/dcep";
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.init(filterConfig);
                filter.doFilter(request, response, chain);
            }
        });
    }

    @Test
    public void test_doFilter_fail() {
        PathPatternFilter filter = new PathPatternFilter();

        new Expectations() {
            {
                filterConfig.getInitParameter("/dcep");
                result = "^dcep\\.(?!466|991)\\d{3}\\.\\d{3}\\.\\d{2}$";

                filterConfig.getInitParameterNames();
                Vector<String> v = new Vector<>();
                v.add("/dcep");
                result = v.elements();

                request.getAttribute("header");
                result = new SoapHeader("01", "", "dcep.466.001.01", "", "", "");

                request.getServletPath();
                result = "/dcep";
            }
        };

        Assertions.assertThrows(GwException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.init(filterConfig);
                filter.doFilter(request, response, chain);
            }
        });
    }
}
