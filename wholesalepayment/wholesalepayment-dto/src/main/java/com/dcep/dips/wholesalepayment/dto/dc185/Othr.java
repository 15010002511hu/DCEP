package com.dcep.dips.wholesalepayment.dto.dc185;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------Other
 * @Author luteng
 * @date 2025-10-12 16:17:04
 */
@Data
public class Othr implements Serializable {
    /**
     * 结算钱包编码
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @Length(
            min = 1,
            max = 34
    )
    @NotBlank
    private String id;
}
