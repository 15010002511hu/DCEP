package com.dcep.supergw.dto.dc434;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : RspsnInf.java v 0.1 2019-08-21
 * @description :
 */
@Getter
@Setter
@ToString
public class RspsnInf implements Serializable {
    private static final long serialVersionUID = 6667063645421530029L;

    /**
     * 业务回执状态 不能为空，码值长度4
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank
    @Length(min = 4, max = 4)
    private String rspsnSts;

    /**
     * 业务拒绝码 标签可以不存在，存在必须有值
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Length(min = 4, max = 4)
    private String rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String rjctInf;

    /**
     * 管理类型
     */
    @JacksonXmlProperty(localName = "MgmtTp")
    @NotBlank
    @Length(min = 4, max = 4)
    private String mgmtTp;

    /**
     * 挂接协议号
     */
    @JacksonXmlProperty(localName = "PtcId")
    @Length(min = 1, max = 60)
    private String ptcId;

    /**
     * 动态关联码
     */
    @JacksonXmlProperty(localName = "MsgSndCd")
    @Length(min = 1,max = 64)
    private String msgSndCd;
}
