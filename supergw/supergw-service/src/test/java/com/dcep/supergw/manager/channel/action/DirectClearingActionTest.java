package com.dcep.supergw.manager.channel.action;

import com.dcep.clearing.dto.dc225.Dcep22500101DTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.dubbo.DynamicInvoker;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : DirectClearingActionTest.java v 0.1 2021-04-25
 * @description :
 */
@SpringBootTest
@RunWith(JMockit.class)
public class DirectClearingActionTest {


    @Tested
    public DirectClearingAction directClearingAction;

    @Mocked
    public ChannelContext context;

    @Mocked
    public DynamicInvoker dynamicInvoker;

    @Mocked
    public Response response;

    /**
     * 分支测试 DTO 为 911,无异常
     */
    @Test
    public void DTOIS911() {
        SoapHeader header = new SoapHeader("01", "2021-04-19T10:00:00", "dcep.911.001.01",
            "2021041500113911000001111222233334444", "C1010311000014", "C1010411000013");
        Dcep91100101DTO dcep91100101DTO = new Dcep91100101DTO("DCEPS9007", "test", "test", "test");
        EnvelopeDTO dto = new EnvelopeDTO();
        dto.setSoapBody(new SoapBody(dcep91100101DTO));
        dto.setSoapHeader(header);

        new Expectations() {
            {
                context.getAttachment(anyString);
                result = dto;
            }
        };
        directClearingAction.doInvoke(context);
    }

    /**
     * 分支测试 DTO 为 225,返回值为null,期望异常
     */
    @Test(expected = GwException.class)
    public void test225_exceptionException() {
        SoapHeader header = new SoapHeader("01", "2021-04-19T10:00:00", "dcep.225.001.01",
            "2021041500112251000001111222233334444", "C1010311000014", "C1010411000013");

        Dcep22500101DTO dcep22500101DTO = new Dcep22500101DTO();
        EnvelopeDTO dto = new EnvelopeDTO();
        dto.setSoapHeader(header);
        dto.setSoapBody(new SoapBody(dcep22500101DTO));

        new Expectations() {
            {
                context.getAttachment(anyString);
                result = dto;
            }

            {
                dynamicInvoker.invokeDubbo(anyString, anyString, dto);
                result = null;
            }
        };

        directClearingAction.doInvoke(context);
    }

    /**
     * 分支测试 DTO 为 225,返回值为不为null,sucess 为 true
     */
    @Test
    public void test225_exceptiontrue() {
        SoapHeader header = new SoapHeader("01", "2021-04-19T10:00:00", "dcep.225.001.01",
            "2021041500112251000001111222233334444", "C1010311000014", "C1010411000013");

        Dcep22500101DTO dcep22500101DTO = new Dcep22500101DTO();
        EnvelopeDTO dto = new EnvelopeDTO();
        dto.setSoapHeader(header);
        dto.setSoapBody(new SoapBody(dcep22500101DTO));

        new Expectations() {
            {
                context.getAttachment(anyString);
                result = dto;
            }

            {
                dynamicInvoker.invokeDubbo(anyString, anyString, dto);
                result = response;
            }

            {
                response.getResult();
                result = dto;
            }

            {
                response.isSuccess();
                result = true;
            }
        };

        directClearingAction.doInvoke(context);
    }


    /**
     * 分支测试 DTO 为 225,返回值为不为null,sucess 为 false
     */
    @Test
    public void test225_exceptionfalse() {
        SoapHeader header = new SoapHeader("01", "2021-04-19T10:00:00", "dcep.225.001.01",
            "2021041500112251000001111222233334444", "C1010311000014", "C1010411000013");

        Dcep22500101DTO dcep22500101DTO = new Dcep22500101DTO();
        EnvelopeDTO dto = new EnvelopeDTO();
        dto.setSoapHeader(header);
        dto.setSoapBody(new SoapBody(dcep22500101DTO));

        new Expectations() {
            {
                context.getAttachment(anyString);
                result = dto;
            }

            {
                dynamicInvoker.invokeDubbo(anyString, anyString, dto);
                result = response;
            }

            {
                response.getResult();
                result = dto;
            }

            {
                response.isSuccess();
                result = false;
            }
        };

        directClearingAction.doInvoke(context);
    }
}
