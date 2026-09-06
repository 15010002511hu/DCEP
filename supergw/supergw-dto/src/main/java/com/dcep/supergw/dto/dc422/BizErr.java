package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件--------BusinessError
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:27:56
 */
@Data
public class BizErr implements Serializable {
    /**
     * 组件----------Error
     */
    @JacksonXmlProperty(
            localName = "Err"
    )
    @NotNull
    @Valid
    private Err err;

    /**
     * 原业务处理信息
     */
    @JacksonXmlProperty(
            localName = "Desc"
    )
    @Length(
            min = 1,
            max = 105
    )
    @NotBlank
    private String desc;
}
