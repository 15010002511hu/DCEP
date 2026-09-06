package com.dcep.supergw.dto.dc391;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.annotation.GwMethod;
import com.dcep.common.annotation.RpcInfo;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.routing.annotation.Routing;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 钱包信息上链请求报文<dcep.391.001.01>
 * 
 * @author laimincai
 * @version 1.0.0
 *
 */
@JacksonXmlRootElement(localName = "WltInfOnChainReq",
        namespace = "http://www.dcep.com/dcep/39100101/")
@Setter
@Getter
@ToString
@Gateway(msgTp = "dcep.391.001.01",
        channel = @Channel(classname = ChannelEnums.CENTRAL_PROCESS,
                services = {@RpcInfo(name = "com.dcep.routing.api.Hlht2Shipping",
                        methods = {@GwMethod(name = "execute")})}))
@Routing(name = "clearing")
public class Dcep39100101DTO extends GwDTO implements DataEncryption {

    /**
     * 
     */
    private static final long serialVersionUID = -1283364706006772513L;

    public Dcep39100101DTO() {

    }

    /**
     * Body报文体
     */

    /**
     * 业务头组件GrpHdr
     */
    @JacksonXmlProperty(localName = "GrpHdr")
    @NotNull(groups = Priority.Highest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @Valid
    private GrpHdr grpHdr;

    /**
     * 钱包信息
     */
    @Valid
    @NotNull
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "WltInf")
    private List<WltInf> wltInfList;

    @Override
    public boolean check(SoapHeader header) {
        return true;
    }

    @Override
    public String fetchResultCode() {
        return "PR00-";
    }

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return this.getGrpHdr().getMsgId();
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

    public List<String> fetchEncryptionFeatures() {
        // 获取需加解密处理的敏感要素，若为空则赋值为null，保证前后顺序一致
        List<String> data = new ArrayList<>();
        if (null != wltInfList && !wltInfList.isEmpty()) {
            for (WltInf wltInf : wltInfList) {
                data.add(wltInf.getWltId());
            }
        }
        return data;
    }

    public void encryptionFeaturesAssign(List<String> encryptionFeatures) {
        // 按顺序赋值加解密处理后的敏感要素
        for (int i = 0; i < encryptionFeatures.size(); i++) {
            wltInfList.get(i).setWltId(encryptionFeatures.get(i));
        }
    }
}
