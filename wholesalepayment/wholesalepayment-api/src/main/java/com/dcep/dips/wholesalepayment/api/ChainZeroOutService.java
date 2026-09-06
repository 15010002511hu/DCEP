package com.dcep.dips.wholesalepayment.api;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutReportDTO;
import com.dubbo.ldc.ZoneRouter;

@ZoneRouter
public interface ChainZeroOutService {

    /**
     * 接受区块链链上清零结果通知，并调用结算钱包服务进行结算记账
     * @param zeroOutReportDto
     * @return
     * @throws DcepException
     */
    Response<String> report(ZeroOutReportDTO zeroOutReportDto) throws DcepException;
}
