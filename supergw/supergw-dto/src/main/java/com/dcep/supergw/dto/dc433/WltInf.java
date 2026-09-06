package com.dcep.supergw.dto.dc433;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckInsId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
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
public class WltInf implements Serializable, DataEncryption {
    private static final long serialVersionUID = -3492485286389362183L;
    
    /**
     * 钱包所属运营机构ID
     */
    @JacksonXmlProperty(localName = "WltPtyId")
    @NotBlank
    @Length(min = 1, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckInsId(groups = Priority.Lowest.class)
    private String wltPtyId;
    
    /**
     * 钱包ID
     */
    @JacksonXmlProperty(localName = "WltId")
    @NotBlank
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltId;

    /**
     * 钱包类型
     */
    @JacksonXmlProperty(localName = "WltTp")
    @NotBlank
    @Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltTp;

    /**
     * 钱包等级
     */
    @JacksonXmlProperty(localName = "WltLvl")
    @NotBlank
    @Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
    private String wltLvl;

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
