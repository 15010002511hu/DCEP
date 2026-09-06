package com.dcep.supergw.manager.channel.action;

import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.utils.SerializationUtil;
import com.dcep.supergw.core.ChannelContext;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class SerializationActionTest {

    private SerializationAction<String> asyncPost;

    class InClass implements SerializationUtil<String> {

        @Override
        public String serialization(String o) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String deserialization(String context, Class<?> clazz) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public byte[] serialize(String o) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String deserialize(byte[] content, Class<?> clazz) {
            // TODO Auto-generated method stub
            return null;
        }

    }

    @Before
    public void Init() {
        SerializationUtil<String> uti = new InClass();
        asyncPost = new SerializationAction<>(uti);
    }


    @Test(expected = Exception.class)
    public void serializationActionTestDoInvoke1() {

        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.DESERIALIZATION, "abcd");

        asyncPost.doInvoke(context);
    }

    @Test(expected = Exception.class)
    public void serializationActionTestDoCallBack1() {

        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.DESERIALIZATION, "abcd");

        asyncPost.doCallBack(context);
    }


}
