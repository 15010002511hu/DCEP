package com.dcep.dips.wholesalepayment.manager.convert;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.dips.wholesalepayment.dto.dc203.Dcep20301001DTO;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import com.dcep.gateway.mcbdc.dto.soap.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class Dcep203ToMbridge200Test {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convert() throws DcepException {

        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_203();
        gwReqDTO.body().init();
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTO = Dcep203ToMbridge200.convert(gwReqDTO);

        Dcep203ToMbridge200.supplement(genericGwDTO, "200");
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
