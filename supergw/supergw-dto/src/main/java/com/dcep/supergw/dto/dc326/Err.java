package com.dcep.supergw.dto.dc326;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Setter
@Getter
@ToString
public class Err implements Serializable {

    private static final long serialVersionUID = 4452529290095208544L;

    /**
     * 运营机构业务拒绝码
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "R[0-9]{3}")
    @NotBlank
    private String rjctCd;
}
