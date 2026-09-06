package com.dcep.supergw.dto.dc443;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;


/**
 * ProtocolInformation
 * 协议信息
 * @author chenkai
 */
@JacksonXmlRootElement(localName = "PtcInf")
@Getter
@Setter
@ToString
public class PtcInf implements DataEncryption, Serializable {

    private static final long serialVersionUID = -2146890622546168422L;


    /**
     * 签约协议号
     */
    @JacksonXmlProperty(localName = "PtcId")
    @Length(min=1,max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  ptcId;


    /**
     * 签约渠道
     * SC00：电脑浏览器
     * SC01：手机应用程序
     * SC02：银行柜面
     */
    @JacksonXmlProperty(localName = "SgnChnl")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "SC00||SC01||SC02")
    private String                  sgnChnl;






    /**
     * 单位银行账户所属运营机构编码
     */
    @JacksonXmlProperty(localName = "SgnAcctPtyId")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(max = 14)
    private String                  sgnAcctPtyId;


    /**
     * 单位银行账户类型
     * AT00：个人银行借记账户
     * AT01：个人银行贷记账户
     * AT02：个人银行准贷记账户
     * AT03：单位银行结算账户
     * AT04：基本存款账户
     * AT05：一般存款账户
     * AT06：临时存款账户
     * AT07：DC/EP特殊存款账户
     */
    @JacksonXmlProperty(localName = "SgnAcctTp")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "AT00||AT01||AT02||AT03||AT04||AT05||AT06||AT07")
    private String                  sgnAcctTp;


    /**
     * 单位银行账户账号
     */
    @JacksonXmlProperty(localName = "SgnAcctId")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1,max = 34)
    private String                  sgnAcctId;


    /**
     * 单位银行账户户名
     */
    @JacksonXmlProperty(localName = "SgnAcctNm")
    @Length(min = 1, max = 60)
    private String                  sgnAcctNm;


    /**
     * 单位名称
     */
    @JacksonXmlProperty(localName = "CorprtnNm")
    @Length(min=1,max = 60)
    private String                  corprtnNm;


    /**
     * 单位证明文件类型
     */
    @JacksonXmlProperty(localName = "CorprtnIDTp")
    //2020.6.15 应王喆需求,修改正则校验限制
    @Pattern(regexp = "IT[0-9]{2}")
    private String                  corprtnIDTp;


    /**
     * 单位证明文件号码
     */
    @JacksonXmlProperty(localName = "CorprtnIDNo")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min=1,max = 64)
    private String                  corprtnIDNo;


    /**
     * 法定代表人或单位负责人姓名
     */
    @JacksonXmlProperty(localName = "LglRepNm")
    @Length(min=1,max = 60)
    private String                  lglRepNm;


    /**
     * 法定代表人或单位负责人证件类型
     */
    @JacksonXmlProperty(localName = "LglRepIDTp")
//    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
//    @Pattern(regexp = "IT01")
    //2020.6.4  应王喆需求,修改正则校验限制
    @Pattern(regexp = "IT[0-9]{2}")
    private String                  lglRepIDTp;


    /**
     * 法定代表人或单位负责人证件号码
     */
    @JacksonXmlProperty(localName = "LglRepIDNo")
    @Length(min=1,max = 60)
    private String                  lglRepIDNo;


    /**
     * 单位联系手机号码
     */
    @JacksonXmlProperty(localName = "Tel")
    @Length(min=1,max=35)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String                  tel;


    /**
     * 对公钱包开立所属运营机构编码
     */
    @JacksonXmlProperty(localName = "WltPtyId")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(max = 14)
    private String                  wltPtyId;

    /**
     * --WalletIdentification
     * 对公钱包ID
     */
    @JacksonXmlProperty(localName = "WltId")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(max = 34)
    private String                  wltId;


    /**
     * --WalletType
     * 对公钱包类型
     */
    @JacksonXmlProperty(localName = "WltTp")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "WT09")
    private String                  wltTp;

    /**
     * --WalletLevel
     * 对公钱包等级
     */
    @JacksonXmlProperty(localName = "WltLvl")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "WL01")
    private String                  wltLvl;


    /**
     * --SingleTransactionAmountLimit
     * 单笔兑出业务金额上限
     */
    @JacksonXmlProperty(localName = "SnglTxAmtLmt")
    @Valid
    private ActiveCurrencyAndAmount snglTxAmtLmt;

    /**
     * --DailyTotalCount
     * 日累计业务兑出笔数上限
     */
    @JacksonXmlProperty(localName = "DlTtlCnt")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 8)
    private String                  dlTtlCnt;

    /**
     * --DailyTotalAmountLimit
     * 日累计兑出金额上限
     */
    @JacksonXmlProperty(localName = "DlTtlAmtLmt")
    @Valid
    private ActiveCurrencyAndAmount dlTtlAmtLmt;

    /**
     * --AnnuallyTotalCount
     * 年累计兑出业务笔数上限
     */
    @JacksonXmlProperty(localName = "AnlTtlCnt")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 8)
    private String                  anlTtlCnt;

    /**
     * --AnnuallyTotalAmountLimit
     * 年累计兑出金额上限
     */
    @JacksonXmlProperty(localName = "AnlTtlAmtLmt")
    @Valid
    private ActiveCurrencyAndAmount anlTtlAmtLmt;

    /**
     * --ProtocolEffectiveDate
     * 协议生效日期
     */
    @JacksonXmlProperty(localName = "PtcFctvDt")
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd"
    )
//    @NotBlank(groups = Priority.Highest.class)(groups = Priority.Highest.class)
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
    private String ptcFctvDt;


    /**
     * --ProtocolIneffectiveDate
     * 协议失效日期
     */
    @JacksonXmlProperty(localName = "PtcIfctvDt")
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd"
    )
//    @NotBlank(groups = Priority.Highest.class)(groups = Priority.Highest.class)
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
    private String ptcIfctvDt;

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        if(this.getSgnAcctId() != null){
            this.setSgnAcctId(encryptionHelper.encrypt(this.getSgnAcctId()));
        }
        if(this.getSgnAcctNm() != null){
            this.setSgnAcctNm(encryptionHelper.encrypt(this.getSgnAcctNm()));
        }
        if(this.getLglRepNm() != null){
            this.setLglRepNm(encryptionHelper.encrypt(this.getLglRepNm()));
        }
        if(this.getLglRepIDNo() != null){
            this.setLglRepIDNo(encryptionHelper.encrypt(this.getLglRepIDNo()));
        }
        if(this.getTel() != null){
            this.setTel(encryptionHelper.encrypt(this.getTel()));
        }
        if(this.getWltId() != null){
            this.setWltId(encryptionHelper.encrypt(this.getWltId()));
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
        if(this.getLglRepNm() != null){
            this.setLglRepNm(encryptionHelper.decrypt(this.getLglRepNm()));
        }
        if(this.getLglRepIDNo() != null){
            this.setLglRepIDNo(encryptionHelper.decrypt(this.getLglRepIDNo()));
        }
        if(this.getTel() != null){
            this.setTel(encryptionHelper.decrypt(this.getTel()));
        }
        if(this.getWltId() != null){
            this.setWltId(encryptionHelper.decrypt(this.getWltId()));
        }

    }

}
