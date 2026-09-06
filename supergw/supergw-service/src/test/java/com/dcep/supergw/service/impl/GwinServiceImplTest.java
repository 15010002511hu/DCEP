package com.dcep.supergw.service.impl;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.infocache.manager.NacosConsume;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.AbstractChannel;
import com.dcep.supergw.manager.channel.CentralProcessChannel;
import com.dcep.supergw.manager.channel.ClearingChannel;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JMockit.class)
public class GwinServiceImplTest {

    private String xml = TestMsgUtils.dcep_202();

    @Tested
    GwinServiceImpl service;

    @Injectable
    Set<AbstractChannel> channels;

    @Mocked
    ValidateUtils validateUtils;

    @Before
    public void init() {
        service = new GwinServiceImpl();

    }

//	@Test(expected = Exception.class)
//	public void testGetChannelInstance()
//			throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//		String name = "com.dcep.supergw.manager.channel.DirectForwardChannel";
//		Method method = service.getClass().getDeclaredMethod("getChannelInstance", new Class[] { String.class });
//		method.setAccessible(true);
//		method.invoke(service, name);
//	}

    @Test(expected = Exception.class)
    public void test_gwinServiceImpl_start()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<AbstractChannel> channels = new HashSet<AbstractChannel>();
        channels.add(new CentralProcessChannel(2));
        ChannelContext context = ChannelContextMocker.newChannelContext(xml);
        new Expectations() {
            {
                Deencapsulation.setField(service, "channels", channels);
            }

            {
                NacosConsume.getIsEncrypt();
                result = false;
            }

        };
        new MockUp<SoapUtils>(SoapUtils.class) {
            @Mock
            public <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, byte[] xml) {
                return (EnvelopeDTO<T>) ChannelContextMocker.toDto(header, xml);
            }
        };
        service.start(context);
    }

    @Test
    public void test_gwinServiceImpl_start2()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<AbstractChannel> channels = new HashSet<AbstractChannel>();
        channels.add(new CentralProcessChannel(2));
        channels.add(new ClearingChannel(2));
        ChannelContext context = ChannelContextMocker.newChannelContext(xml);
        new Expectations() {
            {
                Deencapsulation.setField(service, "channels", channels);
            }

            {
                NacosConsume.getIsEncrypt();
                result = false;
            }
        };

        new MockUp<SoapUtils>(SoapUtils.class) {
            @Mock
            public <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, byte[] xml) {
                return (EnvelopeDTO<T>) ChannelContextMocker.toDto(header, xml);
            }
        };

        service.start(context);
    }

    @Test
    public void test_gwinServiceImpl_start3()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<AbstractChannel> channels = new HashSet<AbstractChannel>();
        channels.add(new CentralProcessChannel(2));
        channels.add(new ClearingChannel(2));
        ChannelContext context = ChannelContextMocker.newChannelContext(xml);
        new Expectations() {
            {
                Deencapsulation.setField(service, "channels", channels);
            }

            {
                NacosConsume.getIsEncrypt();
                result = false;
            }
        };

        new MockUp<SoapUtils>(SoapUtils.class) {
            @Mock
            public <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, byte[] xml) {
                return (EnvelopeDTO<T>) ChannelContextMocker.toDto(header, xml);
            }
        };

        new MockUp<ValidateUtils>(ValidateUtils.class) {
            @Mock
            public void validateMsg(SoapHeader header, byte[] content, String signStr) {
                throw new RejectedExecutionException();
            }
        };

        service.start(context);
    }

}
