package com.dcep.supergw.dto.dc312;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
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
import org.hibernate.validator.constraints.Length;

/**
 * 原业务信息
 *
 * @author duzhong
 */
@Getter
@Setter
@ToString
public class OrgnlTxInf implements Serializable, DataEncryption {

    /**
     *
     */
    private static final long serialVersionUID = 5041452821763496472L;

    /*
     * 原报文标识号
     */
    @JacksonXmlProperty(localName = "OrgnlMsgId")
    @NotBlank
    @Length(min = 1, max = 35)
    private String orgnlMsgId;

    /*
     * 原发起机构
     */
    @JacksonXmlProperty(localName = "OrgnlInstgPty")
    @NotBlank
    @Length(min = 1, max = 14)
    private String orgnlInstgPty;

    /*
     * 原报文编号
     */
    @JacksonXmlProperty(localName = "OrgnlMT")
    @NotBlank
    @Length(min = 1, max = 16)
    private String orgnlMT;

    /*
     * 原订单金额
     */
    @JacksonXmlProperty(localName = "OrgnlTrxAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount orgnlTrxAmt;

    /*
     * 原商户订单号
     */
    @JacksonXmlProperty(localName = "OrgnlOutOrdrNo")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 64)
    private String orgnlOutOrdrNo;

    /*
     * 原订单号生成时间
     */
    @JacksonXmlProperty(localName = "OrgnlOrdrTm")
    @NotBlank
    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
    private String orgnlOrdrTm;

    /*
     * 原订单号
     */
    @JacksonXmlProperty(localName = "OrgnlOrdrNo")
    @Length(min = 1, max = 64)
    private String orgnlOrdrNo;

    /*
     * 原交易完成时间
     */
    @JacksonXmlProperty(localName = "OrgnlTrxFinishTm")
    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
    private String orgnlTrxFinishTm;

    /*
     * 原交易批次号
     */
    @JacksonXmlProperty(localName = "OrgnlBatchId")
    @Length(min = 1, max = 13)
    private String orgnlBatchId;

    /**
     * 原付款钱包ID
     */
    @JacksonXmlProperty(localName = "OrgnlDbtrWltId")
    @Length(min = 1, max = 34)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlDbtrWltId;

    /*
     * 原终端支付支持类型
     */
    @JacksonXmlProperty(localName = "BizPayMtd")
    @Pattern(regexp = "BPOM00||BPOM01||BPOM02")
    private String bizPayMtd;

    /**
     * 跳转信息
     */
    @Valid
    @JacksonXmlProperty(localName = "RdrctInf")
    private RdrctInf rdrctInf;

    @Override
    public void encryptData(EncryptionHelper encryptionHelper) {
        List<String> plainTextList = fetchEncryptElement();
        List<String> encryptTextList = encryptionHelper.encrypt(plainTextList);
        encryptAssign(encryptTextList);
    }

    @Override
    public void decryptData(EncryptionHelper encryptionHelper) {
        List<String> encryptTextList = fetchEncryptElement();
        List<String> plainTextList = encryptionHelper.decrypt(encryptTextList);
        encryptAssign(plainTextList);
    }

    private List<String> fetchEncryptElement() {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(orgnlDbtrWltId)) {
            list.add(orgnlDbtrWltId);
        }
        return list;
    }

    private void encryptAssign(List<String> encryptList) {
        int index = 0;
        if (StringUtils.isNotBlank(orgnlDbtrWltId)) {
            setOrgnlDbtrWltId(encryptList.get(index));
        }
    }
}
