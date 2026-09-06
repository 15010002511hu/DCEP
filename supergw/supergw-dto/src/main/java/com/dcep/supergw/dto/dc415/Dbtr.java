package com.dcep.supergw.dto.dc415;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 付款钱包信息
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "Dbtr")
@Getter
@Setter
@ToString
public class Dbtr implements Serializable{

    /**  */
    private static final long serialVersionUID = -5185417180413486332L;

    /**
     * 付款钱包名称
     */
    @JacksonXmlProperty(localName = "DbtrNm")
    @Length(min = 1,max = 60)
    private String            dbtrNm;

    /**
     * 付款钱包ID
     */
    @JacksonXmlProperty(localName = "DbtrWltId")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1,max = 34)
    private String            dbtrWltId;

    /**
     * 付款钱包类型
     * WT01：个人钱包
     * WT02：子个人钱包
     * WT09：对公钱包
     * WT10：子对公钱包
     */
    @JacksonXmlProperty(localName = "DbtrWltTp")
    @Length(min = 4,max = 4)
    @Pattern(regexp = "WT01||WT02||WT09||WT10")
    private String            dbtrWltTp;

    /**
     * 付款钱包等级
     * WL01：一类钱包
     * WL02：二类钱包
     * WL03：三类钱包
     * WL04：四类钱包
     */
    @JacksonXmlProperty(localName = "DbtrWltLvl")
    @Length(min = 4,max = 4)
    @Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
    private String            dbtrWltLvl;

    /**
     * 付款运营机构
     */
    @JacksonXmlProperty(localName = "DbtrPtyId")
    @Length(min =1,max = 14)
    private String            dbtrPtyId;

}
