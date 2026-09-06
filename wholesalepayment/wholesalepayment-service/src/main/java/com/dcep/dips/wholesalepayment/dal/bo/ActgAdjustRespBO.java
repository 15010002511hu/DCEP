package com.dcep.dips.wholesalepayment.dal.bo;

import com.dcep.dips.acctrans.constants.Constant;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustNotifyRespDTO;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustRespDTO;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
@Data
public class ActgAdjustRespBO {
    private String accountingDate;
    private String accountingStatus;
    private String walletId;
    private BigDecimal accountBalance;
    private String currentSystemFlag;
    private BigDecimal ciLimit;
    private BigDecimal netQuota;
    private String bizCode;
    private String bizMsg;

    private void logActgStatus() {
        if (Constant.AccountingStatus.SUCCESS.equals(accountingStatus)) {
            log.info("结算钱包应答：accountingStatus={}(成功)", accountingStatus);
        } else {
            log.info("结算钱包应答：accountingStatus={}(失败)，bizCode={}, bizMsg={}", accountingStatus, bizCode, bizMsg);
        }
    }

    public ActgAdjustRespBO(AdjustRespDTO respDTO) {
        if (null == respDTO) {
            return;
        }
        this.accountingDate = respDTO.getAccountingDate();
        this.accountingStatus = respDTO.getAccountingStatus();
        this.walletId = respDTO.getWalletId();
        this.accountBalance = respDTO.getAccountBalance();
        this.currentSystemFlag = respDTO.getCurrentSystemFlag();
        this.ciLimit = respDTO.getCiLimit();
        this.netQuota = respDTO.getNetQuota();
        this.bizCode = respDTO.getBizCode();
        this.bizMsg = respDTO.getBizMsg();

        logActgStatus();
    }

    public ActgAdjustRespBO(AdjustNotifyRespDTO respDTO) {
        if (null == respDTO) {
            return;
        }
        this.accountingDate = respDTO.getAccountingDate();
        this.accountingStatus = respDTO.getAccountingStatus();
        this.walletId = respDTO.getWalletId();
        this.accountBalance = respDTO.getAccountBalance();
        this.currentSystemFlag = respDTO.getCurrentSystemFlag();
        this.ciLimit = respDTO.getCiLimit();
        this.netQuota = respDTO.getNetQuota();
        this.bizCode = respDTO.getBizCode();
        this.bizMsg = respDTO.getBizMsg();

        logActgStatus();
    }

    public ActgAdjustRespBO(String accountingStatus, String bizCode, String bizMsg) {
        this.accountingStatus = accountingStatus;
        this.bizCode = bizCode;
        this.bizMsg = bizMsg;
    }
}
