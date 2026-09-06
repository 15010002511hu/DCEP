package com.dcep.supergw.manager.channel;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.SoapHeaderUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import lombok.extern.slf4j.Slf4j;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JMockit.class)
@Slf4j
public class AbstractChannelTest {

    AbstractChannel channel = new AbstractChannel("unittest") {
        @Override
        public AbstractAction getChannel() {
            return new AbstractAction("unittest") {
                @Override
                public void doInvoke(ChannelContext context) {
                    log.info("execute mock AbstractAction.doInvoke()");
                }

//				@Override
//				public void doException(ChannelContext context, GwException e) {
//					log.info("execute mock AbstractAction.doException()");
//				}

                @Override
                public void doCallBack(ChannelContext context) {
                    log.info("execute mock AbstractAction.doCallBack()");
                }
            };
        }
    };


    AbstractChannel channe3 = new AbstractChannel("unittest") {
        @Override
        public AbstractAction getChannel() {
            return new AbstractAction("unittest") {
                @Override
                public void doInvoke(ChannelContext context) {
                    log.info("execute mock AbstractAction.doInvoke()");
                }

                @Override
                public void doException(ChannelContext context, GwException e) {
                    log.info("execute mock AbstractAction.doException()");
                }

                @Override
                public void doCallBack(ChannelContext context) {
                    log.info("execute mock AbstractAction.doCallBack()");
                }
            };
        }
    };

    @Test
    public void test_asyncHttpsPostAction_doInvoke_succ() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_202());
        AbstractChannel channel2 = channel;
        channel.equals(null);
        channel2.equals(channel);
        channe3.equals(channel);
        channel.invokeAction(context);
        channel.invokeException(context, new GwException());
        channel.invokeCallBack(context);

        context.close();

        channel.invokeAction(context);
        channel.invokeException(context, new GwException());
        channel.invokeCallBack(context);
        new MockUp<SoapHeaderUtils>(SoapHeaderUtils.class) {
            @Mock
            void setPbocSignSn(EnvelopeDTO dto) {

            }
        };
        try {
            channel.getHeadAction().doException(context, new GwException("UNKNOWERROR"));
        } catch (Exception e) {
        }


    }

}
