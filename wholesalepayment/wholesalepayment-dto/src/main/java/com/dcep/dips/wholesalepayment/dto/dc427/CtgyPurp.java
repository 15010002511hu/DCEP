package com.dcep.dips.wholesalepayment.dto.dc427;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件--------CategoryPurpose
 * @Author luteng
 * @date 2025-10-21 16:31:26
 */
@Setter
@Getter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CtgyPurp implements Serializable {
    /**
     * 原业务类型编码
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @Length(
            min = 1,
            max = 3
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String prtry;
}
