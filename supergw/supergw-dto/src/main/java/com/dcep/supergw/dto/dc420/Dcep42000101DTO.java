package com.dcep.supergw.dto.dc420;

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
import com.dcep.supergw.validation.Check420Biz;
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
 * 8.3.6 对公钱包绑定账户查询应答报文<dcep.420.001.01>
 *
 * @author duzhong
 */
@JacksonXmlRootElement(localName = "BndngAcctQryRsp", namespace = "http://www.dcep.com/dcep/42000101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.420.001.01", isReturn = true)
@Check420Biz(groups = Priority.Lowest.class)
public class Dcep42000101DTO extends GwDTO implements DataEncryption {

    private static final long serialVersionUID = -7231835772911811423L;

    /**
     * 【业务头组件】
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * 原报文主键组件
     */
    @JacksonXmlProperty(localName = "OrgnlGrpHdr")
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /**
     * 【原查询报文信息】
     */
    @JacksonXmlProperty(localName = "BizQryRef")
    @NotNull
    @Valid
    private BizQryRef bizQryRef;

    /**
     * 【应答的原业务信息】
     */
    @JacksonXmlProperty(localName = "BizRpt")
    @Valid
    private BizRpt bizRpt;

    /**
     * 【应答拒绝信息】
     */
    @JacksonXmlProperty(localName = "OprlErr")
    @Valid
    private OprlErr oprlErr;

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
        if (this.bizRpt != null) {
            if (this.bizRpt.getOrgnlTxInf() != null) {
                if (this.bizRpt.getOrgnlTxInf().getSgnAcctId() != null) {
                    result.add(this.bizRpt.getOrgnlTxInf().getSgnAcctId());
                } else {
                    result.add(null);
                }
                if (this.bizRpt.getOrgnlTxInf().getSgnAcctNm() != null) {
                    result.add(this.bizRpt.getOrgnlTxInf().getSgnAcctNm());
                } else {
                    result.add(null);
                }
                if (this.bizRpt.getOrgnlTxInf().getLglRepNm() != null) {
                    result.add(this.bizRpt.getOrgnlTxInf().getLglRepNm());
                } else {
                    result.add(null);
                }
                if (this.bizRpt.getOrgnlTxInf().getLglRepIDNo() != null) {
                    result.add(this.bizRpt.getOrgnlTxInf().getLglRepIDNo());
                } else {
                    result.add(null);
                }
                if (this.bizRpt.getOrgnlTxInf().getTel() != null) {
                    result.add(this.bizRpt.getOrgnlTxInf().getTel());
                } else {
                    result.add(null);
                }
            }
        }
        return result;
    }

    public void secretAssign(List<String> secretList) {
        if (this.bizRpt != null) {
            if (this.bizRpt.getOrgnlTxInf() != null) {
                if (this.bizRpt.getOrgnlTxInf().getSgnAcctId() != null) {
                    this.bizRpt.getOrgnlTxInf().setSgnAcctId(secretList.get(0));
                }
                if (this.bizRpt.getOrgnlTxInf().getSgnAcctNm() != null) {
                    this.bizRpt.getOrgnlTxInf().setSgnAcctNm(secretList.get(1));
                }
                if (this.bizRpt.getOrgnlTxInf().getLglRepNm() != null) {
                    this.bizRpt.getOrgnlTxInf().setLglRepNm(secretList.get(2));
                }
                if (this.bizRpt.getOrgnlTxInf().getLglRepIDNo() != null) {
                    this.bizRpt.getOrgnlTxInf().setLglRepIDNo(secretList.get(3));
                }
                if (this.bizRpt.getOrgnlTxInf().getTel() != null) {
                    this.bizRpt.getOrgnlTxInf().setTel(secretList.get(4));
                }
            }
        }
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
}