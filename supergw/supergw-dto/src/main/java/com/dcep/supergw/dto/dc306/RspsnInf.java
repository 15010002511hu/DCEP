package com.dcep.supergw.dto.dc306;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * @Author weiqianjing
 * @Date 2021/9/26 18:17
 * @Description
 */
@Setter
@Getter
@ToString
public class RspsnInf implements Serializable {

    private static final long serialVersionUID = -800262247892654953L;
    /**
     * 业务回执状态 不能为空，码值长度4
     */
    @JacksonXmlProperty(localName = "RspsnSts")
    @NotBlank
    @Pattern(regexp = "PR00||PR01")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 4, max = 4)
    private String rspsnSts;

    /**
     * 业务拒绝码 标签可以不存在，存在必须有值
     */
    @JacksonXmlProperty(localName = "RjctCd")
    @Pattern(regexp = "R[0-9]{3}")
    @Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String rjctCd;

    /**
     * 业务拒绝信息
     */
    @JacksonXmlProperty(localName = "RjctInf")
    @Length(min = 1, max = 105)
    private String rjctInf;
    /**
     * 商户订单号
     */
    @JacksonXmlProperty(localName = "OutOrdrNo")
    @NotBlank
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 64)
    private String outOrdrNo;

    /**
     * 订单金额
     */
    @JacksonXmlProperty(localName = "TrxAmt")
    @NotNull(groups = Priority.Highest.class)
    @Valid
    private ActiveCurrencyAndAmount trxAmt;

    /**
     * 订单号
     * 修改说明：新架构补充需求，新增订单号字段
     */
    @JacksonXmlProperty(localName = "OrdrNo")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 64)
    private String ordrNo;

    /**
     * 订单号生成时间
     */
    @JacksonXmlProperty(localName = "OrdrTm")
    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String ordrTm;

    /**
     * 交易完成时间
     */
    @JacksonXmlProperty(localName = "TrxFinishTm")
    @Pattern(regexp = "(\\d{4}-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2})", message = "日期格式错误，正确格式是：yyyy-MM-ddTHH:mm:ss")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String trxFinishTm;

    /**
     * 交易批次号
     */
    @JacksonXmlProperty(localName = "BatchId")
    @Length(min = 1, max = 13)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String batchId;
    
    /**
     * APDU数据信息
     */
    @JacksonXmlProperty(localName = "APDU")
    @Length(min = 1, max = 1024)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String apdu;
}
