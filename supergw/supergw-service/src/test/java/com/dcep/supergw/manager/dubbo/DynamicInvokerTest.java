package com.dcep.supergw.manager.dubbo;

import static org.junit.Assert.assertNotNull;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.dto.dc401.Dcep40100101DTO;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.apache.dubbo.rpc.RpcException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@RunWith(JMockit.class)
public class DynamicInvokerTest {

    private final static String service = "com.dcep.clearing.api.ClearingService";
    private final static String method = "prepare";

    @Mocked
    ApplicationContext context = Mockito.mock(ApplicationContext.class);

    @Tested
    DynamicInvoker dynamicInvoker;

    @Test
    public void test_dynamicInvoker_invokeDubbo_succ()
        throws NoSuchFieldException, IllegalAccessException, InvocationTargetException {

        new Expectations() {
            {
                Deencapsulation.setField(dynamicInvoker, "context", context);
            }
        };

        Mockclass instance = new Mockclass();
        Mockito.when(context.getBean(service)).thenReturn(instance);
        EnvelopeDTO<GwDTO> dto = new EnvelopeDTO<GwDTO>();
        @SuppressWarnings("unchecked")
        Response<Dcep40100101DTO> response = (Response<Dcep40100101DTO>) DynamicInvoker.invokeDubbo(service, method,
            dto);
        assertNotNull(response);
    }

    @Test(expected = GwException.class)
    public void test_dynamicInvoker_invokeDubbo_throw_NoSuchMethodException() {
        new MockUp<Class>(Class.class) {
            @Mock
            Method getMethod(String arg0, Class<?>... arg1) throws NoSuchMethodException, SecurityException {
                throw new NoSuchMethodException("unittest");
            }
        };

        Response response = (Response) DynamicInvoker.invokeDubbo(service, method, new EnvelopeDTO());
    }

    @Test(expected = GwException.class)
    public void test_dynamicInvoker_invokeDubbo_throw_InvocationTargetException_DcepException() {
        new MockUp<Class>(Class.class) {
            @Mock
            Method getMethod(String arg0, Class<?>... arg1) throws InvocationTargetException, SecurityException {
                Throwable ta = new DcepException();
                throw new InvocationTargetException(ta, "unittest");
            }
        };

        Response response = (Response) DynamicInvoker.invokeDubbo(service, method, new EnvelopeDTO());
    }

    @Test(expected = GwException.class)
    public void test_dynamicInvoker_invokeDubbo_throw_InvocationTargetException_OtherException1() {

        new Expectations() {
            {
                Deencapsulation.setField(dynamicInvoker, "context", context);
            }
        };
        EnvelopeDTO<GwDTO> dto = new EnvelopeDTO<GwDTO>();
        Mockclass instance = new Mockclass();
        Mockito.when(context.getBean(service)).thenReturn(instance);
        Response response = (Response) DynamicInvoker.invokeDubbo(service, "finish", new EnvelopeDTO());
    }

    @Test(expected = Exception.class)
    public void test_dynamicInvoker_invokeDubbo_throw_IllegalAccessException() {

        new Expectations() {
            {
                Deencapsulation.setField(dynamicInvoker, "context", context);
            }
        };
        EnvelopeDTO<GwDTO> dto = new EnvelopeDTO<GwDTO>();
        Mockclass2 instance = Mockclass2.newins();
        Mockito.when(context.getBean(service)).thenReturn(instance);
        Response response = (Response) DynamicInvoker.invokeDubbo(service, "privateMethod", new EnvelopeDTO());
    }

    @Test(expected = GwException.class)
    public void test_dynamicInvoker_invokeDubbo_throw_InvocationTargetException_RpcException1() {
        new Expectations() {
            {
                Deencapsulation.setField(dynamicInvoker, "context", context);
            }
        };

        Mockclass instance = new Mockclass();
        Mockito.when(context.getBean(service)).thenReturn(instance);
        new MockUp<Class>(Class.class) {
            @Mock
            Method getMethod(String arg0, Class<?>... arg1) throws InvocationTargetException, SecurityException {
                Throwable ta = new RpcException(RpcException.NO_INVOKER_AVAILABLE_AFTER_FILTER);
                throw new InvocationTargetException(ta, "unittest");
            }
        };
        Response response = (Response) DynamicInvoker.invokeDubbo(service, method, new EnvelopeDTO());
    }

    @Test(expected = GwException.class)
    public void test_dynamicInvoker_invokeDubbo_throw_InvocationTargetException_RpcException2() {
        new Expectations() {
            {
                Deencapsulation.setField(dynamicInvoker, "context", context);
            }
        };

        Mockclass instance = new Mockclass();
        Mockito.when(context.getBean(service)).thenReturn(instance);
        new MockUp<Class>(Class.class) {
            @Mock
            Method getMethod(String arg0, Class<?>... arg1) throws InvocationTargetException, SecurityException {
                Throwable ta = new RpcException(RpcException.TIMEOUT_EXCEPTION);
                throw new InvocationTargetException(ta, "unittest");
            }
        };

        Response response = (Response) DynamicInvoker.invokeDubbo(service, method, new EnvelopeDTO());
    }

    @Test(expected = GwException.class)
    public void test_dynamicInvoker_invokeDubbo_throw_InvocationTargetException_RpcException3() {
        new Expectations() {
            {
                Deencapsulation.setField(dynamicInvoker, "context", context);
            }
        };

        Mockclass instance = new Mockclass();
        Mockito.when(context.getBean(service)).thenReturn(instance);
        new MockUp<Class>(Class.class) {
            @Mock
            Method getMethod(String arg0, Class<?>... arg1) throws InvocationTargetException, SecurityException {
                Throwable ta = new RpcException(RpcException.NETWORK_EXCEPTION);
                throw new InvocationTargetException(ta, "unittest");
            }
        };

        Response response = (Response) DynamicInvoker.invokeDubbo(service, method, new EnvelopeDTO());
    }

    @Test(expected = GwException.class)
    public void test_dynamicInvoker_invokeDubbo_throw_InvocationTargetException_RpcException4() {
        new Expectations() {
            {
                Deencapsulation.setField(dynamicInvoker, "context", context);
            }
        };

        Mockclass instance = new Mockclass();
        Mockito.when(context.getBean(service)).thenReturn(instance);
        new MockUp<Class>(Class.class) {
            @Mock
            Method getMethod(String arg0, Class<?>... arg1) throws InvocationTargetException, SecurityException {
                Throwable ta = new RpcException(RpcException.SERIALIZATION_EXCEPTION);
                throw new InvocationTargetException(ta, "unittest");
            }
        };

        Response response = (Response) DynamicInvoker.invokeDubbo(service, method, new EnvelopeDTO());
    }

    @Test(expected = GwException.class)
    public void test_dynamicInvoker_invokeDubbo_throw_InvocationTargetException_RpcException5() {
        new Expectations() {
            {
                Deencapsulation.setField(dynamicInvoker, "context", context);
            }
        };

        Mockclass instance = new Mockclass();
        Mockito.when(context.getBean(service)).thenReturn(instance);

        new MockUp<Class>(Class.class) {
            @Mock
            Method getMethod(String arg0, Class<?>... arg1) throws InvocationTargetException, SecurityException {
                Throwable ta = new RpcException(RpcException.BIZ_EXCEPTION);
                throw new InvocationTargetException(ta, "unittest");
            }
        };

        Response response = (Response) DynamicInvoker.invokeDubbo(service, method, new EnvelopeDTO());
    }

    class Mockclass {

        public Response<GwDTO> prepare(EnvelopeDTO<GwDTO> req) throws DcepException {
            return new Response<>(new Dcep40100101DTO());
        }

        public Response<GwDTO> finish(EnvelopeDTO<GwDTO> req) throws DcepException, FileNotFoundException {

            throw new FileNotFoundException();

        }

        protected Response<GwDTO> privateMethod(EnvelopeDTO<GwDTO> req) {
            return new Response<>(new Dcep40100101DTO());
        }
    }

    static class Mockclass2 {

        private static Mockclass2 mockclass2 = new Mockclass2();

        private Mockclass2() {

        }

        public static Mockclass2 newins() {
            return mockclass2;
        }
    }

    public Response<GwDTO> prepare(EnvelopeDTO<GwDTO> req) throws DcepException {
        return new Response<>(new Dcep40100101DTO());
    }

    public Response<GwDTO> finish(EnvelopeDTO<GwDTO> req) throws DcepException, FileNotFoundException {

        throw new FileNotFoundException();

    }

    public Response<GwDTO> privateMethod(EnvelopeDTO<GwDTO> req) {
        return new Response<>(new Dcep40100101DTO());
    }
}
