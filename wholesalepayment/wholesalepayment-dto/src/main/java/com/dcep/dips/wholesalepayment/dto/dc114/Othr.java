package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件--------Other
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:44
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
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String id;
}
