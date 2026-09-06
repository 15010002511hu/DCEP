package com.dcep.dips.wholesalepayment.dto.hvps;

import com.dcep.common.validator.Priority;
import com.dcep.dips.wholesalepayment.dto.BaseDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 大额清算通知请求DTO
 *
 * @author liuqinliang
 * @version 1.0.0 2025/9/3
 */
@Data
public class ClearReportReqDTO implements BaseDTO, Serializable {
    private static final long serialVersionUID = -983419855888829303L;

    /**
     * SAPS报文标识号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 16, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String msgId;

    /**
     * SAPS报文编号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 15, max = 15)
    private String msgTp;

    /**
     * 原HVPS报文标识号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 16, max = 16)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlMsgId;

    /**
     * 原HVPS发报参与者ID
     */
    @NotBlank(groups = Priority.Highest.class)
    @Size(min = 12, max = 12, message = "原HVPS发报参与者ID长度必须为12位")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String orgnlSendPty;

    /**
     * 原HVPS报文编号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 15, max = 15)
    private String orgnlMsgTp;

    /**
     * HVPS处理状态
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 4, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String prcStatus;

    /**
     * HVPS处理码
     */
    @Length(max = 12)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String prcCode;

    /**
     * HVPS处理描述信息
     */
    @Length(max = 105)
    private String prcInf;

    /**
     * HVPS结算日期
     */
    private String settlementDate;

    /**
     * 余额
     */
    private String balance;

    /**
     * 大额状态
     */
    @NotBlank(groups = Priority.Highest.class, message = "大额状态，不能为空")
    @Length(min = 2, max = 2)
    private String hvpsSysSts;

    /**
     * 大额报文创建时间
     */
    @NotBlank(groups = Priority.Highest.class, message = "大额报文创建时间，不能为空")
    @Length(min = 19, max = 19)
    private String hvpsCreDtTm;

    @Override
    public String getReqMsgId() {
        return this.orgnlSendPty + "." + this.orgnlMsgId;
    }

    @Override
    public String getReqMsgTp() {
        return this.orgnlMsgTp;
    }

    @Override
    public String getReqSender() {
        return this.orgnlSendPty;
    }

    @Override
    public String getReqReceiver() {
        return null;
    }
}
