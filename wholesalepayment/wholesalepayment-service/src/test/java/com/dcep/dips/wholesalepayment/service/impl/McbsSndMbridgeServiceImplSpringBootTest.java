package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dal.mapper.StorageForwardMapper;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs200Envelope;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs201Envelope;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs202Envelope;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.gateway.mcbdc.dto.soap.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class McbsSndMbridgeServiceImplSpringBootTest {

    @Autowired
    private McbsSndMbridgeServiceImpl mcbsSndMbridgeService;
    @Autowired
    StorageForwardMapper storageForwardMapper;

    @Before
    public void setUp() {

    }

    @Test
    @DisplayName("测试桥上发起mcbs200下桥成功")
    public void testMbridgeAccountingSuccess() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("000");
            GenericEnvelopeDTO<GenericGwDTO> mcbs200EnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeService.process(mcbs200EnvelopeDTO);
            System.out.println(response.isSuccess() + "--" + response.getResult() + "--" + response.getErrorCode() + "--" + response.getErrorMsg());
        }
    }

    @Test
    @DisplayName("测试桥上发起mcbs201上桥成功")
    public void testMbridgeAccountingSuccess1() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("000");
            GenericEnvelopeDTO<GenericGwDTO> mcbs201EnvelopeDTO = GenerateMcbs201Envelope.generateMcbs201EnvelopeDTO();
            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeService.process(mcbs201EnvelopeDTO);
            System.out.println(response.isSuccess() + "--" + response.getResult() + "--" + response.getErrorCode() + "--" + response.getErrorMsg());
        }
    }

    @Test
    @DisplayName("测试桥上发起mcbs202支付退回[下桥]成功")
    public void testMbridgeAccountingSuccess2() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("000");
            GenericEnvelopeDTO<GenericGwDTO> mcbs202EnvelopeDTO = GenerateMcbs202Envelope.generateMcbs202EnvelopeDTO();
            Response<GenericEnvelopeDTO<GenericGwDTO>> response = mcbsSndMbridgeService.process(mcbs202EnvelopeDTO);
            System.out.println(response.isSuccess() + "--" + response.getResult() + "--" + response.getErrorCode() + "--" + response.getErrorMsg());
        }
    }

    @Test
    @DisplayName("")
    public void test() throws Exception {

        GenericEnvelopeDTO<GenericGwDTO> mcbs200EnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
        McbsEnvelopeDTO<McbsGwDTO> mcbs200EnvelopeDTO1 = (McbsEnvelopeDTO<McbsGwDTO>) mcbs200EnvelopeDTO;
        String serialize = serialize(mcbs200EnvelopeDTO1);
        System.out.println(serialize);
    }

    @Test
    @DisplayName("")
    public void test1() throws Exception {

        GenericEnvelopeDTO<GenericGwDTO> mcbs201EnvelopeDTO = GenerateMcbs201Envelope.generateMcbs201EnvelopeDTO();
        McbsEnvelopeDTO<McbsGwDTO> mcbs201EnvelopeDTO1 = (McbsEnvelopeDTO<McbsGwDTO>) mcbs201EnvelopeDTO;
        String serialize = serialize(mcbs201EnvelopeDTO1);
        System.out.println(serialize);
    }

    @Test
    @DisplayName("")
    public void test2() throws Exception {

        GenericEnvelopeDTO<GenericGwDTO> mcbs202EnvelopeDTO = GenerateMcbs202Envelope.generateMcbs202EnvelopeDTO();
        McbsEnvelopeDTO<McbsGwDTO> mcbs202EnvelopeDTO1 = (McbsEnvelopeDTO<McbsGwDTO>) mcbs202EnvelopeDTO;
        String serialize = serialize(mcbs202EnvelopeDTO1);
        System.out.println(serialize);
    }

    public static <T extends McbsGwDTO> String serialize(McbsEnvelopeDTO<T> envelopeDTO) throws Exception {
        /* 组装报文头 */
        McbsSoapHeader mcbsSoapHeader = envelopeDTO.getSoapHeader();
        StringBuilder mcbsXmlBuilder = new StringBuilder();
        mcbsXmlBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">")
                .append("<soap:Header>" )
                .append("<Ver>" ).append(mcbsSoapHeader.getVer()).append("</Ver>" )
                .append("<SndDtTm>" ).append(mcbsSoapHeader.getSndDtTm()).append("</SndDtTm>" )
                .append("<MsgTp>" ).append(mcbsSoapHeader.getMsgTp()).append("</MsgTp>" )
                .append("<SndLEI>" ).append(mcbsSoapHeader.getSenderLEI()).append("</SndLEI>" )
                .append("<SndCBMALEI>" ).append(mcbsSoapHeader.getSenderCBMALEI()).append("</SndCBMALEI>" )
                .append("<RecvLEI>" ).append(mcbsSoapHeader.getReceiverLEI()).append("</RecvLEI>" )
                .append("<RecvCBMALEI>" ).append(mcbsSoapHeader.getReceiverCBMALEI()).append("</RecvCBMALEI>" )
                .append("<MsgDrn>" ).append(mcbsSoapHeader.getMessageDirection()).append("</MsgDrn>" );
        if(!StringUtils.isEmpty(mcbsSoapHeader.getContractSignAlgo())){
            mcbsXmlBuilder.append("<ContrSignAlgo>" ).append(mcbsSoapHeader.getContractSignAlgo()).append("</ContrSignAlgo>");
        }
        mcbsXmlBuilder.append("<SignSN>").append("SignSN").append("</SignSN>");
        if(!StringUtils.isEmpty(mcbsSoapHeader.getNcrptnSN())){
            mcbsXmlBuilder.append("<NcrptnSN>").append(mcbsSoapHeader.getNcrptnSN()).append("</NcrptnSN>");
        }
        mcbsXmlBuilder.append("</soap:Header>");
        /* 组装报文体 */
        JacksonXmlRootElement rootElement = envelopeDTO.body().getClass()
                .getDeclaredAnnotation(JacksonXmlRootElement.class);
        mcbsXmlBuilder.append(new XmlMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL)
                /*.writerWithDefaultPrettyPrinter()*/.writeValueAsString(envelopeDTO.getSoapBody())
                .replaceAll("#localName", rootElement.localName()));
        mcbsXmlBuilder.append(
                "</soap:Envelope>");

        return mcbsXmlBuilder.toString();
    }
}
