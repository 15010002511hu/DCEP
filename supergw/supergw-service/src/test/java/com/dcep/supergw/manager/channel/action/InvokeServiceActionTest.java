package com.dcep.supergw.manager.channel.action;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90200101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.AbstractChannel;
import com.dcep.supergw.manager.channel.ChannelBuilder;
import com.dcep.supergw.manager.dubbo.DynamicInvoker;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JMockit.class)
public class InvokeServiceActionTest {

    private InvokeServiceAction invokeServiceAction = new InvokeServiceAction();

    @Test
    public void test_invokeServiceAction_doInvoke_402_return_902() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_401());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new EnvelopeDTO<Dcep90200101DTO>(new SoapHeader("", "", "", "", "", ""),
                    new Dcep90200101DTO()));
            }
        };

        invokeServiceAction.doInvoke(context);
    }

    @Test(expected = GwException.class)
    public void test_invokeServiceAction_doInvoke_402_throwException() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_401());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>("DCEPXXXXX");
            }
        };

        invokeServiceAction.doInvoke(context);
    }

    private ChannelContext newMockChannelContext(String msg) {
        ChannelContext channelContext = ChannelContextMocker.newChannelContext(msg);
        channelContext.setChannel(new AbstractChannel("invokeServiceAction unit test") {
            @Override
            public AbstractAction getChannel() {
                return ChannelBuilder.getInstance().addAction(invokeServiceAction)
                    .addAction(new AbstractAction("invokeServiceAction unit test") {
                        @Override
                        public void doInvoke(ChannelContext context) {
                        }
                    }).build();
            }
        });
        channelContext.setAction(invokeServiceAction);

        return channelContext;
    }
}
