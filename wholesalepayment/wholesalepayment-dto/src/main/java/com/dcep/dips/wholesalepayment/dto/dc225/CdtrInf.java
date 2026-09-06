/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc225;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckOrgState;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 
 * @author liuqi
 * @version $Id: CdtrInf.java, v 0.1 2019年8月22日 下午4:26:30 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "CdtrInf")
@Setter
@Getter
@ToString
public class CdtrInf implements Serializable,DataEncryption {
    /**  */
    private static final long serialVersionUID = -7732034558775980582L;

    /*
     * 收款人账户所属运营机构
     */
    @JacksonXmlProperty(localName = "CdtrPtyId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 14, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckOrgState(groups = Priority.Lowest.class)
    private String            cdtrPtyId;

    /*
     * 收款人名称
     */
    @JacksonXmlProperty(localName = "CdtrNm")
    @Length(min = 1, max = 60)
    private String            cdtrNm;

    /*
     * 收款人钱包ID
     */
    @JacksonXmlProperty(localName = "CdtrWltId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 16, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            cdtrWltId;

    /*
     * 收款人钱包等级
     */
    @JacksonXmlProperty(localName = "CdtrWltLvl")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "WL01||WL02||WL03||WL04||WL05")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            cdtrWltLvl;

    /*
     * 收款人钱包类型
     * WT01：个人钱包
     * WT02：子个人钱包
     * WT09：对公钱包
     * WT10：子对公钱包
     * 目前只支持个人钱包和对公钱包
     */
    @JacksonXmlProperty(localName = "CdtrWltTp")
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "WT01||WT09")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            cdtrWltTp;

    /*
     * 收款人钱包名称
     */
    @JacksonXmlProperty(localName = "CdtrWltNm")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 60)
    private String            cdtrWltNm;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(cdtrNm != null) {
			this.setCdtrNm(encryptionHelper.encrypt(cdtrNm));
		}
		if(cdtrWltId != null) {
			this.setCdtrWltId(encryptionHelper.encrypt(cdtrWltId));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(cdtrNm != null) {
			this.setCdtrNm(encryptionHelper.decrypt(cdtrNm));
		}
		if(cdtrWltId != null) {
			this.setCdtrWltId(encryptionHelper.decrypt(cdtrWltId));
		}
	}

}
