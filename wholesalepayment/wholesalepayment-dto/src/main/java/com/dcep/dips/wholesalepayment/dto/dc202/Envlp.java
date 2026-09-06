package com.dcep.dips.wholesalepayment.dto.dc202;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@JacksonXmlRootElement(localName = "Envlp")
@Getter
@Setter
@ToString
public class Envlp implements Serializable {

    private static final long serialVersionUID = -8200604792176526022L;
    /**
     * 付款人合约钱包应答
     */
    @JacksonXmlProperty(localName = "CtrctRspsn")
    @Valid
    private CtrctRspsn ctrctRspsn;

}
