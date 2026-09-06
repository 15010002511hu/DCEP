package com.dcep.supergw.dto.dc442;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 渠道信息
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "ChnlInf")
@Getter
@Setter
@ToString
public class ChnlInf implements Serializable {

    private static final long serialVersionUID = -3221125938942264904L;

    /**
     * 签约渠道
     * SC00：电脑浏览器
     * SC01：手机应用程序
     * SC02：银行柜面
     */
    @JacksonXmlProperty(localName = "SgnChnl")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "SC00||SC01||SC02")
    private String                  sgnChnl;

    /**
     * 渠道参数
     */
    @JacksonXmlProperty(localName = "ChnlParam")
    @Length(min = 1,max = 256)
    private String                  chnlParam;


}
