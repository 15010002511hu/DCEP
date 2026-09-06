package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件------Other
 * @Author qiaopengyu
 * @date 2025-10-30 21:16:59
 */
@Data
public class Othr implements Serializable {
    /**
     * 付款方钱包ID
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
