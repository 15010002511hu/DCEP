package com.dcep.dips.wholesalepayment.manager.convert;

import com.dcep.common.exception.DcepException;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs200Envelope;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs202Envelope;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MbridgeToDcep203Test {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convert() throws DcepException {
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTO200 = GenerateMcbs200Envelope.generateMcbs200EnvelopeDTO();
        MbridgeToDcep203.convert(genericGwDTO200);
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTO202 = GenerateMcbs202Envelope.generateMcbs202EnvelopeDTO();
        MbridgeToDcep203.convert(genericGwDTO202);

    }
}
