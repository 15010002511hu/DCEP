package com.dcep.supergw.manager.channel;

import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcep.supergw.manager.channel.action.AsyncActionTest;
import com.dcep.supergw.manager.channel.action.CombActionTest;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AsyncChannelTest extends AbstractChannel {

    public AsyncChannelTest() {
        super("AsyncChannelTest");
    }

    @Override
    public AbstractAction getChannel() {
        return ChannelBuilder.getInstance()
            .addAction(new AsyncActionTest("预清算"))
            //.addAction(new AsyncActionTest("报钱柜"))
            //.addAction(new AsyncActionTest("直接清算"))
            //.addAction(new AsyncActionTest("报同业"))
            .addAction(new CombActionTest())
            .addAction(new AsyncActionTest("报清算"))
            .build();
    }

    @Test
    public void test() throws InterruptedException {
        ChannelContext context = new ChannelContext();
        context.setChannel(new AsyncChannelTest());
    }

}
