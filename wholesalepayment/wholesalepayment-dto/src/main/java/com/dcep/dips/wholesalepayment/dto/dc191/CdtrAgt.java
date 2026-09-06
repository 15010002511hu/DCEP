package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 收款方代理
 * @Author qiaopengyu
 * @date 2025-10-30 21:16:53
 */
@Data
public class CdtrAgt implements Serializable {
    private static final long serialVersionUID = -8489554943906968426L;
    /**
     * 参与机构组件
     */
    @JacksonXmlProperty(
            localName = "FinInstnId"
    )
    @NotNull
    private FinInstnId finInstnId;
}
