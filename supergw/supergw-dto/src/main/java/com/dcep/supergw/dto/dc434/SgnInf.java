package com.dcep.supergw.dto.dc434;

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

/**
 * @author : maxinyu
 * @version : SgnInf.java v 0.1 2019-08-21
 * @description :
 */
@Getter
@Setter
@ToString
public class SgnInf implements Serializable, DataEncryption {
    private static final long serialVersionUID = -7534543606496022113L;

    /**
     * 签约人银行账户类型
     */
    @JacksonXmlProperty(localName = "SgnAcctTp")
    @NotBlank
    @Length(min = 4,max = 4)
    private String sgnAcctTp;

    /**
     * 签约人银行账户账号
     */
    @JacksonXmlProperty(localName = "SgnAcctId")
    @NotBlank
    @Length(min = 1,max = 34)
    private String sgnAcctId;

    /**
     * 签约人银行账户户名
     */
    @JacksonXmlProperty(localName = "SgnAcctNm")
    @NotBlank
    @Length(min =1 ,max = 60)
    private String sgnAcctNm;

    /**
     * 签约类型
     * 修改说明：2021-09-29，按照最新报文标准该字段改为必输-----回退此校验
     */
    @JacksonXmlProperty(localName = "SgnTp")
    @Length(min = 4,max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "SG00||SG01")
    private String sgnTp;

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        if(this.getSgnAcctId() != null){
            this.setSgnAcctId(encryptionHelper.encrypt(this.getSgnAcctId()));
        }
        if(this.getSgnAcctNm() != null){
            this.setSgnAcctNm(encryptionHelper.encrypt(this.getSgnAcctNm()));
        }
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        if(this.getSgnAcctId() != null){
            this.setSgnAcctId(encryptionHelper.decrypt(this.getSgnAcctId()));
        }
        if(this.getSgnAcctNm() != null){
            this.setSgnAcctNm(encryptionHelper.decrypt(this.getSgnAcctNm()));
        }
    }
}
