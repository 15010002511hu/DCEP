package com.dcep.supergw.common.utils;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.infocache.manager.NacosConsume;
import mockit.Delegate;
import mockit.Expectations;
import mockit.Invocation;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author nieyanping
 * @version SoapHeaderUtilsTest.java, v 0.1, 2020/1/13 14:05
 * @description TODO
 */
@RunWith(JMockit.class)
public class SoapHeaderUtilsTest {

    @Mocked
    NacosConsume nacosConsume;
    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Test
    public void test_setSender_succ() {
        ValidateUtilsMocker.mock();
        EnvelopeDTO dto = TestDtoUtils.envelopeDTO911();
        SoapHeaderUtils.setSender(dto);
    }

    @Test
    public void test_setPbocSignSn_succ() {
        ValidateUtilsMocker.mock();
        EnvelopeDTO dto = TestDtoUtils.envelopeDTO911();
        SoapHeaderUtils.setPbocSignSn(dto);
    }

    @Test
    public void test_setNcrptnSn_succ()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ValidateUtilsMocker.mock();
        EnvelopeDTO dto911 = TestDtoUtils.envelopeDTO911();
        SoapHeaderUtils.setNcrptnSn(dto911);

        dto911.getSoapHeader().setMsgTp("dcep.711.001.01");
        SoapHeaderUtils.setNcrptnSn(dto911);

        EnvelopeDTO dto415 = TestDtoUtils.envelopeDTO415();
        SoapHeaderUtils.setNcrptnSn(dto415);

        dto415.getSoapHeader().setNcrptnSN("4");
        SoapHeaderUtils.setNcrptnSn(dto415);
    }

    //    private static void mockNacosConsumeReturnTrue() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
//		Class<NacosConsume> NacosConsumeClass = NacosConsume.class;
//		Field isSignField = NacosConsumeClass.getDeclaredField("isSign");
//		Field isEncryptField = NacosConsumeClass.getDeclaredField("isEncrypt");
//		isSignField.setAccessible(true);
//		isEncryptField.setAccessible(true);
//		isSignField.set(null, "true");
//		isEncryptField.set(null, "true");
//	}
    @Test
    public void test_setDgtlEnvlp_succ()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        ValidateUtilsMocker.mock();

        EnvelopeDTO dto911 = TestDtoUtils.envelopeDTO911();
        SoapHeaderUtils.setDgtlEnvlp(dto911, "test");
        //Mock 返回值
        new Expectations() {
            {
                NacosConsume.getIsSign();
                result = true;
                NacosConsume.getIsEncrypt();
                result = true;
            }
        };
        SoapHeaderUtils.setDgtlEnvlp(dto911, "test");
        dto911.getSoapHeader().setDgtlEnvlp("dcep.711.001.01");
        SoapHeaderUtils.setDgtlEnvlp(dto911, "test");

        EnvelopeDTO dto415 = TestDtoUtils.envelopeDTO415();
        SoapHeaderUtils.setDgtlEnvlp(dto415, "415");

        dto415.getSoapHeader().setDgtlEnvlp("dasjkf");
        SoapHeaderUtils.setDgtlEnvlp(dto415, "test");
    }

    @Test
    public void test_resetPbocDto() {
        ValidateUtilsMocker.mock();
        new MockUp<NacosConsume>(NacosConsume.class) {
            @Mock
            public boolean getIsEncrypt() {
                return true;
            }
        };

        EnvelopeDTO dto911 = TestDtoUtils.envelopeDTO911();
        SoapHeaderUtils.resetPbocDto(dto911, null);
    }

    public void mockDtoWithDigt() {

    }

    /**
     * 测试分支setNcrptnSn dto为711
     */
    @Test
    public void setNcrptnSn_Dto711() {
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        SoapHeader header = new SoapHeader();
        header.setVer("01");
        header.setSender("00000000000000");
        header.setReceiver("00000000000001");
        header.setMsgSN("000000000000000000000000000000000000");
        header.setMsgTp("dcep.711.001.01");
        header.setDgtlEnvlp("adaf");
        header.setNcrptnSN("adaf");
        header.setSignSN("4");
        header.setSndDtTm("2020-03-26T00:00;00");
        envelopeDTO.setSoapHeader(header);

        SoapHeaderUtils.setNcrptnSn(envelopeDTO);
    }

    /**
     * 测试分支 setDgtlEnvlp dtow为 711 getIsSign 为 true
     */
    @Test
    public void testSetDgtlEnvlp_Dto711_IsSignTrue(@Mocked NacosConsume nacosConsume,
        @Mocked EncryptionToolUtils encryptionToolUtils) {
        new Expectations(NacosConsume.class) {
            {
                NacosConsume.getIsSign();
                result = true;
            }
        };
        new Expectations(EncryptionToolUtils.class) {
            {
                EncryptionToolUtils.getEncryptCertId(anyString, anyString);
                result = new Delegate<EncryptionToolUtils>() {
                    String getEncryptCertId(Invocation inv, String x, String y) {
                        return x + 01 + y + 02;
                    }
                };
            }
        };
        EnvelopeDTO envelopeDTO = new EnvelopeDTO();
        SoapHeader header = new SoapHeader();
        header.setVer("01");
        header.setSender("00000000000000");
        header.setReceiver("00000000000000");
        header.setMsgSN("000000000000000000000000000000000000");
        header.setMsgTp("dcep.711.001.01");
        header.setDgtlEnvlp("adaf");
        header.setNcrptnSN("adaf");
        header.setSignSN("4");
        header.setSndDtTm("2020-03-26T00:00;00");
        envelopeDTO.setSoapHeader(header);

        SoapHeaderUtils.setDgtlEnvlp(envelopeDTO, "");
    }
}