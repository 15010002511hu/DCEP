package com.dcep.supergw.manager.channel;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.dcep.supergw.manager.channel.action.AbstractAction;
import com.dcep.supergw.manager.channel.action.JurisdictionAction;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JMockit.class)
public class AbstractChannelTest2 {

    class ChanTest extends AbstractChannel {

        public ChanTest(String name) {
            super(name);

        }

        @Override
        public AbstractAction getChannel() {
            return
                ChannelBuilder.getInstance()
                    //权限校验
                    .addAction(new JurisdictionAction())
                    .build();
        }

    }

    ChanTest c1;
    ChanTest c2;

    @Before
    public void beforeTest() {
        c1 = new ChanTest("1");
        c2 = new ChanTest("2");
    }

    @Test
    public void equalTest() {
        assertFalse(c1.equals(c2));
        assertTrue(c1.equals(c1));
    }

}
