package com.dcep.supergw.dto.dc323;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TrxInf implements Serializable {
    private static final long serialVersionUID = -827095235046962542L;

    /**
     * 交易类型
     */
    @JacksonXmlProperty(localName = "TrxTp")
    @NotBlank
    @Pattern(regexp = "TT[0-9]{2}")
    private String trxTp;

    /**
     * 交易金额
     */
    @JacksonXmlProperty(localName = "TrxAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount trxAmt;

}
