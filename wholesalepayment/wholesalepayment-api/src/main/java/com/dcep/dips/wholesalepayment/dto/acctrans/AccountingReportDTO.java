package com.dcep.dips.wholesalepayment.dto.acctrans;


import java.math.BigDecimal;
import lombok.Data;

/**
 * @author: chenxingfeng
 * 结算排队结果通知
 */
@Data
public class AccountingReportDTO {

    /**
     * 交易流水号
     */
    private String transId;

    /**
     * 结算日期
     */
    private String accountingDate;

    /**
     * 结算状态
     */
    private String accountingStatus;

    /**
     * 转出方账户余额 记账成功时填写
     */
    private BigDecimal fromAccountBalance;

    /**
     * 转入方账户余额 记账成功时填写
     */
    private BigDecimal toAccountBalance;

    /**
     * 当前记账采用的系统标志 A-系统A，B-系统B，记账状态成功时填写
     */
    private String currentSystemFlag;

}
