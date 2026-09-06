/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc221;

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
 * @version $Id: DbtrInf.java, v 0.1 2019年8月22日 下午4:26:11 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "DbtrInf")
@Setter
@Getter
@ToString
public class DbtrInf implements Serializable,DataEncryption {
    /**  */
    private static final long serialVersionUID = 6554616528096429293L;

    /*
     * 付款人钱包所属运营机构
     */
    @JacksonXmlProperty(localName = "DbtrPtyId")
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 14, max = 14)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @CheckOrgState(groups = Priority.Lowest.class)
    private String            dbtrPtyId;

    /*
     * 付款人名称
     */
    @JacksonXmlProperty(localName = "DbtrNm")
    @Length(min = 1, max = 60)
    private String            dbtrNm;

    /*
     * 付款人钱包ID
     */
    @JacksonXmlProperty(localName = "DbtrWltId")
    @NotBlank
    @Length(min = 16, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            dbtrWltId;

    /*
     * 付款人钱包等级
     */
    @JacksonXmlProperty(localName = "DbtrWltLvl")
    @NotBlank
    @Length(min = 4, max = 4)
    @Pattern(regexp = "WL01||WL02||WL03||WL04")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            dbtrWltLvl;

    /*
     * 付款人钱包类型
     * 目前只支持个人钱包WT01和对公钱包WT09
     */
    @JacksonXmlProperty(localName = "DbtrWltTp")
    @NotBlank
    @Length(min = 4, max = 4)
    @Pattern(regexp = "WT01||WT09")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            dbtrWltTp;

    /*
     * 付款人钱包名称
     */
    @JacksonXmlProperty(localName = "DbtrWltNm")
    @NotBlank
    @Length(min = 1, max = 60)
    private String            dbtrWltNm;

	@Override
	public void encryptData(EncryptionHelper encryptionHelper) {
		if(dbtrNm != null) {
			this.setDbtrNm(encryptionHelper.encrypt(dbtrNm));
		}
		if(dbtrWltId != null) {
			this.setDbtrWltId(encryptionHelper.encrypt(dbtrWltId));
		}
	}

	@Override
	public void decryptData(EncryptionHelper encryptionHelper) {
		if(dbtrNm != null) {
			this.setDbtrNm(encryptionHelper.decrypt(dbtrNm));
		}
		if(dbtrWltId != null) {
			this.setDbtrWltId(encryptionHelper.decrypt(dbtrWltId));
		}
	}

}
