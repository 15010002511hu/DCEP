package com.dcep.supergw.dto.dc535;

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
public class WltInf implements Serializable, DataEncryption  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1791530418304092520L;
	/**
     * 钱包ID
     */
    @JacksonXmlProperty(localName = "WltId")
    @NotBlank
    @Length(min = 1, max = 68)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String wltId;
    
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
