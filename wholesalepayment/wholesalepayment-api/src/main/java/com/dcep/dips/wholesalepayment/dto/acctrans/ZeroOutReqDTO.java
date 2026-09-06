package com.dcep.dips.wholesalepayment.dto.acctrans;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.dcep.common.validator.Priority;
import com.dcep.dips.wholesalepayment.dto.BaseDTO;
import com.dcep.dips.wholesalepayment.dto.RecordDTO;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 结算钱包清零请求DTO
 *
 * @author liuqinliang
 * @version 1.0.0 2025/9/3
 */
@Data
public class ZeroOutReqDTO implements RecordDTO, BaseDTO, Serializable {
    private static final long serialVersionUID = -382933124034923519L;

    /**
     * 交易流水号
     */
    @NotBlank(groups = Priority.Highest.class, message = "交易流水号，不能为空")
    @Length(min = 1, max = 35)
    private String transId;

    /**
     * 业务流水号
     */
    @Length(max = 50)
    private String bizFlowNo;

    /**
     * 付款清算行机构标识（数币运营中心在ACS机构标识）
     */
    @NotBlank(groups = Priority.Highest.class, message = "付款清算行机构标识，不能为空")
    @Length(min = 1, max = 35)
    private String dbtrClearingMemberId;

    /**
     * 收款清算行机构标识（同被清零机构标识）
     */
    @NotBlank(groups = Priority.Highest.class, message = "收款清算行机构标识，不能为空")
    @Length(min = 1, max = 35)
    private String cdtrClearingMemberId;

    /**
     * 被清零机构标识
     */
    @NotBlank(groups = Priority.Highest.class, message = "被清零机构标识，不能为空")
    @Length(min = 1, max = 35)
    private String memberId;

    /**
     * 业务类型
     */
    @NotBlank(groups = Priority.Highest.class, message = "业务类型，不能为空")
    @Length(min = 3, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "业务类型，禁止中文")
    private String bizType;

    /**
     * 业务种类
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "^[0-9]{5,8}$", message = "业务种类，仅允许5-8位数字")
    private String bizKind;

    /**
     * 业务优先级
     */
    @NotBlank(groups = Priority.Highest.class, message = "业务优先级，不能为空")
    @Length(min = 3, max = 4)
    private String bizPriority;

    /**
     * 币种
     */
    @NotBlank(groups = Priority.Highest.class, message = "币种，不能为空")
    @Length(min = 3, max = 3)
    private String currency;

    /**
     * 金额，单位为分，格式：BigDecimal(18,0)
     */
    @NotNull(groups = Priority.Highest.class, message = "金额，不能为空")
    private BigDecimal amount;

    /**
     * 记账日期
     */
    @Pattern(regexp = "^\\d{8}$", message = "记账日期，格式不正确，应为YYYYMMDD")
    private String accountingDate;

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
        return this.transId;
    }

    @Override
    public String recMsgTp() {
        return "dcep.185.001.01";
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
        return this.transId;
    }

    @Override
    public String getReqMsgTp() {
        return "hvps.112.001.01";
    }

    @Override
    public String getReqSender() {
        return "acctrans";
    }

    @Override
    public String getReqReceiver() {
        return this.memberId;
    }
}
