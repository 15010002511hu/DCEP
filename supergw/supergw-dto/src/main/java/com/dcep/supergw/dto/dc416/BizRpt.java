package com.dcep.supergw.dto.dc416;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 应答的原业务信息
 *
 * @author chenkai
 */
@Getter
@Setter
@ToString
public class BizRpt implements Serializable {
    /**  */
    private static final long serialVersionUID = -4399682892586463732L;

    /**
     * 交易状态
     */
    @JacksonXmlProperty(localName = "TrdInf")
    @NotNull
    @Valid
    private TrdInf trdInf;


    /**
     * 原交易状态
     */
    @JacksonXmlProperty(localName = "Cdtr")
    @NotNull
    @Valid
    private Cdtr cdtr;

    @JacksonXmlProperty(localName = "AcqAgtInf")
    @Valid
    private AcqAgtInf acqAgtInf;

    @JacksonXmlProperty(localName = "MrchntTerInf")
    @Valid
    private MrchntTerInf mrchntTerInf;

    /**
     *
     */
    @JacksonXmlElementWrapper(localName = "SdyList")
    @JacksonXmlProperty(localName = "SdyInf")
    @Valid
    private List<SdyInf> sdyList;

}
