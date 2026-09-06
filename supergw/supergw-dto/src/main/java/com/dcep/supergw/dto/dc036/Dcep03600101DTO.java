package com.dcep.supergw.dto.dc036;

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
import com.dcep.supergw.validation.Check036Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * dcep.036.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-11 09:57:59
 */
@Data
@Gateway(
    msgTp = "dcep.036.001.01", isReturn = true
)
@JacksonXmlRootElement(
    localName = "SgntrnInforChkRsp",
    namespace = "http://www.dcep.com/dcep/03600101/"
)
@Check036Biz(groups = Priority.Lowest.class)
public class Dcep03600101DTO extends GwDTO implements DataEncryption {

    /**
     * 业务头组件
     */
    @JacksonXmlProperty(
        localName = "GrpHdr"
    )
    @NotNull
    @Valid
    @CheckGrpHdrOrgId(
        groups = Priority.Lowest.class
    )
    @CheckGrpHdrMsgId(
        groups = Priority.Lowest.class
    )
    private GrpHdr grpHdr;

    /**
     * 原报文信息
     */
    @JacksonXmlProperty(
        localName = "OrgnlGrpHdr"
    )
    @NotNull
    @Valid
    private OrgnlGrpHdr orgnlGrpHdr;

    /**
     * 组件ResponseInformation
     */
    @JacksonXmlProperty(
        localName = "RspnInf"
    )
    @NotNull
    @Valid
    private RspnInf rspnInf;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        return CheckUtils.responseMsgChk(soapHeader, grpHdr,orgnlGrpHdr);
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> encryptList = encryptionHelper.encrypt(fetchEncryptionFeatures());
        encryptionFeaturesAssign(encryptList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> decryptList = encryptionHelper.decrypt(fetchEncryptionFeatures());
        encryptionFeaturesAssign(decryptList);
    }

    private List<String> fetchEncryptionFeatures() {
        List<String> data = new ArrayList();
        if (this.rspnInf != null && this.rspnInf.getChckRspnInf() != null) {
            List<ChckRspnInf> chckRspnInfList = rspnInf.getChckRspnInf();
            for (ChckRspnInf chckRspnInf : chckRspnInfList) {
                if (chckRspnInf.getSgntrWltInf() != null) {
                    SgntrWltInf sgntrWltInf = chckRspnInf.getSgntrWltInf();
                    if (StringUtils.isNotBlank(sgntrWltInf.getCstmrNm())) {
                        data.add(sgntrWltInf.getCstmrNm());
                    }
                    if (StringUtils.isNotBlank(sgntrWltInf.getWltId())) {
                        data.add(sgntrWltInf.getWltId());
                    }
                }
            }
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.rspnInf != null && this.rspnInf.getChckRspnInf() != null) {
            List<ChckRspnInf> chckRspnInfList = rspnInf.getChckRspnInf();
            for (ChckRspnInf chckRspnInf : chckRspnInfList) {
                if (chckRspnInf.getSgntrWltInf() != null) {
                    SgntrWltInf sgntrWltInf = chckRspnInf.getSgntrWltInf();
                    if (StringUtils.isNotBlank(sgntrWltInf.getCstmrNm())) {
                        sgntrWltInf.setCstmrNm(encryptionFeatures.get(index++));
                    }
                    if (StringUtils.isNotBlank(sgntrWltInf.getWltId())) {
                        sgntrWltInf.setWltId(encryptionFeatures.get(index++));
                    }
                }
            }
        }

    }
}
