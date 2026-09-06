package com.dcep.supergw.common.utils;

import com.dcep.clearing.dto.dc613.Dcep61300101DTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.config.NacosConfigClient;
import com.dcep.supergw.dto.dc371.DCEP37100101DTO;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class EncryptionHelperHolderTest {

    @Tested
    EncryptionHelperHolder helperHolder;

    @Test
    public void test() {
        EnvelopeDTO dto = new EnvelopeDTO();
        SoapHeader header = new SoapHeader();
        header.setDgtlEnvlp("01|sfdasfdas");
        dto.setSoapHeader(header);
        helperHolder.getHelperWhenDecrypt(dto);
        header.setDgtlEnvlp("sfdasfdas");
        dto.setSoapHeader(header);
        helperHolder.getHelperWhenDecrypt(dto);
    }

    @Test
    public void test1(@Mocked InfoCacheUtils infoCacheUtils, @Mocked NacosConfigClient nacosConfigClient) {
        new Expectations() {
            {
                infoCacheUtils.isOrgUpdate(anyString);
                result = true;
            }
        };
        new Expectations() {
            {
                nacosConfigClient.getTotal();
                result = 100;
                nacosConfigClient.getGrayRate();
                result = 50;
            }
        };
        EnvelopeDTO dto = new EnvelopeDTO();
        SoapHeader header = new SoapHeader();
        header.setReceiver("01");
        dto.setSoapHeader(header);
        helperHolder.getHelperWhenEncrypt(dto);


    }

    @Test
    public void test2(@Mocked InfoCacheUtils infoCacheUtils) {
        EnvelopeDTO dto = new EnvelopeDTO();
        SoapHeader header = new SoapHeader();
        header.setReceiver("01");
        dto.setSoapHeader(header);
        helperHolder.getHelperWhenEncrypt(dto);


    }

    @Test
    public void test3() {
        EnvelopeDTO dto = new EnvelopeDTO();
        DCEP37100101DTO dto371 = new DCEP37100101DTO();
        dto.setSoapBody(new SoapBody(dto371));
        helperHolder.getTransferEncryptionHelper(dto);

        Dcep61300101DTO dto613 = new Dcep61300101DTO();
        dto.setSoapBody(new SoapBody(dto613));
        helperHolder.getTransferEncryptionHelper(dto);


    }
}
