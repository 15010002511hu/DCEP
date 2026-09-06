package com.dcep.supergw.dto.dc421;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 组件----NewCriteria
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:26:46
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
