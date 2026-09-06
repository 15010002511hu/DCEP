package com.dcep.supergw.dto.dc074;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class RjctInf implements Serializable {

    @Length(min =1,max = 105)
    @JacksonXmlProperty(localName = "RjctMsg")
    private String rjctMsg;
}
