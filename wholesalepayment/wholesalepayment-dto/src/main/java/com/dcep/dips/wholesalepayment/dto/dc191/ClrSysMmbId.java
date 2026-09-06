package com.dcep.dips.wholesalepayment.dto.dc191;

import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckOrgState;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 清算系统机构ID组件
 */
@JacksonXmlRootElement(localName = "ClrSysMmbId")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ClrSysMmbId implements Serializable {
    private static final long serialVersionUID = 4687810619128808965L;

    /**
     * 系统ID组件．．．．．ClrSysId
     */
    @JacksonXmlProperty(localName = "ClrSysId")
    @NotNull
    private ClrSysId clrSysId;

    /**
     * 付款运营机构/收款运营机构
     */
    @JacksonXmlProperty(localName = "MmbId")
    @NotBlank
    @Length(min = 1, max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckOrgState(groups = Priority.Lowest.class)
    private String            mmbId;
}
