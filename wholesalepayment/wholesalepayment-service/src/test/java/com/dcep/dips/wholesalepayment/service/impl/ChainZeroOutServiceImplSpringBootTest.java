package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.api.ChainZeroOutService;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutReportDTO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ChainZeroOutServiceImplSpringBootTest {

    @Autowired
    private ChainZeroOutService chainZeroOutService;

    @Test
    public void testReport() {
        ZeroOutReportDTO zeroOutReportDTO = new ZeroOutReportDTO();
        zeroOutReportDTO.setMsgId("20251023102320390233908295230001");
        zeroOutReportDTO.setOrgnlMsgId("20251023001WHOLESALE990001320001");

        List<ZeroOutResult> zeroOutResultList = new ArrayList<>();
        ZeroOutResult zeroOutResult1 = new ZeroOutResult();
        zeroOutResult1.setPtyId("C0915424");
        zeroOutResult1.setChannelSys("BCSP");
        zeroOutResult1.setCurrentSystemFlag("A");
        zeroOutResult1.setFinishZeroOutAmt(new BigDecimal("100"));
        zeroOutResult1.setUnFinishZeroOutAmt(new BigDecimal("100"));
        ZeroOutResult zeroOutResult2 = new ZeroOutResult();
        zeroOutResult2.setPtyId("C6542137");
        zeroOutResult2.setChannelSys("MCBS");
        zeroOutResult2.setCurrentSystemFlag("A");
        zeroOutResult2.setFinishZeroOutAmt(new BigDecimal("10"));
        zeroOutResult2.setUnFinishZeroOutAmt(new BigDecimal("10"));

        zeroOutResultList.add(zeroOutResult1);
        zeroOutResultList.add(zeroOutResult2);

        zeroOutReportDTO.setZeroOutResultlList(zeroOutResultList);

        Response<String> response = chainZeroOutService.report(zeroOutReportDTO);

        System.out.println(response.isSuccess()+"--"+response.getResult()+"--"+response.getErrorCode()+"--"+response.getErrorMsg());

    }




}
