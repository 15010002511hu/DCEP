package com.dcep.dips.wholesalepayment.dto.dc211;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import java.util.List;
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
     * 收款人为合约实例信息
     */
    @JacksonXmlProperty(localName = "CdtrCtrctInst")
    @Valid
    private CdtrCtrctInst cdtrCtrctInst;

    /**
     * 付款人为合约实例信息
     */
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "DbtrCtrctInst")
    @Valid
    private List<DbtrCtrctInst> dbtrCtrctInstList;

}
