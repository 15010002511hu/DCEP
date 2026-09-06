package com.dcep.supergw.common.utils;

import com.dcep.clearing.dto.dc201.Dcep20100101DTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.supergw.dto.dc371.DCEP37100101DTO;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author maxinyu
 * @date 2023/6/20 20:07
 */
@RunWith(JMockit.class)
public class SecretUtilsTest {

    @Tested
    SecretUtils secretUtils;
    @Mocked
    EncryptionHelperHolderTest helperHolder;

    @Test
    public void test() {
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        Dcep20100101DTO dto201 = new Dcep20100101DTO();
        envelopeDTO.setSoapBody(new SoapBody(dto201));
        secretUtils.encrypt(envelopeDTO);
        Dcep91100101DTO dto911 = new Dcep91100101DTO();
        envelopeDTO.setSoapBody(new SoapBody(dto911));
        secretUtils.encrypt(envelopeDTO);
    }


    @Test
    public void test1() {
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        Dcep20100101DTO dto201 = new Dcep20100101DTO();
        envelopeDTO.setSoapBody(new SoapBody(dto201));
        secretUtils.decrypt(envelopeDTO);
        Dcep91100101DTO dto911 = new Dcep91100101DTO();
        envelopeDTO.setSoapBody(new SoapBody(dto911));
        secretUtils.decrypt(envelopeDTO);
    }

    @Test
    public void test2() {
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        DCEP37100101DTO dto371 = new DCEP37100101DTO();
        envelopeDTO.setSoapBody(new SoapBody(dto371));
        secretUtils.transEncrypt(envelopeDTO);
        Dcep91100101DTO dto911 = new Dcep91100101DTO();
        envelopeDTO.setSoapBody(new SoapBody(dto911));
        secretUtils.transEncrypt(envelopeDTO);
    }
}
