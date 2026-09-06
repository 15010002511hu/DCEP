package com.dcep.supergw.dto.dc016;

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
 * 应答分页信息
 *
 * @Author qinchaoyong
 * @date 2024-09-10 14:25:27
 */
@Data
public class RspnPgInf implements Serializable {
    /**
     * 总条数
     */
    @JacksonXmlProperty(
            localName = "Ttls"
    )
    @Length(
            min = 1,
            max = 10
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String ttls;

    /**
     * 当前页数
     */
    @JacksonXmlProperty(
            localName = "PgIndx"
    )
    @Length(
            min = 1,
            max = 3
    )
    @NotBlank
    @Pattern(
            regexp = "^[1-9]\\d{0,2}$",
            message = "页码只能是数字"
    )
    private String pgIndx;

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

    /**
     * 是否有下一页
     */
    @JacksonXmlProperty(
            localName = "NxtPgFlg"
    )
    @Length(
            min = 1,
            max = 2
    )
    @NotBlank
    @Pattern(
            regexp = "00||01"
    )
    private String nxtPgFlg;
}
