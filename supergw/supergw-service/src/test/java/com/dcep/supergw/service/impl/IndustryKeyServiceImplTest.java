/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.service.impl;

import com.crypto.CryptoAdapter;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.supergw.common.utils.IndustryKeyUtils;
import com.dcep.supergw.dto.dc961.CertInf;
import com.dcep.supergw.dto.dc961.Dcep96100101DTO;
import com.dcep.supergw.dto.dc961.IndstryInf;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class IndustryKeyServiceImplTest {

    @Mocked
    private IndustryKeyUtils industryKeyUtils;

    @Test
    public void test_industry_key_service_succ() throws Exception {
        new Expectations() {
            {
                IndustryKeyUtils.create(anyString, (CryptoAdapter) any, anyString, anyString, anyString, anyString,
                    (byte[]) any);
                result = "hello world".getBytes();
            }
        };

        EnvelopeDTO<GwDTO> request = new EnvelopeDTO<GwDTO>();

        GrpHdr grpHdr = new GrpHdr(
            MsgIdUtil.genMsgIdByOriMsgId(MsgIdUtil.randomMsgId("961", "001", "0"),
                Dcep96100101DTO.class.getAnnotation(Gateway.class).msgTp().substring(5, 8)),
            DcepDateUtils.getDcepDateStrNow(), "C1010311000014", "G4001011000013", "");

        CertInf certInf = new CertInf();
        certInf.setNcrptnAlgoTp("SM2withSM3");
        certInf.setNcrptnCert("abc");

        IndstryInf indstryInf = new IndstryInf();
        indstryInf.setIndstryId("01");
        indstryInf.setIndstryXtndFld("0111");
        indstryInf.setInstnFctrId("0030000001");

        Dcep96100101DTO dcep96100101DTO = new Dcep96100101DTO();
        dcep96100101DTO.setGrpHdr(grpHdr);
        dcep96100101DTO.setCertInf(certInf);
        dcep96100101DTO.setIndstryInf(indstryInf);

        SoapHeader soapHeader = new SoapHeader("01",
            DcepDateUtils.getDcepDateStrNow(),
            Dcep96100101DTO.class.getAnnotation(Gateway.class).msgTp(),
            grpHdr.getMsgId() + "0000",
            "C1010311000014",
            "G4001011000013");

        request.setSoapHeader(soapHeader);
        request.setSoapBody(new SoapBody<>(dcep96100101DTO));

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new IndustryKeyServiceImpl().execute(request);
            }
        });
    }

    @Test
    public void test_industry_key_service_fail() throws Exception {
        new Expectations() {
            {
                IndustryKeyUtils.create(anyString, (CryptoAdapter) any, anyString, anyString, anyString, anyString,
                    (byte[]) any);
                result = new RuntimeException("mock creating error");
            }
        };

        EnvelopeDTO<GwDTO> request = new EnvelopeDTO<GwDTO>();

        GrpHdr grpHdr = new GrpHdr(
            MsgIdUtil.genMsgIdByOriMsgId(MsgIdUtil.randomMsgId("961", "001", "0"),
                Dcep96100101DTO.class.getAnnotation(Gateway.class).msgTp().substring(5, 8)),
            DcepDateUtils.getDcepDateStrNow(), "C1010311000014", "G4001011000013", "");

        CertInf certInf = new CertInf();
        certInf.setNcrptnAlgoTp("SM2withSM3");
        certInf.setNcrptnCert("abc");

        IndstryInf indstryInf = new IndstryInf();
        indstryInf.setIndstryId("01");
        indstryInf.setIndstryXtndFld("0111");
        indstryInf.setInstnFctrId("0030000001");

        Dcep96100101DTO dcep96100101DTO = new Dcep96100101DTO();
        dcep96100101DTO.setGrpHdr(grpHdr);
        dcep96100101DTO.setCertInf(certInf);
        dcep96100101DTO.setIndstryInf(indstryInf);

        SoapHeader soapHeader = new SoapHeader("01",
            DcepDateUtils.getDcepDateStrNow(),
            Dcep96100101DTO.class.getAnnotation(Gateway.class).msgTp(),
            grpHdr.getMsgId() + "0000",
            "C1010311000014",
            "G4001011000013");

        request.setSoapHeader(soapHeader);
        request.setSoapBody(new SoapBody<>(dcep96100101DTO));

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                new IndustryKeyServiceImpl().execute(request);
            }
        });
    }
}
