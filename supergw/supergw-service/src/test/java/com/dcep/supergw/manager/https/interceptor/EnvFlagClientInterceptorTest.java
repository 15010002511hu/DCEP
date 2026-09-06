/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.manager.https.interceptor;

import com.dcep.common.Constants.CommonConstant;
import com.dcepex.trace.support.TraceContext;
import mockit.Expectations;
import mockit.Mocked;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.protocol.HttpContext;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class EnvFlagClientInterceptorTest {

    @Mocked
    private HttpRequest request;

    @Mocked
    private HttpResponse response;

    @Mocked
    private HttpContext context;

    @Mocked
    private TraceContext traceContext;

    @Test
    public void test_request_process() {
        EnvFlagClientInterceptor interceptor = new EnvFlagClientInterceptor();

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                interceptor.process(request, context);
            }
        });
    }

    @Test
    public void test_response_process() {
        EnvFlagClientInterceptor interceptor = new EnvFlagClientInterceptor();

        new Expectations() {
            {
                response.getStatusLine();
                result = new StatusLine() {

                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        return null;
                    }

                    @Override
                    public int getStatusCode() {
                        return 200;
                    }

                    @Override
                    public String getReasonPhrase() {
                        return null;
                    }
                };
            }
        };

        new Expectations() {
            {
                TraceContext.get(CommonConstant.LOAD_TRAFFIC_KEY);
                result = "true";
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                interceptor.process(response, context);
            }
        });
    }
}
