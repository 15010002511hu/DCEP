package com.dcep.supergw.dto.dc069;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.Data;

@Data
public class SgntrPtyList implements Serializable {

    @Valid
    @JacksonXmlProperty(localName = "SgntrWltInf")
    SgntrWltInf sgntrWltInf;


    @Valid
    @JacksonXmlProperty(localName = "SgntrMrchntInf")
    SgntrMrchntInf sgntrMrchntInf;

    @Valid
    @JacksonXmlProperty(localName = "SgntrInstnInf")
    SgntrInstnInf sgntrInstnInf;
}
