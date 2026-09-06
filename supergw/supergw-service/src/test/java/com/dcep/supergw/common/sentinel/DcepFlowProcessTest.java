package com.dcep.supergw.common.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.dcep.common.model.soap.SoapHeader;
import java.util.ArrayList;
import java.util.List;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : DcepFlowProcessTest.java v 0.1 2021-04-27
 * @description :
 */
@SpringBootTest
@RunWith(JMockit.class)
public class DcepFlowProcessTest {

    @Tested
    DcepFlowProcess dcepFlowProcess;
    @Mocked
    SphU sphU;
    @Mocked
    Entry entry;

    /**
     * 测试obtainEntry，入参为null
     */
    @Test(expected = Exception.class)
    public void testObtainEntry_param_null() {
        try {
            dcepFlowProcess.obtainEntry(null);
        } catch (BlockException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试obtainEntry，header正常
     */
    @Test
    public void testObtainEntry_normal() {
        SoapHeader header = new SoapHeader("01", "2021-04-19T10:00:00", "dcep.225.001.01",
            "2021041500112251000001111222233334444", "C1010311000014", "C1010411000013");
        new Expectations() {
            {
                try {
                    sphU.entry(anyString);
                    result = entry;
                } catch (BlockException e) {
                }
            }
        };
        try {
            dcepFlowProcess.obtainEntry(header);
        } catch (BlockException e) {
            e.printStackTrace();
        }
    }

    /**
     * 测试obtainEntry，heade.sender为空，header.msgtp为空
     */
    @Test
    public void testObtainEntry_sendermsgtp_blank() {
        SoapHeader header = new SoapHeader("01", "2021-04-19T10:00:00", "", "2021041500112251000001111222233334444", "",
            "C1010411000013");
        new Expectations() {
            {
                try {
                    sphU.entry(anyString);
                    result = entry;
                } catch (BlockException e) {
                }
            }
        };
        try {
            dcepFlowProcess.obtainEntry(header);
        } catch (BlockException e) {
        }
    }

    /**
     * 测试obtainEntry，heade.sender为空，header.msgtp为空
     */
    @Test
    public void testObtainEntry_Exception() {
        SoapHeader header = new SoapHeader("01", "2021-04-19T10:00:00", "", "2021041500112251000001111222233334444", "",
            "C1010411000013");
        new Expectations() {
            {
                try {
                    sphU.entry(anyString);
                    result = new FlowException("dsfdas");
                } catch (BlockException e) {
                }
            }
        };
        try {
            dcepFlowProcess.obtainEntry(header);
        } catch (BlockException e) {
        }
    }

    /**
     * 测试releaseEntry，入参为null
     */
    @Test
    public void testReleaseEntry_list_null() {
        List<Entry> list = new ArrayList<>();
        list.add(entry);

        dcepFlowProcess.releaseEntry(list);

    }

    /**
     * 测试releaseEntry，入参不为null
     */
    @Test
    public void testReleaseEntry_list_notnull() {
        List<Entry> list = null;

        dcepFlowProcess.releaseEntry(list);

    }

}
