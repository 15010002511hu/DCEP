package com.dcep.supergw.common.utils;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.MessageTypeEnum;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.InstdPty;
import com.dcep.common.model.soap.InstgPty;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.infocache.manager.NacosConsume;
import com.dcep.supergw.common.config.DtoMappingConfig;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.dto.dc401.Dcep40100101DTO;
import com.dcep.supergw.dto.dc401.FreeFrmtInf;
import com.dcep.supergw.dto.dc433.Dcep43300101DTO;
import com.dcep.supergw.manager.secure.HardEncryptionHelperImpl;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import javax.validation.ConstraintViolationException;
import mockit.Capturing;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;

/**
 * @author : maxinyu
 * @version : SoapUtilsTest.java v 0.1 2019-12-06
 * @description :
 */
@SpringBootTest
@RunWith(JMockit.class)
public class SoapUtilsTest {

    private String XML433 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<!-- Created with Liquid Studio 2019 (https://www.liquid-technologies.com) -->\n" + "<soap:Body>\n"
        + "<BankAttAcctReq>\n" + "    <GrpHdr>\n" + "                <MsgId>{{ID_433}}</MsgId>\n"
        + "                <!--报文标识号、最后一位必须是0、1、2、3-->\n" + "                <CreDtTm>{{DATE}}</CreDtTm>\n"
        + "                <InstgPty>\n" + "                    <InstgDrctPty>{{Sender}}</InstgDrctPty>\n"
        + "                </InstgPty>\n" + "                <InstdPty>\n"
        + "                    <InstdDrctPty>C1010311000014</InstdDrctPty>\n" + "                </InstdPty>\n"
        + "                <Rmk>备注</Rmk>\n" + "            </GrpHdr>\n" + "            <MgmtTp>MT01</MgmtTp>\n"
        + "            <PtcInf>\n" + "                <PtcId>1234</PtcId>\n" + "                <!--挂接协议号-->\n"
        + "                <MsgSndCd>123456789</MsgSndCd>\n" + "                <!--动态关联码-->\n"
        + "                <MsgVrfy>1234</MsgVrfy>\n" + "                <!--动态验证码-->\n" + "            </PtcInf>\n"
        + "            <SgnInf>\n" + "                <SgnAcctPtyId>C1010511003703</SgnAcctPtyId>\n"
        + "                <!--签约人银行账户所属运营机构-->\n" + "                <SgnAcctTp>PT01</SgnAcctTp>\n"
        + "                <!--签约人银行账户类型-->\n" + "                <SgnAcctId>62000000000000001</SgnAcctId>\n"
        + "                <!--签约人银行账户账号-->\n" + "                <SgnAcctNm>账户户名</SgnAcctNm>\n"
        + "                <!--签约人银行账户户名-->\n" + "                <IDTp>IT01</IDTp>\n"
        + "                <!--签约人证件类型-->\n" + "                <IDNo>111111111111111111</IDNo>\n"
        + "                <!--签约人证件号码-->\n" + "                <Tel>17462764586</Tel>\n"
        + "                <!--银行预留手机号码-->\n" + "            </SgnInf>\n" + "            <WltInf>\n"
        + "                <WltPtyId>C1010311000014</WltPtyId>\n" + "                <!--钱包开立所属运营机构编码-->\n"
        + "                <WltId>0021000000122281</WltId>\n" + "                <!--钱包ID-->\n"
        + "                <WltTp>WT01</WltTp>\n" + "                <!--钱包类型-->\n"
        + "                <WltLvl>WL01</WltLvl>\n" + "                <!--钱包等级-->\n" + "            </WltInf>\n"
        + "        </BankAttAcctReq>\n" + "\t\t</soap:Body>";

    private String XML900 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
        + "<!-- Created with Liquid Studio 2019 (https://www.liquid-technologies.com) -->\n" + "<soap:Body>\n"
        + "<CmonConf>\n" + "    <GrpHdr>\n" + "        <MsgId>xx</MsgId>\n" + "        <CreDtTm>xxx</CreDtTm>\n"
        + "        <InstgPty>\n" + "            <InstgDrctPty>C1010411000013</InstgDrctPty>\n"
        + "        </InstgPty>\n" + "        <InstdPty>\n"
        + "            <InstdDrctPty>C1010511003703</InstdDrctPty>\n" + "        </InstdPty>\n"
        + "        <Rmk>备注</Rmk>\n" + "    </GrpHdr>\n" + "    <OrgnlGrpHdr>\n"
        + "        <OrgnlMsgId>xxx</OrgnlMsgId>\n" + "        <OrgnlInstgPty>C1010511003703</OrgnlInstgPty>\n"
        + "        <OrgnlMT>dcep.433.001.01</OrgnlMT>\n" + "    </OrgnlGrpHdr>\n" + "    <CmonConfInf>\n"
        + "        <PrcSts>xxx</PrcSts>\n" + "        <PrcCd>xxxx</PrcCd>\n" + "        <RjctInf>xxx</RjctInf>\n"
        + "        <BatchId>xxx</BatchId>\n" + "    </CmonConfInf>\n" + "</CmonConf>\n" + "</soap:Body>";

    private String XML911 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:head=\"http://www.dcep.com/dcep/header/\">\n"
        + "    <soap:Header>\n" + "        <head:Ver>01</head:Ver>\n"
        + "        <head:SndDtTm>2020-03-02T16:26:20</head:SndDtTm>\n"
        + "        <head:MsgTp>dcep.911.001.01</head:MsgTp>\n"
        + "        <head:MsgSN>202003020051433813566562302120000001</head:MsgSN>\n"
        + "        <head:Sender>G4001011000013</head:Sender>\n"
        + "        <head:Receiver>C1010511003703</head:Receiver>\n" + "        <head:SignSN>4</head:SignSN>\n"
        + "    </soap:Header>\n" + "    <soap:Body>\n"
        + "        <wstxns1:Fault xmlns:wstxns1=\"http://www.dcep.com/dcep/91100101/\">\n"
        + "            <faultcode>DCEPS9999</faultcode>\n" + "            <faultstring>其他系统错</faultstring>\n"
        + "            <faultactor>C1010511003703</faultactor>\n"
        + "            <detail>数字信封解密失败,Base64-encoded string must have at least four characters, but length specified was 1</detail>\n"
        + "        </wstxns1:Fault>\n" + "    </soap:Body>\n" + "</soap:Envelope>";

    @Mocked(stubOutClassInitialization = true)
    NacosConsume nacosConsume;

    @Mocked
    Environment environment;

    @Mocked
    ValidateUtils validateUtils;
    @Mocked
    InfoCacheUtils infoCacheUtils;
    @Mocked
    EncryptionToolUtils encryptionToolUtils;
    @Capturing
    DataEncryption dataEncryption;
    @Mocked
    SoapHeaderUtils soapHeaderUtils;
    @Mocked
    EnviromentUtils enviromentUtils;
    @Mocked
    DtoMappingConfig dtoMappingConfig;


    /**
     * 测试构造方法
     */
    @Test
    public void testConstructor() {
        SoapUtils soapUtils = new SoapUtils();
    }

    @Test
    public void getSoapHeaderBeanTest() {
        SoapUtils soapUtils = new SoapUtils();
        soapUtils.getSoapHeaderBean((String) null);
    }

    /**
     * 测试 toDto clz == null
     */
    @Test(expected = GwException.class)
    public void testToDto_ClzNull1() {
        // 首先扫描缓存Dto clss
        DtoMappingConfig.loadDTO();
        // 传入不存在的MessagetType
        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("xxx.000.xxx");
        SoapUtils.toDto(soapHeader, "".getBytes());
    }

    @Test(expected = GwException.class)
    public void testToDto_ClzNull3() {
        // 首先扫描缓存Dto clss
        DtoMappingConfig.loadDTO();
        // 传入不存在的MessagetType
        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.900.001.01");
        new MockUp<XmlUtils>(XmlUtils.class) {
            @Mock
            public <T> T xmlToObject(byte[] xmlMessage, Class<T> clazz) throws Exception {
                throw new Exception();
            }
        };
        new MockUp<DtoMappingConfig>(DtoMappingConfig.class) {
            @Mock
            public Class<?> getClzByMsgTp(String msgTp) {
                return Dcep90000101DTO.class;
            }
        };

        SoapUtils.toDto(soapHeader, "".getBytes());
    }

    /**
     * 测试 toDto clz == null
     */
    @Test(expected = GwException.class)
    public void testToDto_ClzNull2() {
        // 首先扫描缓存Dto clss
        DtoMappingConfig.loadDTO();
        // 传入不存在的MessagetType
        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("xxx.000.xxx");
        try {
            SoapUtils.toDto(soapHeader, "".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }

    }

    /**
     * 测试 toDto clz != null getIsEncrypt() = false 报文非911
     */

    @Test
    public void testToDto_Exception() throws Exception {
        new MockUp<DtoMappingConfig>(DtoMappingConfig.class) {
            @Mock
            public Class<?> getClzByMsgTp(String msgTp) {
                return Dcep90000101DTO.class;
            }
        };

//        new Expectations(NacosConsume.class) {
//            {
//                NacosConsume.getIsEncrypt();
//                result = false;
//            }
//        };

        new Expectations(MessageTypeEnum.class) {
            {
                MessageTypeEnum.DCEP_911_001_01.getCode();
                result = new ConstraintViolationException("", null);
            }
        };

        validateUtilsMock();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.900.001.01");
        SoapUtils.toDto(soapHeader, XML900.getBytes());

    }

    @Test
    public void testToDto_Exception2() throws Exception {
        new MockUp<DtoMappingConfig>(DtoMappingConfig.class) {
            @Mock
            public Class<?> getClzByMsgTp(String msgTp) {
                return Dcep90000101DTO.class;
            }
        };

//        new Expectations(NacosConsume.class) {
//            {
//                NacosConsume.getIsEncrypt();
//                result = false;
//            }
//        };

        new Expectations(MessageTypeEnum.class) {
            {
                MessageTypeEnum.DCEP_911_001_01.getCode();
                result = new GwException("");
            }
        };

        validateUtilsMock();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.900.001.01");
        SoapUtils.toDto(soapHeader, XML900.getBytes());

    }

    @Test
    public void testToDto_Exception3() throws Exception {
        new MockUp<DtoMappingConfig>(DtoMappingConfig.class) {
            @Mock
            public Class<?> getClzByMsgTp(String msgTp) {
                return Dcep90000101DTO.class;
            }
        };

//        new Expectations(NacosConsume.class) {
//            {
//                NacosConsume.getIsEncrypt();
//                result = false;
//            }
//        };

        new Expectations(MessageTypeEnum.class) {
            {
                MessageTypeEnum.DCEP_911_001_01.getCode();
                result = new Exception("");
            }
        };

        validateUtilsMock();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.900.001.01");
        SoapUtils.toDto(soapHeader, XML900.getBytes());

    }


    /**
     * 测试 toDto clz != null getIsEncrypt() = true (envelopeDTO.body() instanceof DataEncryption)false 报文非911
     */
    @Test
    public void testToDto_IsEncryptTrue_NotDataEncryption() throws Exception {
        new MockUp<DtoMappingConfig>(DtoMappingConfig.class) {
            @Mock
            public Class<?> getClzByMsgTp(String msgTp) {
                return Dcep90000101DTO.class;
            }
        };

        new Expectations(NacosConsume.class) {
            {
                NacosConsume.getIsEncrypt();
                result = true;
            }
        };
        validateUtilsMock();

        SoapHeader soapHeader = new SoapHeader();

        soapHeader.setMsgTp("dcep.900.001.01");
        SoapUtils.toDto(soapHeader, XML900.getBytes());

        soapHeader.setDgtlEnvlp("12345");
        SoapUtils.toDto(soapHeader, XML900.getBytes());


    }

    /**
     * 测试toDto clz!=null getIsEncrypt() = true (envelopeDTO.body() instanceof DataEncryption) true
     */
    @Test
    public void testToDto_IsEncryptTrue_DataEncryption() throws Exception {
        new MockUp<DtoMappingConfig>(DtoMappingConfig.class) {
            @Mock
            public Class<?> getClzByMsgTp(String msgTp) {
                return Dcep43300101DTO.class;
            }
        };

        new Expectations(NacosConsume.class) {
            {
                NacosConsume.getIsEncrypt();
                result = true;
            }
        };
        new Expectations() {
            {
                InfoCacheUtils.getPbocInf();
                result = "00";
                EncryptionToolUtils.getEncryptCertId(anyString, anyString);
                result = "CertId";
                EncryptionToolUtils.decryptDgtEnvlp(anyString, anyString);
                result = "key";

            }
        };

        new MockUp<HardEncryptionHelperImpl>() {
            @Mock
            String decrypt(String encData) {
                return "testdecrypt";
            }
        };

        validateUtilsMock();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.433.001.01");
        soapHeader.setNcrptnSN("sn");
        SoapUtils.toDto(soapHeader, XML433.getBytes());
        soapHeader.setDgtlEnvlp("env");
        SoapUtils.toDto(soapHeader, XML433.getBytes());


    }

    @Test
    public void testToDto_IsEncryptTrue_DataEncryption_Exception() throws Exception {
        new MockUp<DtoMappingConfig>(DtoMappingConfig.class) {
            @Mock
            public Class<?> getClzByMsgTp(String msgTp) {
                return Dcep43300101DTO.class;
            }
        };

        new Expectations(NacosConsume.class) {
            {
                NacosConsume.getIsEncrypt();
                result = new Exception();
            }
        };
        new Expectations() {
            {
                InfoCacheUtils.getPbocInf();
                result = "00";
                EncryptionToolUtils.getEncryptCertId(anyString, anyString);
                result = "CertId";
                EncryptionToolUtils.decryptDgtEnvlp(anyString, anyString);
                result = "key";

            }
        };

        new MockUp<HardEncryptionHelperImpl>() {
            @Mock
            String decrypt(String encData) {
                return "testdecrypt";
            }
        };

        validateUtilsMock();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.433.001.01");
        soapHeader.setNcrptnSN("sn");
        SoapUtils.toDto(soapHeader, XML433.getBytes());
        soapHeader.setDgtlEnvlp("env");
        SoapUtils.toDto(soapHeader, XML433.getBytes());


    }

    /**
     * 测试toDto 911报文
     */
    @Test
    public void testToDto_911() {

        new Expectations(DtoMappingConfig.class) {
            {
                dtoMappingConfig.getClzByMsgTp(anyString);
                result = Dcep91100101DTO.class;
            }
        };

        validateUtilsMock();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.911.001.01");
        try {
            SoapUtils.toDto(soapHeader, XML911.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {

        }
    }

    /**
     * 测试 toXml 报文401 getIsEncrypt true 不是DataEncryption
     */
    @Test
    public void testToXml_IsEncryptTrue_NotDataEncryption() {

        new Expectations(NacosConsume.class) {
            {
                nacosConsume.getIsEncrypt();
                result = true;
            }
        };
        validateUtilsMock();

        String msgId = TestMsgUtils.getMsgId("401");

        EnvelopeDTO<Dcep40100101DTO> envelopeDTO = new EnvelopeDTO<>();

        SoapHeader header = new SoapHeader();
        header.setVer("01");
        header.setSndDtTm(DcepDateUtils.getDcepDateStrNow());
        header.setMsgTp("dcep.401.001.01");
        header.setMsgSN(msgId + "0001");
        header.setSender("C1010511003703");
        header.setReceiver("C1010511003703");
        header.setSignSN("3");

        Dcep40100101DTO dto = new Dcep40100101DTO();
        GrpHdr grpHdr = new GrpHdr();

        grpHdr.setMsgId(msgId);
        grpHdr.setCreDtTm(DcepDateUtils.getDcepDateStrNow());
        InstdPty instdPty = new InstdPty("C1010511003703");
        InstgPty instgPty = new InstgPty("C1010511003703");
        grpHdr.setInstdPty(instdPty);
        grpHdr.setInstgPty(instgPty);
        grpHdr.setRmk("test");
        dto.setGrpHdr(grpHdr);

        FreeFrmtInf ff = new FreeFrmtInf("msgCnt");
        dto.setFreeFrmtInf(ff);

        envelopeDTO.setSoapHeader(header);
        SoapBody body = new SoapBody(dto);
        envelopeDTO.setSoapBody(body);

        validateUtilsMock();

        System.out.println(SoapUtils.toXml(envelopeDTO));
    }

    /**
     * 测试 toXml 报文433 getIsEncrypt true 是DataEncryption
     */
    @Test
    public void testToXml_IsEncryptTrue_DataEncryption() {
        new Expectations() {
            {
                nacosConsume.getIsEncrypt();
                result = true;
            }
        };

        EnvelopeDTO<Dcep43300101DTO> envelopeDTO = new EnvelopeDTO<>();

        SoapHeader header = new SoapHeader();
        header.setVer("01");
        header.setSndDtTm(DcepDateUtils.getDcepDateStrNow());
        header.setMsgTp("dcep.433.001.01");
        header.setMsgSN("4330001");
        header.setSender("C1010511003703");
        header.setReceiver("C1010511003703");
        header.setSignSN("3");

        Dcep43300101DTO dcep43300101DTO = new Dcep43300101DTO();
        dcep43300101DTO.setMgmtTp("1111");
        envelopeDTO.setSoapHeader(header);
        envelopeDTO.setSoapBody(new SoapBody(dcep43300101DTO));

        validateUtilsMock();

        new MockUp<DataEncryption>(DataEncryption.class) {
            @Mock
            public void encryptData(EncryptionHelper encryptionHelper) throws Exception {
                throw new Exception();
            }
        };
        System.out.println(SoapUtils.toXml(envelopeDTO));
    }

    @Test
    public void testToXml_IsEncryptTrue_DataEncryption_Exception() {
        new Expectations() {
            {
                nacosConsume.getIsEncrypt();
                result = new Exception();
            }
        };

        EnvelopeDTO<Dcep43300101DTO> envelopeDTO = new EnvelopeDTO<>();

        SoapHeader header = new SoapHeader();
        header.setVer("01");
        header.setSndDtTm(DcepDateUtils.getDcepDateStrNow());
        header.setMsgTp("dcep.433.001.01");
        header.setMsgSN("4330001");
        header.setSender("C1010511003703");
        header.setReceiver("C1010511003703");
        header.setSignSN("3");

        Dcep43300101DTO dcep43300101DTO = new Dcep43300101DTO();
        dcep43300101DTO.setMgmtTp("1111");
        envelopeDTO.setSoapHeader(header);
        envelopeDTO.setSoapBody(new SoapBody(dcep43300101DTO));

        validateUtilsMock();

        new MockUp<DataEncryption>(DataEncryption.class) {
            @Mock
            public void encryptData(EncryptionHelper encryptionHelper) throws Exception {
                throw new Exception();
            }
        };
        System.out.println(SoapUtils.toXml(envelopeDTO));
    }

    /**
     * 报文911 toXml
     */
    @Test
    public void testToXml_911() {
        EnvelopeDTO<Dcep91100101DTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader header = new SoapHeader();
        header.setVer("01");
        header.setSndDtTm(DcepDateUtils.getDcepDateStrNow());
        header.setMsgTp("dcep.911.001.01");
        header.setMsgSN("9110001");
        header.setSender("C1010511003703");
        header.setReceiver("C1010511003703");
        header.setSignSN("3");
        header.setNcrptnSN("Ncr");
        header.setDgtlEnvlp("dgt");

        Dcep91100101DTO dcep91100101DTO = new Dcep91100101DTO();
        dcep91100101DTO.setDetail("test");
        envelopeDTO.setSoapHeader(header);
        envelopeDTO.setSoapBody(new SoapBody(dcep91100101DTO));

        System.out.println(SoapUtils.toXml(envelopeDTO));
    }

    @Test
    public void testToXml_exc() {
        EnvelopeDTO<Dcep91100101DTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader header = new SoapHeader();
        header.setVer("01");
        header.setSndDtTm(DcepDateUtils.getDcepDateStrNow());
        header.setMsgTp("dcep.911.001.01");
        header.setMsgSN("9110001");
        header.setSender("C1010511003703");
        header.setReceiver("C1010511003703");
        header.setSignSN("3");
        header.setNcrptnSN("Ncr");
        header.setDgtlEnvlp("dgt");

        Dcep91100101DTO dcep91100101DTO = new Dcep91100101DTO();
        dcep91100101DTO.setDetail("test");
        envelopeDTO.setSoapHeader(header);
        envelopeDTO.setSoapBody(new SoapBody(dcep91100101DTO));
        new MockUp<XmlUtils>(XmlUtils.class) {
            @Mock
            public <T> String objectToXml(T object) throws Exception {
                throw new Exception();
            }
        };
        System.out.println(SoapUtils.toXml(envelopeDTO));
    }


    private void validateUtilsMock() {
        new MockUp<ValidateUtils>(ValidateUtils.class) {
            @Mock
            public <T> void validate(T obj) {

            }

            @Mock
            public <T extends GwDTO> void check(EnvelopeDTO<T> envelopeDTO) {

            }
        };
    }

    @Test
    public void test_SubBytes_fail1() {
        SoapUtils.subBytes("123".getBytes(), 9, 1);
    }

    @Test
    public void test_IndexOf_fail1() {
        SoapUtils.indexOf("123".getBytes(), -9, "3".getBytes());
        SoapUtils.indexOf("123".getBytes(), -9, "34".getBytes());
    }

    @Test
    public void test_Match_fail1() {
        SoapUtils.match("123".getBytes(), 0, "4123".getBytes());
        SoapUtils.match("123".getBytes(), 0, "123".getBytes());
        SoapUtils.match("123".getBytes(), 0, "123456".getBytes());
    }

    @Test
    public void test_lastIndexOf_fail1() {
        SoapUtils.lastIndexOf("123".getBytes(), 0, "3".getBytes());
        SoapUtils.lastIndexOf("123".getBytes(), 9, "34".getBytes());
    }

    @Test
    public void test_getBodyBytes() {
        try {
            Method method = SoapUtils.class.getDeclaredMethod("getBodyBytes", byte[].class);
            method.setAccessible(true);
            Object o = method.invoke(SoapUtils.class.newInstance(), "".getBytes());
            Assert.assertEquals(null, o);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testToDtoString1_911() throws UnsupportedEncodingException {

        new Expectations(DtoMappingConfig.class) {
            {
                dtoMappingConfig.getClzByMsgTp(anyString);
                result = Dcep91100101DTO.class;
            }
        };
        validateUtilsMock();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.911.001.01");
        SoapUtils.toDto(soapHeader, XML911);


    }

    @Test
    public void testToDtoString2_911() throws UnsupportedEncodingException {

        validateUtilsMock();
        new Expectations(DtoMappingConfig.class) {
            {
                dtoMappingConfig.getClzByMsgTp(anyString);
                result = Dcep91100101DTO.class;
            }
        };

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setMsgTp("dcep.911.001.01");
        SoapUtils.toDto(soapHeader, XML911);


    }
}
