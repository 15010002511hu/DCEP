package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 组件----------Error
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:27:55
 */
@Data
public class Err implements Serializable {
    /**
     * 原业务处理码
     */
    @JacksonXmlProperty(
            localName = "Prtry"
    )
    @NotBlank
    private String prtry;
}
