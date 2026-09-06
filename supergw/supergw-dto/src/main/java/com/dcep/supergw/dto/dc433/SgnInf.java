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
 * @version : SgnInf.java v 0.1 2019-08-21
 * @description :修改说明2021-09-28：银行预留手机号、签约人证件类型、签约人证件号码三个字段由必输改为选输，新增开立银行卡的最小天数限制和添加银行卡预留手机号的最小天数限制两个字段
 */
@Getter
@Setter
@ToString
public class SgnInf implements Serializable, DataEncryption {
    private static final long serialVersionUID = -7534543606496022113L;

    /**
     * 签约人银行账户所属运营机构
     */
    @JacksonXmlProperty(localName = "SgnAcctPtyId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1,max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckInsId(groups = Priority.Lower.class)
    private String sgnAcctPtyId;
    
    /**
     * 签约人银行账户类型
     */
    @JacksonXmlProperty(localName = "SgnAcctTp")
    @NotBlank
    @Length(min = 4,max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "AT00||AT01||AT02||AT03")
    private String sgnAcctTp;

    /**
     * 签约人银行账户账号
     * 修改说明2021-09-29：长度改为34
     */
    @JacksonXmlProperty(localName = "SgnAcctId")
    @NotBlank
    @Length(min = 1,max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String sgnAcctId;

    /**
     * 签约人银行账户户名
     */
    @JacksonXmlProperty(localName = "SgnAcctNm")
    @NotBlank
    @Length(min =1 ,max = 60)
    private String sgnAcctNm;
    
    /**
     * 签约人证件类型
     */
    @JacksonXmlProperty(localName = "IDTp")
    @Length(min =4 ,max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "IT[0-9]{2}")
    private String idTp;
    
    /**
     * 签约人证件号码
     */
    @JacksonXmlProperty(localName = "IDNo")
    @Length(min =1 ,max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String idNo;
    
    /**
     * 银行预留手机号码
     */
    @JacksonXmlProperty(localName = "Tel")
    @Length(min =1 ,max = 35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String tel;

    @JacksonXmlProperty(localName = "SgnTp")
    @NotBlank
    @Length(min = 4,max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "SG00||SG01")
    private String sgnTp;

    /**
     * 开立银行卡的最小天数限制
     */
    @JacksonXmlProperty(localName = "OpenCardMinDayLmt")
    @Length(min =1 ,max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String openCardMinDayLmt;

    /**
     * 添加银行卡预留手机号的最小天数限制
     */
    @JacksonXmlProperty(localName = "AddCardPhNoMinDayLmt")
    @Length(min =1 ,max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String addCardPhNoMinDayLmt;

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        if(this.getSgnAcctId() != null){
            this.setSgnAcctId(encryptionHelper.encrypt(this.getSgnAcctId()));
        }
        if(this.getSgnAcctNm() != null){
            this.setSgnAcctNm(encryptionHelper.encrypt(this.getSgnAcctNm()));
        }
        if(this.getIdNo() != null){
            this.setIdNo(encryptionHelper.encrypt(this.getIdNo()));
        }
        if(this.getTel() != null){
            this.setTel(encryptionHelper.encrypt(this.getTel()));
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
        if(this.getIdNo() != null){
            this.setIdNo(encryptionHelper.decrypt(this.getIdNo()));
        }
        if(this.getTel() != null){
            this.setTel(encryptionHelper.decrypt(this.getTel()));
        }
    }
}
