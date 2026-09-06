package com.dcep.dips.wholesalepayment.dto.dc112;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 组件----Proxy
 * @Author zhaotianwu
 * @date 2025-10-13 16:55:51
 */
@Data
public class Prxy implements Serializable {
    /**
     * 付款方币串
     */
    @JacksonXmlProperty(
            localName = "Id"
    )
    @Length(
            min = 1,
            max = 2048
    )
    @NotBlank
    private String id;
}
