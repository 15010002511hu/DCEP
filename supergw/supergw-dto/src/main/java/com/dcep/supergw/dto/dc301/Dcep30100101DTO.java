package com.dcep.supergw.dto.dc301;

import com.dcep.common.annotation.Channel;
import com.dcep.common.annotation.CheckAccountTag;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.annotation.CheckBizCode;
import com.dcep.common.annotation.Gateway;
import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.enums.ChannelEnums;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.CheckUtils;
import com.dcep.common.validator.Priority;
import com.dcep.infocache.validation.CheckGrpHdrMsgId;
import com.dcep.infocache.validation.CheckGrpHdrOrgId;
import com.dcep.supergw.validation.Check301Biz;
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
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : Dcep30100101DTO.java v 0.1 2021-04-13
 * @description :
 */
@Setter
@Getter
@ToString
@JacksonXmlRootElement(localName = "CreOrdrReq", namespace = "http://www.dcep.com/dcep/30100101/")
@Gateway(msgTp = "dcep.301.001.01", channel = @Channel(classname = ChannelEnums.DIRECT_FORWARD))
@Check301Biz(groups = Priority.Lowest.class)
@CheckBizCode(bizCtgyCode = "trxInf.trxCtgyCd", bizTypeCode = "trxInf.trxBizTp")
@CheckAccountTag(type = Type.WID, path = {"cdtrInf.mrchntWltId"})
public class Dcep30100101DTO extends GwDTO implements DataEncryption {

    @Valid
    @NotNull
    @CheckGrpHdrMsgId(groups = Priority.Lowest.class)
    @CheckGrpHdrOrgId(groups = Priority.Lowest.class)
    @JacksonXmlProperty(localName = "GrpHdr")
    private GrpHdr grpHdr;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "TrxInf")
    private TrxInf trxInf;

    @Valid
    @JacksonXmlProperty(localName = "UserInf")
    private UserInf userInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "AcqAgtInf")
    private AcqAgtInf acqAgtInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "CdtrInf")
    private CdtrInf cdtrInf;
    /**
     * 二级商户信息
     */
    @Valid
    @JacksonXmlProperty(localName = "SubMrchntInf")
    private SubMrchntInf subMrchntInf;
    /**
     * 商户受理终端信息
     */
    @Valid
    @JacksonXmlProperty(localName = "MrchntTerInf")
    private MrchntTerInf mrchntTerInf;

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "OrdrInf")
    private OrdrInf ordrInf;

    @Length(min = 1, max = 1024)
    @JacksonXmlProperty(localName = "ClbckUrl")
    private String clbckUrl;

    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "Attach")
    private String attach;

    @Valid
    @JacksonXmlProperty(localName = "PushSubWltInf")
    private PushSubWltInf pushSubWltInf;

    /**
     * 收款人合约实例信息
     */
    @JacksonXmlProperty(
            localName = "CdtrCtrctInst"
    )
    @Valid
    private CdtrCtrctInst cdtrCtrctInst;


    @Valid
    @JacksonXmlProperty(localName = "DbtrInf")
    private DbtrInf dbtrInf;

    @Override
    public void init() {

    }

    @Override
    public String fetchMsgId() {
        return grpHdr.getMsgId();
    }

    @Override
    public boolean check(SoapHeader header) {
        return CheckUtils.requestMsgChk(header, grpHdr);
    }

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> plainTextList = fetchEncryptElement();
        List<String> cipherTestList = encryptionHelper.encrypt(plainTextList);
        encryptAssign(cipherTestList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> cipherTestList = fetchEncryptElement();
        List<String> plainTextList = encryptionHelper.decrypt(cipherTestList);
        encryptAssign(plainTextList);
    }

    private List<String> fetchEncryptElement() {
        if (this.dbtrInf == null) {
            return new ArrayList<>();
        }
        if (this.dbtrInf.getDbtrWltInf() == null) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(this.dbtrInf.getDbtrWltInf().getDbtrNm())) {
            list.add(this.dbtrInf.getDbtrWltInf().getDbtrNm());
        }
        if (StringUtils.isNotBlank(this.dbtrInf.getDbtrWltInf().getDbtrWltId())) {
            list.add(this.dbtrInf.getDbtrWltInf().getDbtrWltId());
        }
        return list;

    }

    private void encryptAssign(List<String> encryptList) {
        if (encryptList.isEmpty()) {
            return;
        }
        int index = 0;
        if (this.dbtrInf != null) {
            //对明文加密
            if (this.dbtrInf.getDbtrWltInf() != null) {
                if (StringUtils.isNotBlank(this.dbtrInf.getDbtrWltInf().getDbtrNm())) {
                    this.dbtrInf.getDbtrWltInf().setDbtrNm(encryptList.get(index++));
                }
                if (StringUtils.isNotBlank(this.dbtrInf.getDbtrWltInf().getDbtrWltId())) {
                    this.dbtrInf.getDbtrWltInf().setDbtrWltId(encryptList.get(index++));
                }
            }
        }
    }

}
