package com.dcep.supergw.dto.model;

import com.dcep.common.model.soap.SoapHeader;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/**
 * @author : maxinyu
 * @version : Envelope.java v 0.1 2019-12-05
 * @description :
 */
@Getter
@Setter
@JacksonXmlRootElement(localName = "Envelope")
public class Envelope implements Serializable {
    private static final long serialVersionUID = -7451138890080504392L;
    /**
     * soap header
     */
    @JacksonXmlProperty(localName = "Header")
    private SoapHeader        soapHeader;

}
