package com.dcep.supergw.manager.channel.action;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.infocache.MessagePermissionCache;
import com.dcep.infocache.api.dto.MessagePermissionDTO;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : JurisdictionActionTest.java v 0.1 2019-12-10
 * @description : 权限校验action测试类
 */
@SpringBootTest
@RunWith(JMockit.class)
public class JurisdictionActionTest {

    private String xml = TestMsgUtils.dcep_401();

    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Before
    public void init() {

        new Expectations() {
            {
                InfoCacheUtils.getPbocInf();
                result = "G4001011000013";
            }
        };

        new MockUp<ChannelContext>(ChannelContext.class) {
            @Mock
            public void fireInvokeAction() {

            }

            @Mock
            public void fireInvokeCallBack() {

            }
        };

        new MockUp<ValidateUtils>(ValidateUtils.class) {
            @Mock
            public <T> void validate(T obj) {

            }

            @Mock
            public void validateMsg(SoapHeader header, String content, String signStr) {

            }
        };
    }

    JurisdictionAction jurisdictionAction = new JurisdictionAction();
    private String sendPbocXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "    <soap:Header>\n" +
        "        <Ver>01</Ver>\n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm>\n" +
        "        <MsgTp>dcep.470.001.01</MsgTp>\n" +
        "        <MsgSN>" + TestMsgUtils.getMsgId("470") + "0001</MsgSN>\n" +
        "        <Sender>G4001011000013</Sender>\n" +
        "        <Receiver>C1010511003703</Receiver>\n" +
        "        <SignSN>3</SignSN>\n" +
        "        <NcrptnSN>3</NcrptnSN>\n" +
        "        <DgtlEnvlp>3</DgtlEnvlp>\n" +
        "    </soap:Header>\n" +
        "    <soap:Body>\n" +
        "        <WltIdQryReq>\n" +
        "            <GrpHdr>\n" +
        "                <MsgId>" + TestMsgUtils.getMsgId("470") + "</MsgId>\n" +
        "                <CreDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</CreDtTm>\n" +
        "                <InstgPty>\n" +
        "                    <InstgDrctPty>G4001011000013</InstgDrctPty>\n" +
        "                </InstgPty>\n" +
        "                <InstdPty>\n" +
        "                    <InstdDrctPty>C1010511003703</InstdDrctPty>\n" +
        "                </InstdPty>\n" +
        "                <Rmk>test</Rmk>\n" +
        "            </GrpHdr>\n" +
        "            <WltInf>\n" +
        "                <WltNm>123131</WltNm>\n" +
        "                <Tel>17751162929</Tel>\n" +
        "                <EmailAdr>1773000@163.com</EmailAdr>\n" +
        "            </WltInf>\n" +
        "        </WltIdQryReq>\n" +
        "    </soap:Body>\n" +
        "</soap:Envelope>";

    private String recPbocXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "    <soap:Header>\n" +
        "        <Ver>01</Ver>\n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm>\n" +
        "        <MsgTp>dcep.470.001.01</MsgTp>\n" +
        "        <MsgSN>" + TestMsgUtils.getMsgId("470") + "0001</MsgSN>\n" +
        "        <Sender>C1010511003703</Sender>\n" +
        "        <Receiver>G4001011000013</Receiver>\n" +
        "        <SignSN>3</SignSN>\n" +
        "        <NcrptnSN>3</NcrptnSN>\n" +
        "        <DgtlEnvlp>3</DgtlEnvlp>\n" +
        "    </soap:Header>\n" +
        "    <soap:Body>\n" +
        "        <WltIdQryReq>\n" +
        "            <GrpHdr>\n" +
        "                <MsgId>" + TestMsgUtils.getMsgId("470") + "</MsgId>\n" +
        "                <CreDtTm>" + TestMsgUtils.getMsgId("470") + "</CreDtTm>\n" +
        "                <InstgPty>\n" +
        "                    <InstgDrctPty>C1010511003703</InstgDrctPty>\n" +
        "                </InstgPty>\n" +
        "                <InstdPty>\n" +
        "                    <InstdDrctPty>G4001011000013</InstdDrctPty>\n" +
        "                </InstdPty>\n" +
        "                <Rmk>test</Rmk>\n" +
        "            </GrpHdr>\n" +
        "            <WltInf>\n" +
        "                <WltNm>wltnm</WltNm>\n" +
        "                <Tel>17751162929</Tel>\n" +
        "                <EmailAdr>1773000@163.com</EmailAdr>\n" +
        "            </WltInf>\n" +
        "        </WltIdQryReq>\n" +
        "    </soap:Body>\n" +
        "</soap:Envelope>";

    //C->A报文
    private String recPbocXml_461 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "    <soap:Header>\n" +
        "        <Ver>01</Ver>\n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm>\n" +
        "        <MsgTp>dcep.461.001.01</MsgTp>\n" +
        "        <MsgSN>" + TestMsgUtils.getMsgId("461") + "0001</MsgSN>\n" +
        "        <Sender>C1010311000014</Sender>\n" +
        "        <Receiver>G4001011000013</Receiver>\n" +
        "        <SignSN>3</SignSN>\n" +
        "        <NcrptnSN>3</NcrptnSN>\n" +
        "        <DgtlEnvlp>3</DgtlEnvlp>\n" +
        "    </soap:Header>\n" +
        "    <soap:Body>\n" +
        "        <ConReq>\n" +
        "            <GrpHdr>\n" +
        "                <MsgId>" + TestMsgUtils.getMsgId("461") + "</MsgId>\n" +
        "                <CreDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</CreDtTm>\n" +
        "                <InstgPty>\n" +
        "                    <InstgDrctPty>C1010311000014</InstgDrctPty>\n" +
        "                </InstgPty>\n" +
        "                <InstdPty>\n" +
        "                    <InstdDrctPty>G4001011000013</InstdDrctPty>\n" +
        "                </InstdPty>\n" +
        "                <Rmk>test</Rmk>\n" +
        "            </GrpHdr>\n" +
        "            <WltCustInfSubNtfctnInf>\n" +
        "                <WltId>0031000000183221</WltId>\n" +
        "                <WltNm>谢三哥2</WltNm>\n" +
        "                <WltLvl>WL01</WltLvl>\n" +
        "                <WltChgTp>CG01</WltChgTp>\n" +
        "                <WltSts>WS05</WltSts>\n" +
        "                <WltIssType>IS01</WltIssType>\n" +
        "                <WltTp>WT01</WltTp>\n" +
        "                <IdTp>IT01</IdTp>\n" +
        "                <IdNo>145625198602311285</IdNo>\n" +
        "                <CustNm>张师弟</CustNm>\n" +
        "                <CustTel>12575239456</CustTel>\n" +
        "                <CustEmail>12575239456@163.com</CustEmail>\n" +
        "                <ExpDt>2020-10-10</ExpDt>\n" +
        "                <Country>CN</Country>\n" +
        "                <CoyNm>光明顶a</CoyNm>\n" +
        "                <UnifSocCdtCd>01234567890123456789</UnifSocCdtCd>\n" +
        "            </WltCustInfSubNtfctnInf>\n" +
        "        </ConReq>\n" +
        "    </soap:Body>\n" +
        "</soap:Envelope>\n";

    private String send_receiver_poc_xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "    <soap:Header>\n" +
        "        <Ver>01</Ver>\n" +
        "        <SndDtTm></SndDtTm>\n" +
        "        <MsgTp>dcep.470.001.01</MsgTp>\n" +
        "        <MsgSN></MsgSN>\n" +
        "        <Sender>G4001011000013</Sender>\n" +
        "        <Receiver>G4001011000013</Receiver>\n" +
        "        <SignSN>3</SignSN>\n" +
        "        <NcrptnSN>3</NcrptnSN>\n" +
        "        <DgtlEnvlp>3</DgtlEnvlp>\n" +
        "    </soap:Header>\n" +
        "    <soap:Body>\n" +
        "        <WltIdQryReq>\n" +
        "            <GrpHdr>\n" +
        "                <MsgId>\"TestMsgUtils.getMsgId(\"470\")\"</MsgId>\n" +
        "                <CreDtTm>\"DcepDateUtils.getDcepDateStrNow()\"</CreDtTm>\n" +
        "                <InstgPty>\n" +
        "                    <InstgDrctPty>G4001011000013</InstgDrctPty>\n" +
        "                </InstgPty>\n" +
        "                <InstdPty>\n" +
        "                    <InstdDrctPty>G4001011000013</InstdDrctPty>\n" +
        "                </InstdPty>\n" +
        "                <Rmk>test</Rmk>\n" +
        "            </GrpHdr>\n" +
        "            <WltInf>\n" +
        "                <WltNm>wltnm</WltNm>\n" +
        "                <Tel>17751162929</Tel>\n" +
        "                <EmailAdr>1773000@163.com</EmailAdr>\n" +
        "            </WltInf>\n" +
        "        </WltIdQryReq>\n" +
        "    </soap:Body>\n" +
        "</soap:Envelope>";

    /**
     * 测试Invoke方法 dto的Sender不为央行机构且机构权限校验通过，Recevier为央行机构
     */
    @Test
    public void testDoInvoke_SendPermission_RecPboc() {
        //public void doInvoke(ChannelContext context)
        new MockUp<MessagePermissionCache>(MessagePermissionCache.class) {

            @Mock
            public MessagePermissionDTO getMessagePermission(String orgId, String messageId) {
                MessagePermissionDTO dto = new MessagePermissionDTO();
                return dto;
            }
        };

        //public void doInvoke(ChannelContext context)
        new MockUp<SoapUtils>(SoapUtils.class) {
            @Mock
            public EnvelopeDTO toDto(SoapHeader header, String xml) {
                EnvelopeDTO envelopeDTO = new EnvelopeDTO();
                envelopeDTO.setSoapHeader(header);
                SoapBody body = new SoapBody();
//                Dcep46100101DTO dcep46100101DTO = new Dcep46100101DTO();
//                dcep46100101DTO.setGrpHdr(new GrpHdr("11111", "2020-02-19T15:00", "C1010311000014", "G4001011000013", "1111"));
//                WltCustInfSubNtfctnInf wltCustInfSubNtfctnInf = new WltCustInfSubNtfctnInf();
//                wltCustInfSubNtfctnInf.setUnifSocCd("1111");
//                wltCustInfSubNtfctnInf.setCoyNm("111");
//                wltCustInfSubNtfctnInf.setCountry("cn");
//                wltCustInfSubNtfctnInf.setExpDt("1111");
//                wltCustInfSubNtfctnInf.setCustEmail("dce@001.com");
//                wltCustInfSubNtfctnInf.setCustTel("12233334444");
//                wltCustInfSubNtfctnInf.setCustNm("qingtian");
//                wltCustInfSubNtfctnInf.setIdNo("1111111");
//                wltCustInfSubNtfctnInf.setIdTp("11111");
//                wltCustInfSubNtfctnInf.setWltTp("001");
//                wltCustInfSubNtfctnInf.setWltIssType("wl01");
//                wltCustInfSubNtfctnInf.setWltLvl("111");
//                wltCustInfSubNtfctnInf.setWltSts("1111");
//                wltCustInfSubNtfctnInf.setWltChgTp("111");
//                body.setT(wltCustInfSubNtfctnInf);
                envelopeDTO.setSoapBody(body);
                return envelopeDTO;
            }
        };

        String signature = "";
        SoapHeader header = SoapUtils.getSoapHeaderBean(recPbocXml_461.getBytes());
        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.SERIALIZATION, recPbocXml_461);
        context.setAttachment(Constant.SIGNATURE, "");
        jurisdictionAction.doInvoke(context);
    }

    /**
     * 测试Invoke方法 dto的Sender不为央行机构且机构权限校验不通过，Recevier为央行机构
     */
    @Test(expected = GwException.class)
    public void testDoInvoke_SendNoPermission_RecPboc() {
        //public void doInvoke(ChannelContext context)
        new MockUp<MessagePermissionCache>(MessagePermissionCache.class) {
            @Mock
            public MessagePermissionCache getInstance() {
                return new MessagePermissionCache();
            }

            @Mock
            public MessagePermissionDTO getMessagePermission(String orgId, String messageId) {
                MessagePermissionDTO dto = new MessagePermissionDTO();
                return dto;
            }
        };

        String signature = "";
        SoapHeader header = SoapUtils.getSoapHeaderBean(recPbocXml_461.getBytes());
        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.SERIALIZATION, recPbocXml_461);
        context.setAttachment(Constant.SIGNATURE, "");
        jurisdictionAction.doInvoke(context);
    }

    /**
     * 测试Invoke方法 dto的Sender为央行机构，Recevier为央行机构
     */
    @Test
    public void testDoInvoke_SendPboc_RecPboc() {
        //public void doInvoke(ChannelContext context)
        String signature = "";
        SoapHeader header = SoapUtils.getSoapHeaderBean(send_receiver_poc_xml.getBytes());
        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.SERIALIZATION, recPbocXml_461);
        context.setAttachment(Constant.SIGNATURE, "");
        jurisdictionAction.doInvoke(context);
    }

    /**
     * 测试Invoke方法 dto的Sender为央行机构，Recevier不为央行机构且权限校验通过
     */
    @Test
    public void testDoInvoke_SendPboc_RecPermission() {
        //public void doInvoke(ChannelContext context)

        new MockUp<MessagePermissionCache>(MessagePermissionCache.class) {
            @Mock
            public MessagePermissionDTO getMessagePermission(String orgId, String messageId) {
                MessagePermissionDTO messagePermissionDTO = new MessagePermissionDTO();
                return messagePermissionDTO;
            }
        };

        String signature = "";
        SoapHeader header = SoapUtils.getSoapHeaderBean(sendPbocXml.getBytes());
        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.SERIALIZATION, recPbocXml_461);
        context.setAttachment(Constant.SIGNATURE, "");
        jurisdictionAction.doInvoke(context);
    }

    /**
     * 测试Invoke方法 dto的Sender为央行机构，Recevier不为央行机构且权限校验不通过
     */
    @Test(expected = GwException.class)
    public void testDoInvoke_SendPboc_RecNoPermission() {
        //public void doInvoke(ChannelContext context)

        new MockUp<MessagePermissionCache>(MessagePermissionCache.class) {

            @Mock
            public MessagePermissionCache getInstance() {
                return new MessagePermissionCache();
            }

            @Mock
            public MessagePermissionDTO getMessagePermission(String orgId, String messageId) {
                MessagePermissionDTO messagePermissionDTO = new MessagePermissionDTO();
                return messagePermissionDTO;
            }
        };
        String signature = "";
        SoapHeader header = SoapUtils.getSoapHeaderBean(sendPbocXml.getBytes());
        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.SERIALIZATION, recPbocXml_461);
        context.setAttachment(Constant.SIGNATURE, "");
        jurisdictionAction.doInvoke(context);
    }

    /**
     * 测试doException方法，dto Sender为央行机构，C->A模式
     */
    @Test
    public void testDoException_Return_Dto() {
        String signature = "";
        SoapHeader header = SoapUtils.getSoapHeaderBean(sendPbocXml.getBytes());
        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.SERIALIZATION, recPbocXml_461);
        context.setAttachment(Constant.SIGNATURE, "");
        jurisdictionAction.doException(context, new GwException("测试"));
    }

    /**
     * 测试doException方法，dto Sender为央行机构，非C->A模式
     */
    @Test
    public void testDoException_Return_Xml() {
        new MockUp<SoapUtils>(SoapUtils.class) {
            @Mock
            public EnvelopeDTO toDto(SoapHeader header, String xml) {
                EnvelopeDTO<Dcep91100101DTO> envelopeDTO = new EnvelopeDTO<>();
                SoapHeader soapHeader = new SoapHeader();
                soapHeader.setVer("01");
                soapHeader.setSndDtTm("2019-11-12T09:25:43");
                soapHeader.setMsgTp("dcep.911.001.01");
                soapHeader.setMsgSN("201911120041911000000000000900010001");
                soapHeader.setSender("C1010411000013");
                soapHeader.setReceiver("C1010211000012");
                soapHeader.setSignSN("3");
                soapHeader.setNcrptnSN("3");
                soapHeader.setDgtlEnvlp("3");
                envelopeDTO.setSoapHeader(soapHeader);
                Dcep91100101DTO dcep91100101DTO = new Dcep91100101DTO();
                dcep91100101DTO.setFaultactor("C1010211000012");
                dcep91100101DTO.setDetail("机构处理错误");
                dcep91100101DTO.setFaultcode(GwErrorEnum.MANAGER_MAN_TIMEOUT.getCode());
                dcep91100101DTO.setFaultstring(GwErrorEnum.MANAGER_MAN_TIMEOUT.getDescription());
                SoapBody<Dcep91100101DTO> body = new SoapBody<>();
                body.setT(dcep91100101DTO);
                envelopeDTO.setSoapBody(body);
                return envelopeDTO;
            }
        };
        String signature = "";
        SoapHeader header = SoapUtils.getSoapHeaderBean(recPbocXml_461.getBytes());
        ChannelContext context = new ChannelContext();
        context.setAttachment(Constant.SOAP_HEADER, header);
        context.setAttachment(Constant.SERIALIZATION, recPbocXml_461);
        context.setAttachment(Constant.SIGNATURE, "");
        jurisdictionAction.doException(context, new GwException("测试"));
    }

}
