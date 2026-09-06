package com.dcep.supergw.dto.dc437;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class WltInf implements Serializable, DataEncryption {
	 /**
	 * 
	 */
	private static final long serialVersionUID = -4930156878465856388L;

	/**
     * 钱包开立所属运营结构编号
     */
    @JacksonXmlProperty(localName = "WltPtyId")
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltPtyId;

	 /**
     * 钱包ID
     */
    @JacksonXmlProperty(localName = "WltId")
    @NotBlank
    @Length(min = 1, max = 68)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltId;
    
    /**
     * 钱包类型
     */
    @JacksonXmlProperty(localName = "WltTp")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "WT01||WT02||WT09||WT10")
    private String wltTp;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(localName = "WltLvl")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "WL01||WL02||WL03||WL04")
    private String wltLvl;
    
	 /**
     * 业务上下文
     */
    @JacksonXmlProperty(localName = "ContextNo")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 64)
    private String contextNo;
    
    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        if(this.getWltId() != null){
            this.setWltId(encryptionHelper.encrypt(this.getWltId()));
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        if(this.getWltId() != null){
            this.setWltId(encryptionHelper.decrypt(this.getWltId()));
        }
    }
    
}
