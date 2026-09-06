package com.dcep.supergw.dto.dc442;


import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 跳转信息
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "RdrctInf")
@Getter
@Setter
@ToString
public class RdrctInf implements DataEncryption, Serializable {

    private static final long serialVersionUID = 4959853108034697952L;


    /**
     * 跳转地址
     */
    @JacksonXmlProperty(localName = "RdrctUrl")
    @NotBlank(groups = Priority.Highest.class)
    @Length(max = 256)
    private String   rdrctUrl;


    /**
     * 【加密信息】
     */
    @JacksonXmlProperty(localName = "Sec")
    @NotBlank(groups = Priority.Highest.class)
    private String sec;

    @Override
    public void encryptData(EncryptionHelper encryptionHelper){

        if(this.getSec() != null){
            this.setSec(encryptionHelper.encrypt(this.getSec()));
        }

    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {

        if(this.getSec() != null){
            this.setSec(encryptionHelper.decrypt(this.getSec()));
        }

    }

}
