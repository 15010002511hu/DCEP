/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.controller.filter;

import com.dcepex.trace.support.TraceContext;
import javax.servlet.FilterChain;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import mockit.Mocked;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class EnvFlagFilterTest {

    @Mocked
    private HttpServletRequest request;

    @Mocked
    private ServletResponse response;

    @Mocked
    private FilterChain chain;

    @Mocked
    private TraceContext context;

    @Test
    public void test_doFilter_succ() {
        EnvFlagFilter filter = new EnvFlagFilter();

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.doFilter(request, response, chain);
            }
        });
    }
}
