package com.dcep.supergw.dto.dc326;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Setter
@Getter
@ToString
public class OprlErr implements Serializable {
    private static final long serialVersionUID = -5899580484623671818L;
    /**
     * 应答拒绝信息err
     */
    @JacksonXmlProperty(localName = "Err")
    @NotNull
    @Valid
    private Err err;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    @NotBlank
    private String rjctInf;
}
