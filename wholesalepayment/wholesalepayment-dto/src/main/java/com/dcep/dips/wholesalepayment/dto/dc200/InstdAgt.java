package com.dcep.dips.wholesalepayment.dto.dc200;

import com.dcep.common.model.soap.FinInstnId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 接收参与机构
 * @Author luteng
 * @date 2025-09-28 11:40:32
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstdAgt implements Serializable {
    /**
     * 参与机构组件
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    private FinInstnId finInstnId;
}
