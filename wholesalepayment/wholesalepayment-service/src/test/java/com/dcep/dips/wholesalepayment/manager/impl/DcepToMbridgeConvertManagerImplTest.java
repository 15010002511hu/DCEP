package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc213.Dcep21301001DTO;
import com.dcep.dips.wholesalepayment.dto.dc262.Dcep26201001DTO;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs200Envelope;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs201Envelope;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class DcepToMbridgeConvertManagerImplTest {
    @InjectMocks
    private DcepToMbridgeConvertManagerImpl dcepToMbridgeConvertManagerImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convert() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO203 = loadEnvelopeDTO_203();
        dcepToMbridgeConvertManagerImpl.convertRequest(gwReqDTO203);
    }

    @Test
    public void convert1() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO213 = loadEnvelopeDTO_213();
        gwReqDTO213.body().init();
        dcepToMbridgeConvertManagerImpl.convertRequest(gwReqDTO213);
    }

    @Test
    public void convert2() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO262 = loadEnvelopeDTO_262("PR01");
        try {
            dcepToMbridgeConvertManagerImpl.convertRequest(gwReqDTO262);
        } catch (DcepException e) {
            System.out.println(e.getCode());
            assertEquals(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), e.getCode());
        }
    }

    @Test
    public void supplement() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO203 = loadEnvelopeDTO_203();
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTO200 = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
        dcepToMbridgeConvertManagerImpl.requestSupplement(genericGwDTO200, gwReqDTO203, "");
    }

    @Test
    public void supplement1() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO213 = loadEnvelopeDTO_213();
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTO201 = GenerateMcbs201Envelope.generateMcbs201EnvelopeDTO();
        dcepToMbridgeConvertManagerImpl.requestSupplement(genericGwDTO201, gwReqDTO213, "");
    }

    @Test
    public void supplement2() throws DcepException {
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTO201 = GenerateMcbs201Envelope.generateMcbs201EnvelopeDTO();
        EnvelopeDTO<GwDTO> gwReqDTO262 = loadEnvelopeDTO_262("PR01");
        try {
            dcepToMbridgeConvertManagerImpl.requestSupplement(genericGwDTO201, gwReqDTO262, "");
        } catch (DcepException e) {
            System.out.println(e.getCode());
            assertEquals(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), e.getCode());
        }
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_203() {
        String msgTp = "dcep.203.010.01";
        String shortMsgTp = "203";
        String shortSender = "002";
        String shortReceiver = "005";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "mbridge/20301.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{BatchId}}", getBatchId());

        Dcep20301001DTO dcep203DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep20301001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep203DTO));
        return envelopeDTO;
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_213() {
        String msgTp = "dcep.213.010.01";
        String shortMsgTp = "213";
        String shortSender = "005";
        String shortReceiver = "002";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "mbridge/21301.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{BatchId}}", getBatchId());

        Dcep21301001DTO dcep213DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep21301001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep213DTO));
        return envelopeDTO;
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_262(String rspsnSts) {
        String msgTp = "dcep.262.010.01";
        String shortMsgTp = "262";
        String shortSender = "003";
        String shortReceiver = "002";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "payment/262.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{SysWorkDt}}", DcepDateUtils.getNowStrByPattern(DcepDateUtils.ISO_DATE_PATTERN));
        dataMap.put("{{RspsnSts}}", rspsnSts);

        Dcep26201001DTO dcep262DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep26201001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep262DTO));

        return envelopeDTO;
    }

    public static String getBatchId() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String formattedDate = sdf.format(date);

        // 解析日期时间字符串
        String[] parts = formattedDate.split("[T:-]");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        int hour = Integer.parseInt(parts[3]);

        // 生成批次ID
        return "B" + year + s(month + 1) + s(day) + s(hour + 1) + "00";
    }

    private static String s(int num) {
        return String.format("%02d", num);
    }
}
