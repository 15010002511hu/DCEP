package com.dcep.dips.wholesalepayment.dto.summary;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
@Data
public class FundAdjustInfDTO{
    @NotBlank(message = "报文标识号不能为空")
    @Size(max = 32, message = "报文标识号长度不能超过32位")
    private String msgId;
    @NotBlank(message = "报文编号不能为空")
    @Size(max = 15, message = "报文编号长度不能超过15位")
    private String msgTp;
    @NotBlank(message = "资金调整参与机构不能为空")
    @Size(max = 14, message = "资金调整参与机构长度不能超过14位")
    private String transPtyId;
    @Size(max = 14, message = "托管行参与机构长度不能超过14位")
    private String custodian;
    @NotBlank(message = "结算钱包ID不能为空")
    @Size(max = 16, message = "结算钱包ID长度不能超过16位")
    private String walletId;
    @NotNull(message = "资金调整金额不能为空")
    private BigDecimal amount;
    @NotBlank(message = "资金调整类型不能为空")
    @Size(max = 4, message = "资金调整类型长度不能超过4位")
    private String operationType;
    @NotBlank(message = "业务状态不能为空")
    @Size(max = 4, message = "业务状态长度不能超过4位")
    private String prcSts;
    @NotBlank(message = "原大额报文标识号不能为空")
    @Size(max = 20, message = "原大额报文标识号长度不能超过20位")
    private String orgnlMsgId;
    @NotBlank(message = "原大额报文编号不能为空")
    @Size(max = 20, message = "原大额报文编号长度不能超过20位")
    private String orgnlMsgTp;
}
