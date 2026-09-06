package com.dcep.supergw.dto.dc039;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 查询参数
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:35:56
 */
@Data
public class QryParams implements Serializable {
    /**
     * 合约签约记录ID
     */
    @JacksonXmlProperty(
            localName = "CtrctSgntrRcrdId"
    )
    @Length(
            min = 1,
            max = 64
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ctrctSgntrRcrdId;
}
