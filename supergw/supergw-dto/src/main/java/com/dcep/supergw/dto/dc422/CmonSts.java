package com.dcep.supergw.dto.dc422;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 组件------CommonStatus
 *
 * @Author qinchaoyong
 * @date 2024-12-04 10:28:09
 */
@Data
public class CmonSts implements Serializable {
    /**
     * 组件--------Code
     */
    @JacksonXmlProperty(
            localName = "Cd"
    )
    @NotNull
    @Valid
    private Cd cd;
}
