package com.dcep.dips.wholesalepayment.dto.chain;


import com.dcep.common.validator.Priority;
import com.dcep.dips.wholesalepayment.dto.BaseDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;

@Setter
@Getter
@ToString
public class OnChainAdjustReqDTO  implements BaseDTO {
    /**
     * 报文标识号
     */
    @NotBlank(message = "交易标识号不能为空")
    @Length(min = 16, max = 32,message = "交易标识号长度超过限制")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "交易标识号禁止中文")
    private String msgId;


    /**
     * 机构编码
     */
    @NotBlank(groups = Priority.Highest.class, message = "机构编码不能为空")
    @Length(min = 1, max = 14,message = "机构编码长度超过限制")
    private String clearingMemberId;

    /**
     * 机构系统标识  BCSP
     */
    @NotNull(groups = Priority.Highest.class, message = "机构系统标识不能为空")
    @Length(min = 1, max = 10,message = "机构系统标识长度超过限制")
    private String clearingSystemId;
    /**
     * 机构结算钱包ID
     */
//    @NotNull(groups = Priority.Highest.class, message = "机构结算钱包ID不能为空")
    @Length(min = 1, max = 20,message = "机构结算钱包ID长度超过限制")
    private String clearingWalletId;

    /**
     * 期望结算日期
     */
    @NotNull(groups = Priority.Highest.class, message = "期望结算日期不能为空")
    @Length(min = 8, max = 8,message = "期望结算日期长度超过限制")
    private String expectedSettlementDate;
    /**
     * 交易批次号
     */
    @Length(min = 1, max = 13,message = "交易批次号长度超过限制")
    private String batchId;
    /**
     * 业务类型
     */
    @NotNull(groups = Priority.Highest.class, message = "业务类型不能为空")
    @Length(min = 1, max = 4,message = "业务类型长度超过限制")
    private String bizTp;
    /**
     * 业务种类
     */
    @NotNull(groups = Priority.Highest.class, message = "期望结算日期不能为空")
    @Length(min = 1, max = 8,message = "业务种类请求长度超过限制")
    private String bizKind;
    /**
     * 调整类型  QOT01 调增上链、QOT02 调减下链
     */
    @NotNull(groups = Priority.Highest.class, message = "调整类型不能为空")
    @Pattern(regexp = "QOT01||QOT02",message = "调整类型只能是QOT01或QOT02")
    private String adjustTp;
    /**
     * 调整币种 CNY
     */
    @NotBlank(groups = Priority.Highest.class, message = "调整币种不能为空")
    @Pattern(regexp = "CNY",message = "调整币种只能是CNY")
    private String currency;

    /**
     * 调整金额，单位为元，格式：BigDecimal(18,2)
     */
    @NotNull(groups = Priority.Highest.class, message = "调整金额不能为空")
    private BigDecimal amount;
    /**
     * 使用系统标识
     */
    @NotNull(groups = Priority.Highest.class, message = "使用系统标识不能为空")
    @Pattern(regexp = "A||B",message = "使用系统标识只能是A或B")
    private String useCurrentSystemFlag;
    /**
     * 备注
     */
    @Length(max = 200, message = "备注信息超过长度限制")
    private String remark;

    @Override
    public String getReqMsgId() {
        return this.msgId;
    }

    @Override
    public String getReqMsgSn() {
        return this.msgId;


    }

    @Override
    public String getReqMsgTp() {
        return null;
    }

    @Override
    public String getReqSender() {
        return this.clearingMemberId;
    }

    @Override
    public String getReqReceiver() {
        return this.clearingMemberId;
    }
}
