package com.dcep.dips.wholesalepayment.dto.chain;



import com.dcep.common.validator.Priority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@ToString
public class OnChainTransReqDTO implements Serializable {
    private static final long serialVersionUID = -4096817157521946393L;
    /**
     * 报文标识号 TODO接口未标明最小长度
     */
    @NotBlank(groups = Priority.Highest.class, message = "报文标识号不能为空")
    @Length(min = 16, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "报文标识号禁止中文")
    private String msgId;

    /**
     * 报文批次号
     */
    @NotBlank(groups = Priority.Highest.class, message = "报文批次号不能为空")
    @Length(min = 1, max = 15)
    private String batId;

    /**
     * 交易时间
     */
    @NotBlank(groups = Priority.Highest.class, message = "交易时间不能为空")
    private Date bizDt;

    /**
     * 交易金额
     */
    @NotBlank(groups = Priority.Highest.class, message = "交易金额不能为空")
    private BigDecimal transAmt;

    /**
     * 渠道系统
     */
    @NotBlank(groups = Priority.Highest.class, message = "渠道系统不能为空")
    @Length(min = 1, max = 8)
    private String channelSys;

    /**
     * 使用系统标志
     */
    @NotBlank(groups = Priority.Highest.class, message = "使用系统标志不能为空")
    @Pattern(regexp = "A||B")
    private String useCurrentSystemFlag;

    /**
     * 业务类型
     */
    @Length(max = 4, message = "业务类型长度超限")
    private String bizTp;

    /**
     * 交易状态
     */
    @NotNull(groups = Priority.Highest.class, message = "交易状态不能为空")
    @Pattern(regexp = "PR00")
    private String trans_sts;

    /**
     * 付款机构编码
     */
    @NotBlank(groups = Priority.Highest.class, message = "付款机构编码不能为空")
    @Length(min = 1, max = 14)
    private String dbtrPtyId;

    /**
     * 付款钱包ID
     */
    @Length( max = 16, message = "付款钱包ID长度超限")
    private String dbtrWltId;

    /**
     * 付款机构系统标识
     */
    @NotBlank(groups = Priority.Highest.class, message = "付款机构系统标识不能为空")
    @Length(min = 1, max = 8)
    private String dbtrSysId;

    /**
     * 收款机构编码
     */
    @NotBlank(groups = Priority.Highest.class, message = "收款钱包ID不能为空")
    @Length(min = 1, max = 14)
    private String cdtrPtyId;

    /**
     * 收款钱包ID
     */
    @Length(max = 16, message = "收款钱包ID长度超限")
    private String cdtrWltId;

    /**
     * 收款机构系统标识
     */
    @NotBlank(groups = Priority.Highest.class, message = "收款机构系统标识不能为空")
    @Length(min = 1, max = 8)
    private String cdtrSysId;

}
