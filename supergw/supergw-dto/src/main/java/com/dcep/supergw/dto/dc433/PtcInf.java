package com.dcep.supergw.dto.dc433;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : WltInf.java v 0.1 2019-08-21
 * @description :
 */
@Getter
@Setter
@ToString
public class PtcInf implements Serializable {

	private static final long serialVersionUID = 4853065045747805741L;

	/**
     * 钱包ID
     */
    @JacksonXmlProperty(localName = "PtcId")
    @Length(min = 1, max = 34)
    private String ptcId;

    /**
     * 钱包类型
     */
    @JacksonXmlProperty(localName = "MsgSndCd")
    @Length(min = 1, max = 64)
    private String msgSndCd;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(localName = "MsgVrfy")
    @Length(min = 1, max = 20)
    private String msgVrfy;
}
