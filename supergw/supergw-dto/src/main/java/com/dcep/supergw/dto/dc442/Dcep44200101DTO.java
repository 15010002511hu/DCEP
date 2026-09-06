package com.dcep.supergw.dto.dc442;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check442Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;


/**
 * 8.3.17 对公钱包绑定账户应答报文<dcep.442.001.01>
 *
 * @author huangyang
 */
@JacksonXmlRootElement(localName = "BndngAcctPtcMgmtRsp", namespace = "http://www.dcep.com/dcep/44200101/")
@Getter
@Setter
@ToString
@Gateway(msgTp = "dcep.442.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@Check442Biz(groups = Priority.Lowest.class)
public class Dcep44200101DTO extends GwDTO implements DataEncryption {

    private static final long serialVersionUID = -2145059527207661908L;


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
     * 【业务回执信息】
     */
    @JacksonXmlProperty(localName = "RspsnInf")
    @NotNull
    @Valid
    private RspsnInf rspsnInf;

    /**
     * 【管理类型】
     * MT01：身份认证
     * MT02：身份确认
     * MT03：解约申请
     * MT04：解约通知
     * MT05：网关签约
     * MT06：普通签约
     */
    @JacksonXmlProperty(localName = "MgmtTp")
    @Pattern(regexp = "MT01||MT02||MT03||MT04||MT05||MT06")
    @NotBlank(groups = Priority.Highest.class)
    private String mgmtTp;

    /**
     * 【渠道信息】
     */
    @JacksonXmlProperty(localName = "ChnlInf")
    @Valid
    private ChnlInf chnlInf;

    /**
     * 【跳转信息】
     */
    @JacksonXmlProperty(localName = "RdrctInf")
    @Valid
    private RdrctInf rdrctInf;


    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override

    public boolean check(SoapHeader header) {
//        //当管理类型为“MT06”时该域必填填写
//        if("MT06".equals(this.getMgmtTp()) && "PR00".equals(this.getRspsnInf().getRspsnSts()) && this.getChnlInf()==null)
//            throw new DcepException(ErrorEnum.NULL_ERROR);
//            //当管理类型为“MT05”时该域必填填写
//        else if("MT05".equals(this.getMgmtTp()) && "PR00".equals(this.getRspsnInf().getRspsnSts()) && this.getRdrctInf()==null)
//            throw new DcepException(ErrorEnum.NULL_ERROR);

        return CheckUtils.responseMsgChk(header, grpHdr, orgnlGrpHdr);

    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> plainTextList =  fetchSecretFactor();
        List<String> cipherTextList =  encryptionHelper.encrypt(plainTextList);
        secretAssign(cipherTextList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> cipherTextList =  fetchSecretFactor();
        List<String> plainTextList =  encryptionHelper.decrypt(cipherTextList);
        secretAssign(plainTextList);
    }

    public List<String> fetchSecretFactor() {
        List<String> result = new ArrayList<>();
        if (rdrctInf != null) {
            if (rdrctInf.getSec() != null) {
                result.add(rdrctInf.getSec());
            } else {
                result.add(null);
            }
        }
        return result;
    }

    public void secretAssign(List<String> secretList) {
        if (rdrctInf != null) {
            if (rdrctInf.getSec() != null) {
                rdrctInf.setSec(secretList.get(0));
            }
        }
    }

    @Override
    public String fetchResultCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(rspsnInf.getRspsnSts());
        if (StringUtils.isNotEmpty(rspsnInf.getRjctCd())) {
            sb.append("-");
            sb.append(rspsnInf.getRjctCd());
        }
        return sb.toString();
    }
}