package com.dcep.dips.wholesalepayment.dto.summary;

import com.dcep.common.validator.Priority;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class SysSummaryInfoQryRespDTO implements Serializable {

    private static final long serialVersionUID = -4903786285354211242L;

    /**
     * 系统标识 "MCBS JISR BCSP"
     */
    @NotBlank(groups = Priority.Highest.class, message = "系统标识不能为空")
    @Length(min = 1, max = 4)
    private String sysId;

    /**
     * 结算日期
     */
    @NotBlank(groups = Priority.Highest.class, message = "结算日期不能为空")
    @Length(min = 8, max = 8)
    private String bookingDate;

    /**
     * 汇总总笔数
     */
    @NotBlank(groups = Priority.Highest.class, message = "汇总总笔数不能为空")
    private long cntNo;

    /**
     * 汇总总金额
     */
    @NotBlank(groups = Priority.Highest.class, message = "汇总总金额不能为空")
    private BigDecimal cntAmt;

    /**
     * 借方总笔数
     */
    @NotBlank(groups = Priority.Highest.class, message = "借方总笔数不能为空")
    private long dbtCntNo;

    /**
     * 借方总金额
     */
    @NotBlank(groups = Priority.Highest.class, message = "借方总金额不能为空")
    private BigDecimal dbtCntAmt;

    /**
     * 贷方总笔数
     */
    @NotBlank(groups = Priority.Highest.class, message = "贷方总笔数不能为空")
    private long cdtCntNo;

    /**
     * 贷方总金额
     */
    @NotBlank(groups = Priority.Highest.class, message = "贷方总金额不能为空")
    private BigDecimal cdtCntAmt;
}
