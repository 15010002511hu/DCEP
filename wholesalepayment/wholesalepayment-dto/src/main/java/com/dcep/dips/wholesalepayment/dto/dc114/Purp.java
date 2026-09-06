package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------Purpose
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:24
 */
@Data
public class Purp implements Serializable {
    /**
     * 业务种类编码
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @Length(
            min = 1,
            max = 8
    )
    @NotBlank
    private String prtry;
}
