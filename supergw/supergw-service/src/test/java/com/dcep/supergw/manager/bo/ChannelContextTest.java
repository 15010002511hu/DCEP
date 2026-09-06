package com.dcep.supergw.manager.bo;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.manager.channel.AbstractChannel;
import com.dcep.supergw.manager.channel.AgencyProcessChannel;
import com.dcep.supergw.manager.channel.DirectForwardChannel;
import javax.servlet.AsyncContext;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : ChannelContextTest.java v 0.1 2019-12-11
 * @description : ChannelContext测试类
 */
@SpringBootTest
@RunWith(JMockit.class)
public class ChannelContextTest {

    @Mocked
    AgencyProcessChannel channel;
    private ChannelContext context = new ChannelContext();
    private String xml = TestMsgUtils.dcep_401();

    @Before
    public void init() {
        context.setChannel(channel);
        new MockUp<AbstractChannel>(AbstractChannel.class) {
            @Mock
            public void invokeAction(ChannelContext context) {

            }

            @Mock
            public void invokeException(ChannelContext context, GwException e) {

            }

            @Mock
            public void invokeCallBack(ChannelContext context) {

            }
        };

        String msgId = TestMsgUtils.getMsgId("401");
        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setVer("01");
        soapHeader.setSndDtTm(DcepDateUtils.getDcepDateStrNow());
        soapHeader.setMsgTp("dcep.401.001.01");
        soapHeader.setMsgSN(msgId + "0001");
        soapHeader.setSender("C1010511003703");
        soapHeader.setReceiver("C1010511003703");
        soapHeader.setSignSN("3");
        soapHeader.setNcrptnSN("3");
        soapHeader.setDgtlEnvlp("3");

        DirectForwardChannel channel = new DirectForwardChannel(5);

        context = ChannelContextMocker.newChannelContext(xml);

        context.setChannel(channel);
    }

    /**
     * 测试setChannel
     */
    @Test
    public void testSetChannel() {
        DirectForwardChannel channel = new DirectForwardChannel(5);
        context.setChannel(channel);
    }

    /**
     * 测试setEntry(SoapHeader header, String xml, String signature)
     */
//    @Test
//    public void testSetEntry_param3() {
//        String msgId = TestMsgUtils.getMsgId("401");
//        SoapHeader soapHeader = new SoapHeader();
//        soapHeader.setVer("01");
//        soapHeader.setSndDtTm(DcepDateUtils.getDcepDateStrNow());
//        soapHeader.setMsgTp("dcep.401.001.01");
//        soapHeader.setMsgSN(msgId + "0001");
//        soapHeader.setSender("C1010511003703");
//        soapHeader.setReceiver("C1010511003703");
//        soapHeader.setSignSN("3");
//        soapHeader.setNcrptnSN("3");
//        soapHeader.setDgtlEnvlp("3");
//        context.setEntry(soapHeader, xml, "");
//    }

    /**
     * 测试ensureActive
     */
    @Test
    public void testEnsureActive() {
        context.ensureActive();
    }

    /**
     * 测试close
     */
    @Test
    public void testClose1() {
        AsyncContext asyncContext = new MockUp<AsyncContext>(AsyncContext.class) {
            @Mock
            public void complete() throws Exception {
                throw new Exception();
            }
        }.getMockInstance();

        context.setServletContext(asyncContext);
        context.close();
    }

    @Test
    public void testClose2() {
        context.close();
        context.close();
    }

    @Test
    public void testClose3() {
        try {
            context.setChannel(null);
        } catch (Exception e) {
        }
        context.close();
    }

    /**
     * 测试fireInvokeChannel
     */
    @Test
    public void testFireInvokeChannel() {
        context.fireInvokeAction();
    }

    /**
     * 测试fireInvokeException
     */
    @Test
    public void testFireInvokeException() {
        GwException e = new GwException();
        context.fireInvokeException(e);
    }

    /**
     * 测试fireInvokeCallBack
     */
    @Test
    public void testFireInvokeCallBack() {
        context.fireInvokeCallBack();
    }

}
