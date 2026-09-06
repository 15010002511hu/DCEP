package com.dcep.supergw.common.utils;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.Aplication;
import com.dcep.supergw.common.enums.GwErrorEnum;
import java.io.IOException;
import mockit.Expectations;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.util.Assert;

@SpringBootTest(classes = Aplication.class)
@RunWith(JMockit.class)
public class GwMsgUtilsTest {

    private SoapHeader header;
    private GwErrorEnum gwError;


    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Before
    public void init() {
        header = new SoapHeader();
        header.setSender("C1010511003703");
        header.setReceiver("C1010211000012");
        header.setMsgSN("201910090111201000000007598893700001");

        gwError = GwErrorEnum.BUSSINESS_ERROR;
    }

    @Test
    public void test_gwMsgUtils_dcep911_args7_lst512_succ() {

        new Expectations() {
            {
                InfoCacheUtils.getPbocInf();
                result = "G4001011000013";
            }
        };

        GwMsgUtils.dcep911(header, gwError.getCode(), gwError.getDescription(), "1234");
        EnvelopeDTO<Dcep91100101DTO> dcep_911 = GwMsgUtils
            .dcep911(header.getMsgSN(), header.getSender(), header.getReceiver(), gwError.getCode(),
                gwError.getDescription(), header.getSender(), "");
        Assert.notNull(dcep_911, "dcep.911.001.01 generate fail");
    }

    @Test
    public void test_gwMsgUtils_dcep911_args7_grt512_succ() {

        EnvelopeDTO<Dcep91100101DTO> dcep_911 = GwMsgUtils
            .dcep911(header.getMsgSN(), header.getSender(), header.getReceiver(), gwError.getCode(),
                gwError.getDescription(), header.getSender(),
                "unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test unit test ");
        Assert.notNull(dcep_911, "dcep.911.001.01 generate fail");
        dcep_911 = GwMsgUtils.dcep911(header.getMsgSN(), header.getSender(), header.getReceiver(), gwError.getCode(),
            gwError.getDescription(), header.getSender(), null);
    }

    @Test
    public void test_gwMsgUtils_writerXmlToInst_succ() {
        new Expectations() {
            {
                InfoCacheUtils.getPbocInf();
                result = "G4001011000013";
            }

            {
                InfoCacheUtils.getPbocSignCertDnOrNickname(anyString);
                result = "abc";
            }
        };
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        GwMsgUtils.writerXmlToInst(httpServletResponse, "", "abcd");
    }

    @Test(expected = Exception.class)
    public void test_gwMsgUtils_writerXmlToInst_fail() {
        new Expectations() {
            {
                InfoCacheUtils.getPbocInf();
                result = "G4001011000013";
            }

            {
                InfoCacheUtils.getPbocSignCertDnOrNickname(anyString);
                result = new IOException();
            }
        };
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        GwMsgUtils.writerXmlToInst(httpServletResponse, "", "abcd");
    }

    @Test
    public void dcep911Test() {
        new Expectations() {
            {
                InfoCacheUtils.getPbocInf();
                result = "G4001011000013";
            }
        };
        GwMsgUtils.dcep911(header, gwError, null);
        GwMsgUtils.dcep911(header.getMsgSN(), header.getSender(), header.getReceiver(), gwError, "actor", "detail");
    }

}
