package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 参与机构组件
 * @Author liuyuzeng
 * @date 2025-10-27 21:13:17
 */
@Data
public class FinInstnId implements Serializable {

    /**
     * LEI编码
     */
    @JacksonXmlProperty(localName = "BICFI")
    private String bicfi;

    /**
     * 组件------ClrSysMmbId
     */
    @JacksonXmlProperty(localName = "ClrSysMmbId")
    @NotNull
    private ClrSysMmbId clrSysMmbId;

    /**
     * LEI编码
     */
    @JacksonXmlProperty(localName = "LEI")
    @Length(min = 1, max = 20)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            lei;

    /**
     * 组件------PostalAddress
     */
    @JacksonXmlProperty(
            localName = "PstlAdr"
    )
    @Valid
    private PstlAdr pstlAdr;

    /**
     * 付款方名称
     */
    @JacksonXmlProperty(
            localName = "Nm"
    )
    @Length(
            min = 1,
            max = 140
    )
    @Valid
    private String nm;
}
