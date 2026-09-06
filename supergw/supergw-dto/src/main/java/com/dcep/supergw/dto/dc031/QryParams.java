package com.dcep.supergw.dto.dc031;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 查询参数
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:50:06
 */
@Data
public class QryParams implements Serializable {
    /**
     * 申请流水号
     */
    @JacksonXmlProperty(
            localName = "SrlNb"
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
    private String srlNb;
}
