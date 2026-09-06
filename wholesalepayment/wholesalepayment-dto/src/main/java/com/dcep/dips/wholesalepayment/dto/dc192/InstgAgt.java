package com.dcep.dips.wholesalepayment.dto.dc192;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 发起参与机构
 * @Author qiaopengyu
 * @date 2025-10-31 11:24:55
 */
@Data
public class InstgAgt implements Serializable {
    /**
     * 参与机构组件
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotBlank
    private String finInstnId;
}
