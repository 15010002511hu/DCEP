/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.manager;

import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dto.acctrans.AccountingReportDTO;

public interface WholesaleReportManager {
    void process(AccountingReportDTO accountingReportDTO, AccountingInstrDO orgAccountingInstrDO);

    void processForMbridge(AccountingReportDTO accountingReportDTO, AccountingInstrDO orgAccountingInstrDO);
}
