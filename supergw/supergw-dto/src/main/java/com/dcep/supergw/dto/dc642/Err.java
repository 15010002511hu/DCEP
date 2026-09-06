package com.dcep.supergw.dto.dc642;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 应答拒绝信息err
 * 
 * @author chenkai
 * @version $Id: Err.java, v 0.1 2019年7月17日 上午11:32:59 chenkai Exp $
 */
@Getter
@Setter
@ToString
public class Err implements Serializable {
    /**  */
    private static final long serialVersionUID = 5596531163527269175L;

    /**
     * 运营机构业务拒绝码
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Length(min = 1, max = 4)
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "R[0-9]{3}")
    private String rjctCd;

}
