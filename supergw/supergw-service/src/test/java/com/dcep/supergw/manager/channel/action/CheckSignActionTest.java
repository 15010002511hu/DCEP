package com.dcep.supergw.manager.channel.action;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.https.HttpsAsyncClient;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class CheckSignActionTest {

    @Tested
    private CheckSignAction asyncPost;

    @Mocked
    HttpsAsyncClient client;

    @Mocked
    ValidateUtils validateUtils;

    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Test
    public void test_CheckSignAction_doInvoke_succ() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        new MockUp<SoapUtils>(SoapUtils.class) {
            @Mock
            public <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, byte[] xml) {
                return null;
            }
        };
        asyncPost.doInvoke(context);
    }

    @Test
    public void test_CheckSignAction_doException_succ() {
        ChannelContext context = ChannelContextMocker.newChannelContext(TestMsgUtils.dcep_201());
        context.setRequest(false);
        asyncPost.doException(context, new GwException("unittest"));
        context.setRequest(true);
        asyncPost.doException(context, new GwException("unittest"));
        context.setAttachment("respmsg", "123");

        asyncPost.doException(context, new GwException("unittest"));

        context.isRequest();
    }
}
