package com.dcep.supergw.dto.dc416;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check416Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * 8.3.7　商户及订单查询应答报文<dcep.416.001.01>
 * @author dzj
 */
@JacksonXmlRootElement(localName = "OrdrQryRsp",namespace = "http://www.dcep.com/dcep/41600101/")
@Getter
@Setter
@ToString
@Check416Biz(groups = Priority.Lowest.class)
@Gateway(msgTp ="dcep.416.001.01", isReturn = true)
public class Dcep41600101DTO extends GwDTO implements DataEncryption {

    private static final long serialVersionUID = -1148765956286408079L;

    /**
     * Body报文体
     */

    /**
     * 业务头组件GrpHdr
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lower.class)
    @CheckGrpHdrMsgId(groups = Priority.Lower.class)
    @Valid
    private GrpHdr            grpHdr;

    /**
     * 原报文主键组件
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /**
     * 查询信息
     */
    @JacksonXmlProperty(localName = "BizQryRef")
    @NotNull
    @Valid
    private BizQryRef bizQryRef;

    /**
     * 应答的原业务信息
     */
    @JacksonXmlProperty(localName = "BizRpt")
    @Valid
    private BizRpt            bizRpt;

    /**
     * 应答拒绝信息
     */
    @JacksonXmlProperty(localName = "OprlErr")
    @Valid
    private OprlErr           oprlErr;

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

	@Override
	public boolean check(SoapHeader header) {
        return CheckUtils.responseMsgChk(header, grpHdr, orgnlGrpHdr);
    }

    @Override
    public String fetchResultCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(bizQryRef.getQryRs());
        if (oprlErr != null) {
            if (StringUtils.isNotEmpty(oprlErr.getErr().getRjctCd())) {
                sb.append("-");
                sb.append(oprlErr.getErr().getRjctCd());
            }
        }
        return sb.toString();
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
       List<String> plainTextList = fetchSecretFactor();
       List<String> cipherTextList = encryptionHelper.encrypt(plainTextList);
       secretAssign(cipherTextList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> cipherTextList = fetchSecretFactor();
        List<String> plainTextList = encryptionHelper.decrypt(cipherTextList);
        secretAssign(plainTextList);
    }

    public List<String> fetchSecretFactor() {
        List<String> result = new ArrayList<>();
        if (bizRpt != null) {
            //对明文加密
            if (bizRpt.getCdtr() != null) {
                if(bizRpt.getCdtr().getCdtrWltId() != null){
                    result.add(bizRpt.getCdtr().getCdtrWltId());
                }else{
                    result.add(null);
                }
                if(bizRpt.getCdtr().getCdtrNm() != null){
                    result.add(bizRpt.getCdtr().getCdtrNm());
                }else{
                    result.add(null);
                }
                if(bizRpt.getCdtr().getCdtrMblPhNo() != null){
                    result.add(bizRpt.getCdtr().getCdtrMblPhNo());
                }else{
                    result.add(null);
                }
                if(bizRpt.getCdtr().getCdtrEmailAdr() != null){
                    result.add(bizRpt.getCdtr().getCdtrEmailAdr());
                }else{
                    result.add(null);
                }
            }
        }
        return result;
    }

    public void secretAssign(List<String> secretList) {
        if (bizRpt != null) {
            //对明文加密
            if (bizRpt.getCdtr() != null) {
                if(bizRpt.getCdtr().getCdtrWltId() != null){
                    bizRpt.getCdtr().setCdtrWltId(secretList.get(0));
                }
                if(bizRpt.getCdtr().getCdtrNm() != null){
                    bizRpt.getCdtr().setCdtrNm(secretList.get(1));
                }
                if(bizRpt.getCdtr().getCdtrMblPhNo() != null){
                    bizRpt.getCdtr().setCdtrMblPhNo(secretList.get(2));
                }
                if(bizRpt.getCdtr().getCdtrEmailAdr() != null){
                    bizRpt.getCdtr().setCdtrEmailAdr(secretList.get(3));
                }
            }
        }
    }
}
