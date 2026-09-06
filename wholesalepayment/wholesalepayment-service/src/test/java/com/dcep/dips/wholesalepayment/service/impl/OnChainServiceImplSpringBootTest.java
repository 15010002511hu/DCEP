package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainTransReqDTO;
import com.dcep.gateway.mcbdc.dto.mcbs708.*;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import com.dcep.gateway.mcbdc.dto.soap.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class OnChainServiceImplSpringBootTest {

    @Autowired
    private OnChainServiceImpl onChainService;

    @Before
    public void setUp() {

    }

    @Test
    @DisplayName("测试链上同步记账成功")
    public void testChainAccountingSuccess() throws Exception {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);

            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();

            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            System.out.println(response.isSuccess() + "--" + response.getResult() + "--" + response.getErrorCode() + "--" + response.getErrorMsg());
        }
    }

    @Test
    @DisplayName("测试桥上同步记账成功")
    public void testMbridgeAccountingSuccess() throws DcepException {
        /*
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);

            String mcbsMsgId = generateRandomString();
            // 生成 PayerAgt 实例
            PayerAgt payerAgt = PayerAgt.builder()
                    .finInstnId(FinInstnId.builder().nm("name").lEI("0000").build())
                    .wltId("123456789012")
                    .ctryOfRes("123")
                    .cenBnkID("7890")
                    .build();

            // 生成 PayeeAgt 实例
            PayeeAgt payeeAgt = PayeeAgt.builder()
                    .finInstnId(FinInstnId.builder().nm("name").lEI("0001").build())
                    .wltId("123456789013")
                    .ctryOfRes("124")
                    .cenBnkID("7891")
                    .build();

            // 生成 DetailInf 实例
            DetailInf detailInf = DetailInf.builder()
                    .amt("100.00")
                    .bizTp("PYMT")
                    .msgId(mcbsMsgId)
                    .stsCd("SUCD")
                    .deDtTm("2025-09-25T14:08:28")
                    .payerAgt(payerAgt)
                    .payeeAgt(payeeAgt)
                    .build();

            // 生成 GrpHdr 实例
            GrpHdr grpHdr = GrpHdr.builder()
                    .msgId(mcbsMsgId)
                    .dlTime("2025092511")
                    .creDtTm("2025-09-25T14:08:28")
                    .build();

            // 生成 TxInf 实例
            TxInf txInf = TxInf.builder()
                    .detailInf(detailInf)
                    .build();

            // 生成 Mcbs70800101DTO 实例
            Mcbs70800101DTO mcbs70800101DTO = Mcbs70800101DTO.builder()
                    .grpHdr(grpHdr)
                    .txInf(txInf)
                    .build();

            McbsEnvelopeDTO<McbsGwDTO> mcbsGwReqDTO = new McbsEnvelopeDTO<>();
            mcbsGwReqDTO.setSoapBody(new McbsSoapBody<>(mcbs70800101DTO));
            mcbsGwReqDTO.setSoapHeader(TestUtils.generateMcbsSoapHeader("mcbs.708.001.01"));

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = onChainService.mbridgeAccounting(mcbsGwReqDTO);
            System.out.println(response.isSuccess() + "--" + response.getResult() + "--" + response.getErrorCode() + "--" + response.getErrorMsg());
         */
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(22);
        for (int i = 0; i < 22; i++) {
            sb.append(random.nextInt(10)); // 生成0-9之间的随机数字
        }
        return getSysTime("yyyyMMddHH") + sb;
    }
    private String getSysTime(String pattern){
        // 获取当前系统时间
        LocalDateTime now = LocalDateTime.now();

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 格式化当前时间
        return now.format(formatter);
    }

    private OnChainTransReqDTO generateRandomOnChainTransReqDTO() throws ParseException {
        OnChainTransReqDTO onChainTransReqDTO = new OnChainTransReqDTO();

        // 设置固定值
        onChainTransReqDTO.setMsgId(TestUtils.getMsgId("000", "000"));
        onChainTransReqDTO.setBatId("1234567890123");
        onChainTransReqDTO.setBizDt(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(TestUtils.getCurCreDtTm())); // 当前日期时间
        onChainTransReqDTO.setTransAmt(new BigDecimal("100.00"));
        onChainTransReqDTO.setChannelSys("BCSP");
        onChainTransReqDTO.setBizTp("PYMT");
        onChainTransReqDTO.setTrans_sts("SUCD");
        onChainTransReqDTO.setDbtrPtyId("12345678901234");
        onChainTransReqDTO.setDbtrWltId("1234567890123456");
        onChainTransReqDTO.setCdtrPtyId("12345678901234");
        onChainTransReqDTO.setCdtrWltId("1234567890123456");
        onChainTransReqDTO.setDbtrSysId("BCSP");
        onChainTransReqDTO.setCdtrSysId("MCBS");

        // 打印对象
        System.out.println(onChainTransReqDTO);

        return onChainTransReqDTO;
    }
}
