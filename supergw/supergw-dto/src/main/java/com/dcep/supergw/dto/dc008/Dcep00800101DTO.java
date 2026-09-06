package com.dcep.supergw.dto.dc008;

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
import com.dcep.supergw.validation.Check008Biz;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * dcep.008.001.01DTO
 *
 * @Author qinchaoyong
 * @date 2024-09-10 11:45:07
 */
@Data
@Gateway(
    msgTp = "dcep.008.001.01", isReturn = true
)
@JacksonXmlRootElement(
    localName = "PdctDtlQryRsp",
    namespace = "http://www.dcep.com/dcep/00800101/"
)
@Check008Biz(groups = Priority.Lowest.class)
public class Dcep00800101DTO extends GwDTO implements DataEncryption {

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
     * 组件ProductDetail
     */
    @JacksonXmlProperty(
        localName = "PdctDtl"
    )
    @Valid
    private PdctDtl pdctDtl;

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
        if (this.pdctDtl != null) {
            if (StringUtils.isNotBlank(this.pdctDtl.getCtctInf())) {
                data.add(this.pdctDtl.getCtctInf());
                data.add(this.pdctDtl.getCtct());
            }
            if(pdctDtl.getSgntrPtyList()!=null&&!pdctDtl.getSgntrPtyList().isEmpty()){
                for(SgntrPty sgntrPty:pdctDtl.getSgntrPtyList()){
                   if(sgntrPty.getSgntrWltInf()!=null){
                       SgntrWltInf wltInf =  sgntrPty.getSgntrWltInf();
                       if(StringUtils.isNotBlank(wltInf.getWltId())){
                           data.add(wltInf.getWltId());
                       }
                       if(StringUtils.isNotBlank(wltInf.getCstmrNm())){
                           data.add(wltInf.getCstmrNm());
                       }
                   }
                }
            }
        }

        return data;
    }

    private void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        int index = 0;
        if (this.pdctDtl != null) {
            if (StringUtils.isNotBlank(this.pdctDtl.getCtctInf())) {
                pdctDtl.setCtctInf(encryptionFeatures.get(index++));
                pdctDtl.setCtct(encryptionFeatures.get(index++));
            }
            if(pdctDtl.getSgntrPtyList()!=null&&!pdctDtl.getSgntrPtyList().isEmpty()){
                for(SgntrPty sgntrPty:pdctDtl.getSgntrPtyList()){
                    if(sgntrPty.getSgntrWltInf()!=null){
                        SgntrWltInf wltInf =  sgntrPty.getSgntrWltInf();
                        if(StringUtils.isNotBlank(wltInf.getWltId())){
                            wltInf.setWltId(encryptionFeatures.get(index++));
                        }
                        if(StringUtils.isNotBlank(wltInf.getCstmrNm())){
                            wltInf.setCstmrNm(encryptionFeatures.get(index++));
                        }
                    }
                }
            }
        }

    }
}
