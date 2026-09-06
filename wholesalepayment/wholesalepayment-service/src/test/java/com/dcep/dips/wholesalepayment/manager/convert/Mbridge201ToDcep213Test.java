package com.dcep.dips.wholesalepayment.manager.convert;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.dips.wholesalepayment.utils.GenerateMcbs201Envelope;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class Mbridge201ToDcep213Test {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void convert() throws DcepException {
        GenericEnvelopeDTO<GenericGwDTO> genericGwDTO = GenerateMcbs201Envelope.generateMcbs201EnvelopeDTO();
        Mbridge201ToDcep213.convert(genericGwDTO, new EnvelopeDTO<>());
    }
}
