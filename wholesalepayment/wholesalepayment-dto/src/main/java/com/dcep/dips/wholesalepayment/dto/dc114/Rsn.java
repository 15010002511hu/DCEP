package com.dcep.dips.wholesalepayment.dto.dc114;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 组件----Reason
 * @Author zhaotianwu
 * @date 2025-10-13 17:13:38
 */
@Data
public class Rsn implements Serializable {
    /**
     * 退汇原因码
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String prtry;
}
