package com.dcep.dips.wholesalepayment.dto.funding;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.validator.Priority;
import com.dcep.dips.wholesalepayment.dto.BaseDTO;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 大额调增请求DTO
 *
 * @author liuqinliang
 * @version 1.0.0 2025/9/3
 */
@Data
public class IncreaseReqDTO implements RecordDTO, BaseDTO, Serializable {
    private static final long serialVersionUID = -484460816236203027L;

    /**
     * 大额报文标识号
     */
    @NotBlank(groups = Priority.Highest.class, message = "HVPS报文标识号不能为空")
    @Length(min = 16, max = 20)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "HVPS报文标识号禁止中文")
    private String msgId;

    /**
     * 大额报文编号
     */
    @NotBlank(groups = Priority.Highest.class, message = "HVPS报文编号不能为空")
    @Length(min = 15, max = 15)
    private String msgTp;

    /**
     * 端到端标识号
     */
    @NotBlank(groups = Priority.Highest.class, message = "端到端标识号不能为空")
    @Length(max = 35)
    private String endToEndId;

    /**
     * 大额付款机构系统标识
     */
    @Length(max = 35)
    private String clearingSystemId;

    /**
     * 大额发送清算行
     */
    @NotBlank(groups = Priority.Highest.class, message = "大额发送清算行，不能为空")
    @Length(min = 12, max = 12)
    private String clearingMemberId;

    /**
     * 大额发送行
     */
    @NotBlank(groups = Priority.Highest.class, message = "大额发送行，不能为空")
    @Length(min = 12, max = 12)
    private String sendMemberId;

    /**
     * 收款机构在大额系统中，数字人民币运营管理中心下的间参行号
     */
    @NotBlank(groups = Priority.Highest.class, message = "收款机构在大额系统中，数字人民币运营管理中心下的间参行号，不能为空")
    @Length(min = 12, max = 12)
    private String receiveMemberId;

    /**
     * 币种
     */
    @NotBlank(groups = Priority.Highest.class, message = "币种不能为空")
    @Pattern(regexp = "CNY", message = "币种格式不正确,应固定为CNY")
    private String currency;

    /**
     * 金额，元为单位，如 new BigDecimal("12.34")
     */
    @NotNull(groups = Priority.Highest.class, message = "金额不能为空")
    private BigDecimal amount;

    /**
     * 记账日期
     */
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "记账日期格式不正确，应为YYYY-MM-DD")
    private String accountingDate;

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
    public String encode() {
        return JSON.toJSONString(this);
    }

    @Override
    public RecordDTO decode(String encode) {
        return JSONObject.toJavaObject(JSON.parseObject(encode), this.getClass());
    }

    @Override
    public String recMsgId() {
        return this.msgId;
    }

    @Override
    public String recMsgTp() {
        return this.msgTp;
    }

    @Override
    public String recOrgnlMsgId() {
        return null;
    }

    @Override
    public String recOrgnlMsgTp() {
        return null;
    }

    @Override
    public String getReqMsgId() {
        return String.format("%s.%s", this.getSendMemberId(), this.getMsgId());
    }

    @Override
    public String getReqMsgTp() {
        return this.msgTp;
    }

    @Override
    public String getReqSender() {
        return this.sendMemberId;
    }

    @Override
    public String getReqReceiver() {
        return this.receiveMemberId;
    }
}
