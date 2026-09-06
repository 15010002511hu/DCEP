/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.manager.https.interceptor;

import com.dcep.common.Constants.CommonConstant;
import com.dcepex.trace.support.TraceContext;
import mockit.Expectations;
import mockit.Mocked;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.concurrent.FutureCallback;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class EnvFlagHttpCallBackTest {

    @Mocked
    private FutureCallback<HttpResponse> callback;

    @Mocked
    private HttpResponse response;

    @Mocked
    private TraceContext context;

    @Test
    public void test_callback() {
        EnvFlagHttpCallBack envCallBack = new EnvFlagHttpCallBack(callback);

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
                envCallBack.failed(new Exception());

                envCallBack.cancelled();

                envCallBack.completed(response);
            }

            ;
        });
    }
}
