package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.enums.ErrorEnum;
import com.dcep.common.exception.DcepException;
import com.dcep.gateway.mcbdc.dto.mcbs708.*;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs200Envelope;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs201Envelope;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import com.dcep.gateway.mcbdc.dto.soap.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MbridgeToDcepConvertManagerImplTest {
    @InjectMocks
    private MbridgeToDcepConvertManagerImpl mbridgeToDcepConvertManagerImpl;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convert() throws DcepException {
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
        mbridgeToDcepConvertManagerImpl.convertRequest(genericGwDTOGenericEnvelopeDTO);
    }

    @Test
    public void convert1() throws DcepException {
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = GenerateMcbs201Envelope.generateMcbs201EnvelopeDTO();
        mbridgeToDcepConvertManagerImpl.convertRequest(genericGwDTOGenericEnvelopeDTO);
    }

    @Test
    public void convert2() throws DcepException {
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTOGenericEnvelopeDTO = generateMcbs708();
        try {
            mbridgeToDcepConvertManagerImpl.convertRequest(genericGwDTOGenericEnvelopeDTO);
        } catch (DcepException e) {
            System.out.println(e.getCode());
            assertEquals(ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), e.getCode());
        }
    }

    private GenericEnvelopeDTO<GenericGwDTO> generateMcbs708(){
        /*
        String mcbsMsgId = "111";
        // 生成 PayerAgt 实例
        PayerAgt payerAgt = PayerAgt.builder()
                .finInstnId(FinInstnId.builder().nm("name").lEI("0000").build())
                .wltId("123456789012")
                .ctryOfRes("123")
                .cenBnkID("7890")
                .build();

        // 生成 PayeeAgt 实例
        PayeeAgt payeeAgt = PayeeAgt.builder()
                .finInstnId(FinInstnId.builder().nm("name").lEI("0001").build())
                .wltId("123456789013")
                .ctryOfRes("124")
                .cenBnkID("7891")
                .build();

        // 生成 DetailInf 实例
        DetailInf detailInf = DetailInf.builder()
                .amt("100.00")
                .bizTp("PYMT")
                .msgId(mcbsMsgId)
                .stsCd("SUCD")
                .deDtTm("2025-09-25T14:08:28")
                .payerAgt(payerAgt)
                .payeeAgt(payeeAgt)
                .build();

        // 生成 GrpHdr 实例
        GrpHdr grpHdr = GrpHdr.builder()
                .msgId(mcbsMsgId)
                .dlTime("2025092511")
                .creDtTm("2025-09-25T14:08:28")
                .build();

        // 生成 TxInf 实例
        TxInf txInf = TxInf.builder()
                .detailInf(detailInf)
                .build();

        // 生成 Mcbs70800101DTO 实例
        Mcbs70800101DTO mcbs70800101DTO = Mcbs70800101DTO.builder()
                .grpHdr(grpHdr)
                .txInf(txInf)
                .build();

        McbsEnvelopeDTO<McbsGwDTO> mcbsGwReqDTO = new McbsEnvelopeDTO<>();
        mcbsGwReqDTO.setSoapBody(new McbsSoapBody<>(mcbs70800101DTO));
        mcbsGwReqDTO.setSoapHeader(TestUtils.generateMcbsSoapHeader("mcbs.708.001.01"));
        return mcbsGwReqDTO;
         */
        return null;
    }
}
