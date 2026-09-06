package com.dcep.supergw.dto.dc025;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 请求分页信息
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:47:35
 */
@Data
public class ReqPgInf implements Serializable {
    /**
     * 当前页码
     */
    @JacksonXmlProperty(
            localName = "Pgntn"
    )
    @Length(
            min = 1,
            max = 3
    )
    @NotBlank
    @Pattern(
            regexp = "^[1-9]\\d{0,2}$",
            message = "页码只能是数字且范围在[1,999]"
    )
    @Max(value = 999L, message = "页码不能大于999", groups = {Priority.High.class})
    @Min(value = 1L, message = "页码不能小于1", groups = {Priority.High.class})
    private String pgntn;

    /**
     * 每页条数
     */
    @JacksonXmlProperty(
            localName = "PgSz"
    )
    @Length(
            min = 1,
            max = 3
    )
    @NotBlank
    @Pattern(
            regexp = "^[1-9]\\d{0,2}$",
            message = "每页条数只能是数字且范围为[1,50]"
    )
    @Max(value = 50L, message = "每页条数不能大于50", groups = {Priority.High.class})
    @Min(value = 1L, message = "每页条数不能小于1", groups = {Priority.High.class})
    private String pgSz;
}
