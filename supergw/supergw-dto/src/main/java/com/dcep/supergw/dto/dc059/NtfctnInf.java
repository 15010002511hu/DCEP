package com.dcep.supergw.dto.dc059;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 合约消息通知内容
 *
 * @Author qinchaoyong
 * @date 2024-11-22 09:26:35
 */
@Data
public class NtfctnInf implements Serializable {
    /**
     * 交易标识号
     */
    @JacksonXmlProperty(
            localName = "TxId"
    )
    @Length(
            min = 1,
            max = 35
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String txId;

    @JacksonXmlProperty(
        localName = "PreCurrCtrctCmdId"
    )
    @Length(
        min = 1,
        max = 64
    )
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    String preCurrCtrctCmdId;

    @JacksonXmlProperty(
        localName = "CurrCtrctCmdId"
    )
    @Length(
        min = 1,
        max = 64
    )
    @NotBlank
    @Pattern(
        regexp = "[^\u4e00-\u9fa5]*",
        message = "禁止中文"
    )
    String currCtrctCmdId;

    @JacksonXmlElementWrapper(
        localName = "NextCtrctCmdIdList"
    )
    @JacksonXmlProperty(
        localName = "NextCtrctCmdId"
    )
    @Valid
    List<String> nextCtrctCmdIdList;

    
    /**
     * 合约实例操作类型
     */
    @JacksonXmlProperty(
            localName = "CtrctInstOprTp"
    )
    @Pattern(regexp = "SCOCT[0-9]{2}")
    @NotBlank
    private String ctrctInstOprTp;
    
    /**
     * 交易类型
     */
    @JacksonXmlProperty(
            localName = "TxTp"
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String txTp;

    /**
     * 业务类型编码
     */
    @JacksonXmlProperty(
            localName = "BizCtgyCd"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String bizCtgyCd;

    /**
     * 业务种类编码
     */
    @JacksonXmlProperty(
            localName = "BizTpCd"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String bizTpCd;

    /**
     * 支付订单号
     */
    @JacksonXmlProperty(
            localName = "PmtTxId"
    )
    @Length(
            min = 1,
            max = 64
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String pmtTxId;

    /**
     * 发起方信息
     */
    @JacksonXmlProperty(
            localName = "SndrInf"
    )
    @NotNull
    @Valid
    private SndrInf sndrInf;

    /**
     * 接收方信息
     */
    @JacksonXmlProperty(
            localName = "RcvrInf"
    )
    @NotNull
    @Valid
    private RcvrInf rcvrInf;

    /**
     * 合约参数
     */
    @JacksonXmlProperty(
            localName = "CtrctCallParam"
    )
    @Valid
    private CtrctCallParam ctrctCallParam;

    /**
     * 业务处理时间
     */
    @JacksonXmlProperty(
            localName = "CreDtTm"
    )
    @JsonFormat(
            locale = "zh",
            timezone = "GMT+8",
            pattern = "yyyy-MM-dd'T'HH:mm:ss"
    )
    @NotBlank
    @Pattern(
            regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})?",
            message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss"
    )
    private String creDtTm;

    /**
     * 业务回执状态
     */
    @JacksonXmlProperty(
            localName = "StsId"
    )
    @NotBlank
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String stsId;

    /**
     * 业务拒绝码
     */
    @JacksonXmlProperty(
            localName = "RjctCd"
    )
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(
            localName = "RjctInf"
    )
    @Length(
            min = 1,
            max = 105
    )
    private String rjctInf;

    /**
     * 合约应答
     */
    @JacksonXmlProperty(
            localName = "CtrctRspn"
    )
    @Valid
    private CtrctRspn ctrctRspn;
}
