package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件----NewCriteria
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:59:27
 */
@Data
public class NewCrit implements Serializable {
    /**
     * 组件------SearchCriteria
     */
    @JacksonXmlProperty(
            localName = "SchCrit"
    )
    @NotNull
    @Valid
    private SchCrit schCrit;
}
