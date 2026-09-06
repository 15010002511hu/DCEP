package com.dcep.supergw.common.utils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.ValidSequence;
import com.dcep.infocache.manager.NacosConsume;
import com.dcep.kmsapi.exception.KmsException;
import com.dcep.kmsapi.intf.KmsApi;
import com.dcep.supergw.Aplication;
import com.dcep.supergw.common.config.DtoMappingConfig;
import com.dcep.supergw.common.config.NacosConfigClient;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.sign.client.SignClient;
import com.dcepex.trace.support.TraceContext;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import javax.validation.Validator;
import mockit.Deencapsulation;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Aplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(JMockit.class)
public class ValidateUtilsTest {

    @Injectable
    Validator validator = mock(Validator.class);

    @Tested
    ValidateUtils validateUtils;

    @Mocked
    EnviromentUtils enviromentUtils;
    @Mocked
    DtoMappingConfig dtoMappingConfig;

    @Mocked
    TraceContext traceContext;

    @Mocked
    NacosConfigClient nacosConfigClient;

    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Test
    public void test_null_object() {
        validateUtils = new ValidateUtils();
        EnvelopeDTO dto = TestDtoUtils.envelopeDTO911();
        SoapHeader soapHeader = dto.getSoapHeader();
        new Expectations() {
            {
                Deencapsulation.setField(validateUtils, "validator", validator);
            }
        };
        when(validator.validate(soapHeader, ValidSequence.class)).thenReturn(null);
        ValidateUtils.validate(soapHeader);
    }

    @Test
    public void test_notnull_object() {
//		validateUtils = new ValidateUtils();
//		EnvelopeDTO dto = TestDtoUtils.envelopeDTO911();
//		SoapHeader soapHeader = dto.getSoapHeader();
//		new Expectations() {
//			{
//				Deencapsulation.setField(validateUtils, "validator", validator);
//			}
//		};
//		Set<ConstraintViolation<T> 
//		when(validator.validate(soapHeader, ValidSequence.class)).thenReturn(null);
        EnvelopeDTO dto = TestDtoUtils.envelopeDTO911();
        //SoapHeader soapHeader = dto.getSoapHeader();
        ValidateUtils.validate(dto);
    }

    @Test
    public void test_validateUtils_validateMsg_validsndrcv_succ() throws NoSuchFieldException, SecurityException,
        IllegalArgumentException, IllegalAccessException {
        mockNacosConsumeReturnTrue();

        mockAdvanceSignClientp1VerifyReturnTrue();

        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public boolean checkInst(String instId) {
                return true;
            }

        };

        SoapHeader header = new SoapHeader();
        header.setSender("Z2004944000010");
        header.setReceiver("Z2004944000010");
        header.setMsgSN("201910090111201000000007598893700001");
        header.setMsgTp("dcep.201.001.01");
        header.setSignSN("signsn");
        header.setSndDtTm("1900-01-01T00:00:00");
        header.setVer("01");

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = null;
            }
        };

        new Expectations() {
            {
                NacosConfigClient.getInstance();
                result = nacosConfigClient;

                nacosConfigClient.getEnvFlag();
                result = "1";
            }
        };

        new Expectations() {
            {
                InfoCacheUtils.checkInstLEI(anyString, anyString);
                result = true;

                InfoCacheUtils.checkInst(anyString);
                result = true;
            }
        };

        ValidateUtils.validateMsg(header, "1234", "1234");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_validateMsg_validsndrcv_fail()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        mockNacosConsumeReturnTrue();

        mockAdvanceSignClientp1VerifyReturnTrue();

        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public boolean checkInst(String instId) {
                return true;
            }

        };

        SoapHeader header = new SoapHeader();
        header.setSender("Z2004944000010");
        header.setReceiver("Z2004944000011");
        header.setMsgSN("201910090111201000000007598893700001");
        header.setMsgTp("dcep.201.001.01");
        header.setSignSN("signsn");
        header.setSndDtTm("1900-01-01T00:00:00");
        header.setVer("01");

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = null;
            }
        };

        new Expectations() {
            {
                NacosConfigClient.getInstance();
                result = nacosConfigClient;

                nacosConfigClient.getEnvFlag();
                result = "1";
            }
        };

        new Expectations() {
            {
                InfoCacheUtils.checkInstLEI("Z2004944000010", anyString);
                result = false;
            }
        };

        ValidateUtils.validateMsg(header, "1234", "1234");

        new Expectations() {
            {
                InfoCacheUtils.checkInst(anyString);
                result = false;
            }
        };

        ValidateUtils.validateMsg(header, "1234", "1234");

        new Expectations() {
            {
                InfoCacheUtils.checkInstLEI("Z2004944000011", anyString);
                result = true;
            }
        };

        ValidateUtils.validateMsg(header, "1234", "1234");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_validateMsg_validsnd_fail()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        mockNacosConsumeReturnTrue();

        SoapHeader header = new SoapHeader();
        header.setSender("G1010511003703");
        header.setReceiver("C1010211000012");
        header.setMsgSN("201910090111201000000007598893700001");
        header.setMsgTp("dcep.201.001.01");
        header.setSignSN("signsn");
        header.setSndDtTm("1900-01-01T00:00:00");
        header.setVer("01");

        new MockUp<String>(String.class) {
            @Mock
            public byte[] getBytes(String charsetName)
                throws UnsupportedEncodingException {
                throw new UnsupportedEncodingException();
            }

        };

        ValidateUtils.validateMsg(header, "1234", "1234");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_validateMsg_validrcv_fail()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        mockNacosConsumeReturnTrue();

        SoapHeader header = new SoapHeader();
        header.setSender("C1010411000013");
        header.setReceiver("G1010211000012");
        header.setMsgSN("201910090111201000000007598893700001");
        header.setMsgTp("dcep.201.001.01");
        header.setSignSN("signsn");
        header.setSndDtTm("1900-01-01T00:00:00");
        header.setVer("01");

        new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
            @Mock
            public boolean checkInst(String instId) {

                return instId.equalsIgnoreCase("C1010411000013");
            }

        };
        ValidateUtils.validateMsg(header, "1234", "1234");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_validateMsg_validCertId_fail()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        mockNacosConsumeReturnTrue();

        SoapHeader header = new SoapHeader();
        header.setSender("C1010411000013");
        header.setReceiver("C1010211000012");
        header.setMsgSN("201910090111201000000007598893700001");
        header.setMsgTp("dcep.201.001.01");
        header.setSignSN("signsn");
        header.setSndDtTm("1900-01-01T00:00:00");
        header.setVer("01");

        new MockUp<ValidateUtils>(ValidateUtils.class) {
            @Mock
            public String getCertId(String fiCode, String signSN) {
                return null;
            }
        };

        ValidateUtils.validateMsg(header, "1234", "1234");
    }

    @Test
    public void test_validateUtils_verifySign_skip_succ()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        mockNacosConsumeReturnFalse();
        ValidateUtils.verifySign("", "", "");
        ValidateUtils.verifySign("", "1234".getBytes(), "1234");
    }

    @Test
    public void test_validateUtils_verifySign_succ() throws NoSuchFieldException, SecurityException,
        IllegalArgumentException, IllegalAccessException {

        mockNacosConsumeReturnTrue();

        mockAdvanceSignClientp1VerifyReturnTrue();
        ValidateUtils.verifySign(
            "MEUCIQDLua70Sd2wtgmXeSpGt22Rk5InETrHBMF84JYquY+w4AIgKxk04Ed8Ie6DkyXoVrC+8FwrEmndW4V5EkgwdeCIlw4=", "",
            "");
        ValidateUtils.verifySign(
            "MEUCIQDLua70Sd2wtgmXeSpGt22Rk5InETrHBMF84JYquY+w4AIgKxk04Ed8Ie6DkyXoVrC+8FwrEmndW4V5EkgwdeCIlw4=",
            "".getBytes(), "");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_verifySign_fail() throws NoSuchFieldException, SecurityException,
        IllegalArgumentException, IllegalAccessException {

        mockNacosConsumeReturnTrue();

        mockAdvanceSignClientp1VerifyReturnFalse();

        ValidateUtils.verifySign("", "", "");
        ValidateUtils.verifySign("", "".getBytes(), "");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_verifySign_fail2()
        throws NoSuchFieldException, IllegalAccessException {
        mockNacosConsumeReturnTrue();

        mockAdvanceSignClientp1VerifyReturnFail();

        ValidateUtils.verifySign(
            "MEUCIQDLua70Sd2wtgmXeSpGt22Rk5InETrHBMF84JYquY+w4AIgKxk04Ed8Ie6DkyXoVrC+8FwrEmndW4V5EkgwdeCIlw4=", "",
            "");
        ValidateUtils.verifySign(
            "MEUCIQDLua70Sd2wtgmXeSpGt22Rk5InETrHBMF84JYquY+w4AIgKxk04Ed8Ie6DkyXoVrC+8FwrEmndW4V5EkgwdeCIlw4=",
            "".getBytes(), "");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_verifySign_fail3()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        mockNacosConsumeReturnTrue();

        new MockUp<String>(String.class) {
            @Mock
            public byte[] getBytes(String charsetName)
                throws UnsupportedEncodingException {
                throw new UnsupportedEncodingException();
            }

        };

        ValidateUtils.verifySign(
            "MEUCIQDLua70Sd2wtgmXeSpGt22Rk5InETrHBMF84JYquY+w4AIgKxk04Ed8Ie6DkyXoVrC+8FwrEmndW4V5EkgwdeCIlw4=", "",
            "");
    }


    @Test
    public void test_validateUtils_skip_succ()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        mockNacosConsumeReturnFalse();

        String sign = ValidateUtils.sign("", "");
        sign = ValidateUtils.sign("".getBytes(), "");
        Assert.assertSame(sign, "");
    }

    @Test
    public void test_validateUtils__succ() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
        IllegalAccessException {

        mockNacosConsumeReturnTrue();

        mockAdvanceSignClientp1SignReturnSucc();

        String signStr = ValidateUtils.sign("mock_KmsApi_p1Sign", "");

        Assert.assertEquals(signStr, "mock_KmsApi_p1Sign");
    }

    @Test(expected = GwException.class)
    public void test_validateUtils_fail() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
        IllegalAccessException {

        mockNacosConsumeReturnTrue();

        mockAdvanceSignClientp1SignReturnFail();

        ValidateUtils.sign("", "");
    }

    private static void mockNacosConsumeReturnTrue()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<NacosConsume> NacosConsumeClass = NacosConsume.class;
        Field isSignField = NacosConsumeClass.getDeclaredField("isSign");
        Field isEncryptField = NacosConsumeClass.getDeclaredField("isEncrypt");
        isSignField.setAccessible(true);
        isEncryptField.setAccessible(true);
        isSignField.set(null, "true");
        isEncryptField.set(null, "true");
    }

    private static void mockNacosConsumeReturnFalse()
        throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Class<NacosConsume> NacosConsumeClass = NacosConsume.class;
        Field isSignField = NacosConsumeClass.getDeclaredField("isSign");
        Field isEncryptField = NacosConsumeClass.getDeclaredField("isEncrypt");
        isSignField.setAccessible(true);
        isEncryptField.setAccessible(true);
        isSignField.set(null, "false");
        isEncryptField.set(null, "false");
    }

    private static void mockAdvanceSignClientp1VerifyReturnTrue() {

        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            boolean p1SignVerify(String arg0, String arg1, byte[] arg2, String arg3) throws KmsException {
                return true;
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };
    }

    private static void mockAdvanceSignClientp1VerifyReturnFalse() {
        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            boolean p1SignVerify(String arg0, String arg1, byte[] arg2, String arg3) throws KmsException {
                return false;
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };
    }

    private static void mockAdvanceSignClientp1VerifyReturnFail() {
        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            boolean p1SignVerify(String arg0, String arg1, byte[] arg2, String arg3) throws KmsException {
                throw new KmsException();
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };
    }

    private static void mockAdvanceSignClientp1SignReturnSucc() {
        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            String p1Sign(String arg0, byte[] arg1, String arg2) throws KmsException {
                return "mock_KmsApi_p1Sign";
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };
    }

    private static void mockAdvanceSignClientp1SignReturnFail() {
        MockUp<KmsApi> api = new MockUp<KmsApi>(KmsApi.class) {
            @Mock
            String p1Sign(String arg0, byte[] arg1, String arg2) throws KmsException {
                throw new KmsException();
            }
        };

        new MockUp<SignClient>(SignClient.class) {
            @Mock
            public KmsApi getInstance() {
                return api.getMockInstance();
            }
        };

    }

    @Test(expected = GwException.class)
    public void checkExceptionTest() {
        EnvelopeDTO dto = mock(EnvelopeDTO.class);
        when(dto.body()).thenThrow(new DcepException());
        validateUtils.check(dto);
    }

    @Test(expected = GwException.class)
    public void checkFalseTest() {
        @SuppressWarnings("rawtypes")
        EnvelopeDTO dto = mock(EnvelopeDTO.class);
        //HttpsClientFactory httpsClientFactory = mock(HttpsClientFactory.class);
        GwDTO gwDTO = mock(GwDTO.class);
        when(dto.body()).thenReturn(gwDTO);
        when(gwDTO.check(any())).thenReturn(false);
        //Mockito.doReturn(false).when(dto.body().check(Mockito.any()));
        validateUtils.check(dto);
    }

}
