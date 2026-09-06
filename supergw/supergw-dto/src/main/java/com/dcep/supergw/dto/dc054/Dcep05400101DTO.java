package com.dcep.supergw.dto.dc054;

import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.OrgnlGrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * dcep.054.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-11 10:49:29
 */
@Data
@Gateway(
        msgTp = "dcep.054.001.01", isReturn = true
)
@JacksonXmlRootElement(
        localName = "CtrctCclCnfrmgRspn",
        namespace = "http://www.dcep.com/dcep/05400101/"
)
public class Dcep05400101DTO extends GwDTO implements DataEncryption {
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
     * 原报文主键组件
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

    /**
     * 合约解约记录
     */
    @JacksonXmlProperty(
            localName = "CtrctCclRcrd"
    )
    @Valid
    private CtrctCclRcrd ctrctCclRcrd;

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader soapHeader) {
        return true;
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
        if (this.ctrctCclRcrd != null && this.ctrctCclRcrd.getCtrctPtyList() != null
                && this.ctrctCclRcrd.getCtrctPtyList().size() > 0) {
            for (CtrctPty ctrctPty : this.ctrctCclRcrd.getCtrctPtyList()) {
                WltInf wltInf = ctrctPty.getWltInf();
                if (null != wltInf) {
                    if (StringUtils.isNotBlank(wltInf.getCstmrNm())) {
                        data.add(wltInf.getCstmrNm());
                    }

                    if (StringUtils.isNotBlank(wltInf.getWltId())) {
                        data.add(wltInf.getWltId() );
                    }
                }
            }
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;

        if (this.ctrctCclRcrd != null && this.ctrctCclRcrd.getCtrctPtyList() != null
                && this.ctrctCclRcrd.getCtrctPtyList().size() > 0) {
            for (CtrctPty ctrctPty : this.ctrctCclRcrd.getCtrctPtyList()) {
                WltInf wltInf = ctrctPty.getWltInf();
                if (null != wltInf) {
                    if (StringUtils.isNotBlank(wltInf.getCstmrNm())) {
                        wltInf.setCstmrNm(encryptionFeatures.get(index++));
                    }

                    if (StringUtils.isNotBlank(wltInf.getWltId())) {
                        wltInf.setWltId(encryptionFeatures.get(index++));
                    }
                }
            }
        }
    }
}
