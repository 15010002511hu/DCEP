package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dto.acctrans.AccountingReportDTO;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface WholesaleReportService {

    /**
     * 结算排队结果通知
     * @param accountingReportDTO
     * @return
     * @throws DcepException
     */
    Response<String> report(AccountingReportDTO accountingReportDTO) throws DcepException;

}
