/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.manager.dubbo.filter;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.utils.EnviromentUtils;
import com.dubbo.ldc.ZoneClient;
import java.util.Collections;
import mockit.Expectations;
import mockit.Mocked;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class ProviderRpcLogFilterTest {

    @Mocked
    private Invoker<?> invoker;

    @Mocked
    private Invocation invocation;

    @Mocked
    private RpcContext rpcContext;

    @Mocked
    private ZoneClient zoneClient;

    @Mocked
    private EnviromentUtils envUtils;

    @Mocked
    private Result rpcResult;

    @Test
    public void test_invoke_result_is_null() {
        ProviderRpcLogFilter filter = new ProviderRpcLogFilter();

        new Expectations() {
            {
                invocation.getArguments();
                result = new Object[]{new EnvelopeDTO<GwDTO>(
                    new SoapHeader(), new SoapBody<GwDTO>())};

                invocation.getMethodName();
                result = "test_invoke_succ";
            }
        };

        new Expectations() {
            {
                invoker.getInterface();
                result = ProviderRpcLogFilterTest.class;

                invoker.getUrl();
                result = new URL("rpc", "127.0.0.1", 20882, Collections.singletonMap("application", "mock"));

                invoker.invoke((Invocation) any);
                result = null;
            }
        };

        new Expectations() {
            {
                RpcContext.getContext();
                result = rpcContext;

                rpcContext.getRemoteHost();
                result = "127.0.0.1";
            }
        };

        new Expectations() {
            {
                ZoneClient.getInstance();
                result = zoneClient;

                zoneClient.getZoneName();
                result = "CZ";
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.invoke(invoker, invocation);
            }
        });
    }

    @Test
    public void test_invoke_result_throw_unkown_exception() {
        ProviderRpcLogFilter filter = new ProviderRpcLogFilter();

        new Expectations() {
            {
                invocation.getArguments();
                result = new Object[]{new EnvelopeDTO<GwDTO>(
                    new SoapHeader(), new SoapBody<GwDTO>())};

                invocation.getMethodName();
                result = "test_invoke_succ";
            }
        };

        new Expectations() {
            {
                invoker.getInterface();
                result = ProviderRpcLogFilterTest.class;

                invoker.getUrl();
                result = new URL("rpc", "127.0.0.1", 20882, Collections.singletonMap("application", "mock"));

                invoker.invoke((Invocation) any);
                result = rpcResult;

                rpcResult.hasException();
                result = true;
            }
        };

        new Expectations() {
            {
                RpcContext.getContext();
                result = rpcContext;

                rpcContext.getRemoteHost();
                result = "127.0.0.1";
            }
        };

        new Expectations() {
            {
                ZoneClient.getInstance();
                result = zoneClient;

                zoneClient.getZoneName();
                result = "CZ";
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.invoke(invoker, invocation);
            }
        });
    }

    @Test
    public void test_invoke_result_throw_dcep_exception() {
        ProviderRpcLogFilter filter = new ProviderRpcLogFilter();

        new Expectations() {
            {
                invocation.getArguments();
                result = new Object[]{new EnvelopeDTO<GwDTO>(
                    new SoapHeader(), new SoapBody<GwDTO>())};

                invocation.getMethodName();
                result = "test_invoke_succ";
            }
        };

        new Expectations() {
            {
                invoker.getInterface();
                result = ProviderRpcLogFilterTest.class;

                invoker.getUrl();
                result = new URL("rpc", "127.0.0.1", 20882, Collections.singletonMap("application", "mock"));

                invoker.invoke((Invocation) any);
                result = rpcResult;

                rpcResult.hasException();
                result = true;

                rpcResult.getException();
                result = new DcepException("mock-error-code", "mock-error-desc");
            }
        };

        new Expectations() {
            {
                RpcContext.getContext();
                result = rpcContext;

                rpcContext.getRemoteHost();
                result = "127.0.0.1";
            }
        };

        new Expectations() {
            {
                ZoneClient.getInstance();
                result = zoneClient;

                zoneClient.getZoneName();
                result = "CZ";
            }
        };

        Assertions.assertThrows(DcepException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.invoke(invoker, invocation);
            }
        });
    }

    @Test
    public void test_invoke_result_is_response() {
        ProviderRpcLogFilter filter = new ProviderRpcLogFilter();

        new Expectations() {
            {
                invocation.getArguments();
                result = new Object[]{new EnvelopeDTO<GwDTO>(
                    new SoapHeader(), new SoapBody<GwDTO>())};

                invocation.getMethodName();
                result = "test_invoke_succ";
            }
        };

        new Expectations() {
            {
                invoker.getInterface();
                result = ProviderRpcLogFilterTest.class;

                invoker.getUrl();
                result = new URL("rpc", "127.0.0.1", 20882, Collections.singletonMap("application", "mock"));

                invoker.invoke((Invocation) any);
                result = rpcResult;

                rpcResult.getValue();
                result = new Response<EnvelopeDTO<GwDTO>>(new EnvelopeDTO<GwDTO>(
                    new SoapHeader(), new SoapBody<GwDTO>(new GwDTO() {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public void init() {
                    }

                    @Override
                    public String fetchMsgId() {
                        return null;
                    }

                    @Override
                    public boolean check(SoapHeader header) {
                        return false;
                    }
                })));
            }
        };

        new Expectations() {
            {
                RpcContext.getContext();
                result = rpcContext;

                rpcContext.getRemoteHost();
                result = "127.0.0.1";
            }
        };

        new Expectations() {
            {
                ZoneClient.getInstance();
                result = zoneClient;

                zoneClient.getZoneName();
                result = "CZ";
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                filter.invoke(invoker, invocation);
            }
        });
    }
}
