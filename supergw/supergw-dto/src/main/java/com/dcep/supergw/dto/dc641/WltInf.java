/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.dto.dc641;

import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.encryption.TransEncryption;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@ToString
public class WltInf implements TransEncryption {

    @JacksonXmlProperty(localName = "APDU")
    @NotBlank
    @Length(min = 1, max = 1024)
    private String apdu;

    @JacksonXmlProperty(localName = "Pwd")
    @Length(min = 1, max = 64)
    private String pwd;

    @JacksonXmlProperty(localName = "EncTrsKey")
    @Length(min = 1, max = 512)
    private String encTrsKey;

    @JacksonXmlProperty(localName = "VrfyAuthCd")
    @Length(min = 1, max = 64)
    private String vrfyAuthCd;

    @Override
    public void transEncrypt(EncryptionHelper encryptionHelper) {
        if (this.encTrsKey != null) {
            this.setEncTrsKey(encryptionHelper.encrypt(this.encTrsKey));
        }
    }

}
