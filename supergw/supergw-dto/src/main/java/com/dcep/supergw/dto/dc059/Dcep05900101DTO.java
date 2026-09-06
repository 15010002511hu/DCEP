package com.dcep.supergw.dto.dc059;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check059Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * dcep.059.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:37
 */
@Data
@Gateway(
        msgTp = "dcep.059.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD)
)
@JacksonXmlRootElement(
        localName = "CtrctMsgNotf",
        namespace = "http://www.dcep.com/dcep/05900101/"
)
@Check059Biz(groups = Priority.Lowest.class)
public class Dcep05900101DTO extends GwDTO implements DataEncryption {
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
     * 合约消息通知内容
     */
    @JacksonXmlProperty(
            localName = "NtfctnInf"
    )
    @NotNull
    @Valid
    private NtfctnInf ntfctnInf;

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
        if (this.ntfctnInf != null
        ) {
            if (this.ntfctnInf.getSndrInf() != null && this.ntfctnInf.getSndrInf().getSndrCstmr() != null) {
                SndrCstmr sndrCstmr = this.ntfctnInf.getSndrInf().getSndrCstmr();
                if (StringUtils.isNotBlank(sndrCstmr.getSndrCstmrNm())) {
                    data.add(sndrCstmr.getSndrCstmrNm());
                }

                if (StringUtils.isNotBlank(sndrCstmr.getSndrWltId())) {
                    data.add(sndrCstmr.getSndrWltId());
                }
            }

            if (this.ntfctnInf.getRcvrInf() != null && this.ntfctnInf.getRcvrInf().getRcvrCstmr() != null) {
                RcvrCstmr rcvrCstmr = this.ntfctnInf.getRcvrInf().getRcvrCstmr();
                if (StringUtils.isNotBlank(rcvrCstmr.getRcvrCstmrNm())) {
                    data.add(rcvrCstmr.getRcvrCstmrNm());
                }

                if (StringUtils.isNotBlank(rcvrCstmr.getRcvrWltId())) {
                    data.add(rcvrCstmr.getRcvrWltId());
                }
            }

        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;

        if (this.ntfctnInf != null && this.ntfctnInf.getSndrInf() != null
                && this.ntfctnInf.getSndrInf().getSndrCstmr() != null) {
            SndrCstmr sndrCstmr = this.ntfctnInf.getSndrInf().getSndrCstmr();
            if (StringUtils.isNotBlank(sndrCstmr.getSndrCstmrNm())) {
                sndrCstmr.setSndrCstmrNm(encryptionFeatures.get(index++));
            }

            if (StringUtils.isNotBlank(sndrCstmr.getSndrWltId())) {
                sndrCstmr.setSndrWltId(encryptionFeatures.get(index++));
            }

        }
    }
}
