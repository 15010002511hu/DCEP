package com.dcep.supergw.dto.dc305;

import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.encryption.TransEncryption;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : PwdInf.java v 0.1 2022-10-18
 * Copyright 2022 PBCDCI ALL Rights
 * @description :
 */
@ToString
@Getter
@Setter
public class PwdInf implements TransEncryption {
    @JacksonXmlProperty(localName = "Pwd")
    @Length(min = 1, max = 64)
    String pwd;
    @JacksonXmlProperty(localName = "EncTrsKey")
    @Length(min = 1, max = 512)
    String encTrsKey;

    @Override
    public void transEncrypt(EncryptionHelper encryptionHelper) {
        if (this.encTrsKey != null) {
            this.setEncTrsKey(encryptionHelper.encrypt(this.encTrsKey));
        }
    }
}
