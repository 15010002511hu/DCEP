package com.dcep.supergw.dto.dc026;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 合约产品标签列表
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:47:09
 */
@Data
public class PdctLbl implements Serializable {
    /**
     * 合约产品标签
     */
    @JacksonXmlProperty(
            localName = "PdctLbl"
    )
    @Length(
            min = 1,
            max = 40
    )
    @NotBlank
    private String pdctLbl;
}
