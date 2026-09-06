/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.manager.secure;

import cn.com.platform.security.adapter.Session;
import cn.com.platform.security.adapter.result.RetAsymDecryptParameter;
import com.dcep.common.enums.MessageTypeEnum;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.kmsapi.exception.KmsException;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.sign.client.SignClient;
import mockit.Capturing;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class HeaderTransferEncryptionHelperImplTest {

    @Tested
    HeaderTransferEncryptionHelperImpl helper;

    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Capturing
    Session kmsApi;

    @Capturing
    RetAsymDecryptParameter parameter;

    @Mocked
    SignClient signClient;

    @Test
    public void encrypt0() {
        EnvelopeDTO dto = envelopeDTO911();
        dto.getSoapHeader().setDgtlEnvlp("02|slfdjlsjfdlkjljl");

        helper.encryptInit(dto);
        new Expectations() {{
            infoCacheUtils.isOrgUpdate(anyString);
            result = true;
        }};

        new Expectations() {{
            signClient.getSession();
            result = kmsApi;
        }};
        helper.encrypt("");
    }


    @Test
    public void encrypt1() {
        EnvelopeDTO dto = envelopeDTO911();
        dto.getSoapHeader().setDgtlEnvlp("slfdjlsjfdlkjljl");

        helper.encryptInit(dto);
        new Expectations() {{
            infoCacheUtils.isOrgUpdate(anyString);
            result = true;
        }};

        new Expectations() {{
            signClient.getSession();
            result = kmsApi;

            parameter.getPlaintext();
            result = "02|abcdefg".getBytes();
        }};
        helper.encrypt("");
    }


    @Test
    public void encrypt2() {
        EnvelopeDTO dto = envelopeDTO911();
        dto.getSoapHeader().setDgtlEnvlp("02|slfdjlsjfdlkjljl");

        helper.encryptInit(dto);
        new Expectations() {{
            infoCacheUtils.isOrgUpdate(anyString);
            result = false;
        }};

        new Expectations() {{
            signClient.getSession();
            result = kmsApi;
        }};
        helper.encrypt("");
    }


    @Test
    public void encrypt3() {
        EnvelopeDTO dto = envelopeDTO911();
        dto.getSoapHeader().setDgtlEnvlp("slfdjlsjfdlkjljl");

        helper.encryptInit(dto);
        new Expectations() {{
            infoCacheUtils.isOrgUpdate(anyString);
            result = false;
        }};

        helper.encrypt("");
    }

    @Test
    public void encrypt4() {
        EnvelopeDTO dto = envelopeDTO911();
        dto.getSoapHeader().setDgtlEnvlp("slfdjlsjfdlkjljl");

        helper.encryptInit(dto);

        new MockUp<BodyTransferEncryptionHelperImpl>() {
            @Mock
            public String encrypt(String s) throws KmsException {
                throw new KmsException();
            }
        };

        try {
            helper.encrypt("");
        } catch (Exception e) {

        }
    }

    private EnvelopeDTO envelopeDTO911() {
        EnvelopeDTO<Dcep91100101DTO> dto = new EnvelopeDTO<>();

        String msgId = TestMsgUtils.getMsgId("911");
        SoapHeader soapHeader = new SoapHeader("01", DcepDateUtils.getDcepDateStrNow(),
            MessageTypeEnum.DCEP_911_001_01.getCode(), msgId + "0000", "00000000000000", "C1010211000012");
        soapHeader.setNcrptnSN("4");
        soapHeader.setSignSN("1");

        SoapBody<Dcep91100101DTO> soapBody = new SoapBody<>();
        Dcep91100101DTO fault = new Dcep91100101DTO();
        fault.setFaultcode("DCEPS9999");
        fault.setFaultstring("未知错误");
        fault.setFaultactor("C1010211000012");

        soapBody.setT(fault);

        dto.setSoapHeader(soapHeader);
        dto.setSoapBody(soapBody);
        return dto;
    }
}