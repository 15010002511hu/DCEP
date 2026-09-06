package com.dcep.supergw.dto.dc008;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 附件列表
 *
 * @Author qinchaoyong
 * @date 2024-09-10 11:45:00
 */
@Data
public class Attchmntchmnt implements Serializable {
    /**
     * 附件
     */
    @JacksonXmlProperty(
            localName = "Attchmnt"
    )
    @NotNull
    @Valid
    private Attchmnt attchmnt;
}
