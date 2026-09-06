package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.OnchainPaymentTransMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.OnChainPaymentTransDO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainTransReqDTO;
import com.dcep.gateway.mcbdc.dto.mcbs708.*;
import com.dcep.dips.wholesalepayment.manager.AccountingManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.gateway.mcbdc.dto.soap.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class OnChainServiceImplTest {

    @InjectMocks
    private OnChainServiceImpl onChainService;

    @Mock
    private MbridgeManager mbridgeManager;

    @Mock
    private OnchainPaymentTransMapper onchainPaymentTransMapper;

    @Mock
    private AccountingManager accountingManager;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("测试链上同步记账成功")
    public void testChainAccountingSuccess() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);


            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();

            AccountingInstrDO accountingInstrDO = generateRandomAccountingInstrDO();
            TransferRespDTO transferRespDTO = new TransferRespDTO();
            transferRespDTO.setAccountingStatus("0");
            transferRespDTO.setAccountingDate("20251013");
            Response<TransferRespDTO> transferRespDTOResponse = new Response<>(transferRespDTO);


            when(mbridgeManager.onChainRecord(any(), eq(false), any())).thenReturn(new Response<>(accountingInstrDO));
            when(accountingManager.transfer(any(),any(),any())).thenReturn(transferRespDTOResponse);
            when(accountingInstrMapper.updateAccountingInstr(any())).thenReturn(1);


            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            assertTrue(response.isSuccess());
        }
    }

    @Test
    @DisplayName("测试链上同步记账业务检查失败")
    public void testChainAccountingBusinessCheckFailed() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(false);
            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();

            TransferRespDTO transferRespDTO = new TransferRespDTO();
            transferRespDTO.setAccountingStatus("0");
            transferRespDTO.setAccountingDate("20251013");

            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            assertFalse(response.isSuccess());
        }
    }

    @Test
    @DisplayName("测试链上同步记账交易登记幂等检查->幂等")
    public void testChainAccountingRecordFailed() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();
            onChainTransReqDTO.setTransAmt(new BigDecimal("1.00"));
            onChainTransReqDTO.setDbtrPtyId("1234567890");
            onChainTransReqDTO.setCdtrPtyId("1234567890");

            AccountingInstrDO accountingInstrDO = generateRandomAccountingInstrDO();
            OnChainPaymentTransDO onChainPaymentTransDO = generateRandomOnChainPaymentTransDO();
            onChainPaymentTransDO.setDbtrPtyId("1234567890");
            onChainPaymentTransDO.setCdtrPtyId("1234567890");
            TransferRespDTO transferRespDTO = new TransferRespDTO();
            transferRespDTO.setAccountingStatus("0");
            transferRespDTO.setAccountingDate("20251013");
            Response<TransferRespDTO> transferRespDTOResponse = new Response<>(transferRespDTO);

            when(mbridgeManager.onChainRecord(any(), eq(false), any())).thenThrow(new DuplicateKeyException(""));
            when(onchainPaymentTransMapper.selectByPrimaryKey(any())).thenReturn(onChainPaymentTransDO);
            when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(accountingInstrDO);
            when(accountingManager.transfer(any(),any(),any())).thenReturn(transferRespDTOResponse);
            when(accountingInstrMapper.updateAccountingInstr(any())).thenReturn(1);

            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            assertTrue(response.isSuccess());
        }
    }

    @Test
    @DisplayName("测试链上同步记账交易登记幂等检查->非幂等：金额不一致")
    public void testChainAccountingRecordFailed1() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();
            onChainTransReqDTO.setTransAmt(new BigDecimal("100.00"));
            onChainTransReqDTO.setDbtrPtyId("1234567890");
            onChainTransReqDTO.setCdtrPtyId("1234567890");

            OnChainPaymentTransDO onChainPaymentTransDO = generateRandomOnChainPaymentTransDO();
            onChainPaymentTransDO.setDbtrPtyId("1234567890");
            onChainPaymentTransDO.setCdtrPtyId("1234567890");
            TransferRespDTO transferRespDTO = new TransferRespDTO();
            transferRespDTO.setAccountingStatus("1");
            transferRespDTO.setAccountingDate("20251013");

            when(mbridgeManager.onChainRecord(any(), eq(false), any())).thenThrow(new DuplicateKeyException(""));
            when(onchainPaymentTransMapper.selectByPrimaryKey(any())).thenReturn(onChainPaymentTransDO);

            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            assertFalse(response.isSuccess());
        }
    }

    @Test
    @DisplayName("测试链上同步记账交易登记幂等检查->非幂等：收付款机构不一致")
    public void testChainAccountingRecordFailed2() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();
            onChainTransReqDTO.setTransAmt(new BigDecimal("1.00"));
            onChainTransReqDTO.setDbtrPtyId("1234567891");
            onChainTransReqDTO.setCdtrPtyId("1234567890");

            OnChainPaymentTransDO onChainPaymentTransDO = generateRandomOnChainPaymentTransDO();
            onChainPaymentTransDO.setDbtrPtyId("1234567890");
            onChainPaymentTransDO.setCdtrPtyId("1234567890");
            TransferRespDTO transferRespDTO = new TransferRespDTO();
            transferRespDTO.setAccountingStatus("1");
            transferRespDTO.setAccountingDate("20251013");

            when(mbridgeManager.onChainRecord(any(), eq(false), any())).thenThrow(new DuplicateKeyException(""));
            when(onchainPaymentTransMapper.selectByPrimaryKey(any())).thenReturn(onChainPaymentTransDO);

            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            assertFalse(response.isSuccess());
        }
    }

    @Test
    @DisplayName("测试链上同步记账结算钱包失败")
    public void testChainAccountingTransferFailed() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();
            onChainTransReqDTO.setTransAmt(new BigDecimal("1.00"));
            onChainTransReqDTO.setDbtrPtyId("1234567890");
            onChainTransReqDTO.setCdtrPtyId("1234567890");

            AccountingInstrDO accountingInstrDO = generateRandomAccountingInstrDO();
            OnChainPaymentTransDO onChainPaymentTransDO = generateRandomOnChainPaymentTransDO();
            onChainPaymentTransDO.setDbtrPtyId("1234567890");
            onChainPaymentTransDO.setCdtrPtyId("1234567890");
            TransferRespDTO transferRespDTO = new TransferRespDTO();
            transferRespDTO.setAccountingStatus("0");
            transferRespDTO.setAccountingDate("20251013");
            Response<TransferRespDTO> transferRespDTOResponse = new Response<>("调用结算钱包通讯异常");

            when(mbridgeManager.onChainRecord(any(), eq(false), any())).thenThrow(new DuplicateKeyException(""));
            when(onchainPaymentTransMapper.selectByPrimaryKey(any())).thenReturn(onChainPaymentTransDO);
            when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(accountingInstrDO);
            when(accountingManager.transfer(any(),any(),any())).thenReturn(transferRespDTOResponse);


            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            assertFalse(response.isSuccess());
        }
    }

    @Test
    @DisplayName("测试链上同步记账交易更新记账指令表记录不为1")
    public void testChainAccountingTransferUpdateFailed() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();
            onChainTransReqDTO.setTransAmt(new BigDecimal("1.00"));
            onChainTransReqDTO.setDbtrPtyId("1234567890");
            onChainTransReqDTO.setCdtrPtyId("1234567890");

            AccountingInstrDO accountingInstrDO = generateRandomAccountingInstrDO();
            OnChainPaymentTransDO onChainPaymentTransDO = generateRandomOnChainPaymentTransDO();
            onChainPaymentTransDO.setDbtrPtyId("1234567890");
            onChainPaymentTransDO.setCdtrPtyId("1234567890");
            TransferRespDTO transferRespDTO = new TransferRespDTO();
            transferRespDTO.setAccountingStatus("0");
            transferRespDTO.setAccountingDate("20251013");
            Response<TransferRespDTO> transferRespDTOResponse = new Response<>(transferRespDTO);

            when(mbridgeManager.onChainRecord(any(), eq(false), any())).thenThrow(new DuplicateKeyException(""));
            when(onchainPaymentTransMapper.selectByPrimaryKey(any())).thenReturn(onChainPaymentTransDO);
            when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(accountingInstrDO);
            when(accountingManager.transfer(any(),any(),any())).thenReturn(transferRespDTOResponse);
            when(accountingInstrMapper.updateAccountingInstr(any())).thenReturn(0);


            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            assertFalse(response.isSuccess());
        }
    }

    @Test
    @DisplayName("测试链上同步记账交易结算钱包返回状态不为成功")
    public void testChainAccountingTransferResultFailed() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            OnChainTransReqDTO onChainTransReqDTO = generateRandomOnChainTransReqDTO();
            onChainTransReqDTO.setTransAmt(new BigDecimal("1.00"));
            onChainTransReqDTO.setDbtrPtyId("1234567890");
            onChainTransReqDTO.setCdtrPtyId("1234567890");

            AccountingInstrDO accountingInstrDO = generateRandomAccountingInstrDO();
            OnChainPaymentTransDO onChainPaymentTransDO = generateRandomOnChainPaymentTransDO();
            onChainPaymentTransDO.setDbtrPtyId("1234567890");
            onChainPaymentTransDO.setCdtrPtyId("1234567890");
            TransferRespDTO transferRespDTO = new TransferRespDTO();
            transferRespDTO.setAccountingStatus("1");
            transferRespDTO.setAccountingDate("20251013");
            Response<TransferRespDTO> transferRespDTOResponse = new Response<>(transferRespDTO);

            when(mbridgeManager.onChainRecord(any(), eq(false), any())).thenThrow(new DuplicateKeyException(""));
            when(onchainPaymentTransMapper.selectByPrimaryKey(any())).thenReturn(onChainPaymentTransDO);
            when(accountingInstrMapper.selectByMsgIdPrepare(any())).thenReturn(accountingInstrDO);
            when(accountingManager.transfer(any(),any(),any())).thenReturn(transferRespDTOResponse);
            when(accountingInstrMapper.updateAccountingInstr(any())).thenReturn(1);


            Response<String> response = onChainService.chainAccounting(onChainTransReqDTO);
            assertFalse(response.isSuccess());
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
            mcbsGwReqDTO.setSoapHeader(generateMcbsSoapHeader());

            AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
            accountingInstrDO.setTransId(IdUtils.randomTransIdWithBizDt("20251020"));

            when(mbridgeManager.onChainRecord(any(), eq(true))).thenReturn(new Response<>(accountingInstrDO));
            when(accountingManager.transfer(any(),any(),any())).thenReturn(new Response<>(new TransferRespDTO()));

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = onChainService.mbridgeAccounting(mcbsGwReqDTO);
            assertTrue(response.isSuccess());
        }
         */
    }

    private static String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(22);
        for (int i = 0; i < 22; i++) {
            sb.append(random.nextInt(10)); // 生成0-9之间的随机数字
        }
        return "2025101311" + sb;
    }

    private static BigDecimal generateRandomBigDecimal(int max) {
        return new BigDecimal(new Random().nextInt(max));
    }

    private static Date generateRandomDate() {
        LocalDateTime localDateTime = LocalDateTime.of(2023, 1, 1, 0, 0)
                .plusDays(new Random().nextInt(365))
                .plusHours(new Random().nextInt(24))
                .plusMinutes(new Random().nextInt(60))
                .plusSeconds(new Random().nextInt(60));
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    private OnChainTransReqDTO generateRandomOnChainTransReqDTO() {
        OnChainTransReqDTO onChainTransReqDTO = new OnChainTransReqDTO();

        // 设置固定值
        onChainTransReqDTO.setMsgId(generateRandomString());
        onChainTransReqDTO.setBatId("123456789012345");
        onChainTransReqDTO.setBizDt(new Date()); // 当前日期时间
        onChainTransReqDTO.setTransAmt(new BigDecimal("100.00"));
        onChainTransReqDTO.setChannelSys("BCSP");
        onChainTransReqDTO.setBizTp("0001");
        onChainTransReqDTO.setTrans_sts("PR00");
        onChainTransReqDTO.setDbtrPtyId("12345678901234");
        onChainTransReqDTO.setDbtrWltId("1234567890123456");
        onChainTransReqDTO.setCdtrPtyId("12345678901234");
        onChainTransReqDTO.setCdtrWltId("1234567890123456");
        onChainTransReqDTO.setDbtrSysId("BCSP");
        onChainTransReqDTO.setCdtrSysId("MCBS");
        onChainTransReqDTO.setUseCurrentSystemFlag("A");

        // 打印对象
        System.out.println(onChainTransReqDTO);

        return onChainTransReqDTO;
    }

    private AccountingInstrDO generateRandomAccountingInstrDO(){
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        // 设置固定值
        accountingInstrDO.setTransId(generateRandomString());
        accountingInstrDO.setMsgId(generateRandomString());
        accountingInstrDO.setMsgTp("MSG001");
        accountingInstrDO.setOrgnlTransId("123456789012345678");
        accountingInstrDO.setSendPtyId("1111");
        accountingInstrDO.setEndToEndId("123456789012345678");
        accountingInstrDO.setActgBizTp("SWBC01");
        accountingInstrDO.setActgBizKind("0001");
        accountingInstrDO.setBizPrty("URGT");
        accountingInstrDO.setMgmtTp("12");
        accountingInstrDO.setBizDt("2023-10-01");
        accountingInstrDO.setActgDt("2023-10-01");
        accountingInstrDO.setFromClrSysId("DCEP");
        accountingInstrDO.setFromClrMmbId("1111");
        accountingInstrDO.setFromWlltId("123456789012345678");
        accountingInstrDO.setFromAcctNo("123456789012345678");
        accountingInstrDO.setToClrSysId("DCEP");
        accountingInstrDO.setToClrMmbId("1111");
        accountingInstrDO.setToWlltId("123456789012345678");
        accountingInstrDO.setToAcctNo("123456789012345678");
        accountingInstrDO.setActgModel("1");
        accountingInstrDO.setCurrency("CNY");
        accountingInstrDO.setAmount(new BigDecimal("100.00"));
        accountingInstrDO.setAbstractCd("123456789012345678");
        accountingInstrDO.setAbstractDesc("摘要描述");
        accountingInstrDO.setActgSts("1");
        accountingInstrDO.setActgPrcCd("123456789012345678");
        accountingInstrDO.setActgPrcInf("处理信息");
        accountingInstrDO.setGmtCreate(new Date());
        accountingInstrDO.setGmtModified(new Date());

        // 打印对象
        System.out.println(accountingInstrDO);

        return accountingInstrDO;
    }

    private OnChainPaymentTransDO generateRandomOnChainPaymentTransDO(){
        OnChainPaymentTransDO onChainPaymentTransDO = new OnChainPaymentTransDO();
        // 设置固定值
        onChainPaymentTransDO.setMsgId(generateRandomString());
        onChainPaymentTransDO.setMsgTp("MSG001");
        onChainPaymentTransDO.setBizDt("2023-10-01");
        onChainPaymentTransDO.setBatId("123456789012345");
        onChainPaymentTransDO.setChnlSys("BCSP");
        onChainPaymentTransDO.setTransAmt(new BigDecimal("100"));
        onChainPaymentTransDO.setBizTp("0001");
        onChainPaymentTransDO.setTransSts("PR00");
        onChainPaymentTransDO.setChainDbtrPtyId("DBTR001");
        onChainPaymentTransDO.setDbtrPtyId("DBTR002");
        onChainPaymentTransDO.setDbtrSysId("BCSP");
        onChainPaymentTransDO.setDbtrPtyLei("LEI123456789012345678");
        onChainPaymentTransDO.setDbtrWltId("DBTRWLT001");
        onChainPaymentTransDO.setChainCdtrPtyId("CDTR001");
        onChainPaymentTransDO.setCdtrPtyId("CDTR002");
        onChainPaymentTransDO.setCdtrSysId("BCSP");
        onChainPaymentTransDO.setCdtrPtyLei("LEI987654321098765432");
        onChainPaymentTransDO.setCdtrWltId("CDTRWLT001");
        onChainPaymentTransDO.setGmtCreate(new Date());
        onChainPaymentTransDO.setGmtModified(new Date());

        // 打印对象
        System.out.println(onChainPaymentTransDO);

        return onChainPaymentTransDO;
    }

    private McbsSoapHeader generateMcbsSoapHeader() {
        McbsSoapHeader mcbsSoapHeader = new McbsSoapHeader();
        mcbsSoapHeader.setVer(Constant.MCBS_SOAPHEADER_VER);
        mcbsSoapHeader.setSndDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        mcbsSoapHeader.setMsgTp("mcbs.708.001.01");
        mcbsSoapHeader.setSenderLEI(Constant.LEI_MCBS);
        mcbsSoapHeader.setSenderCBMALEI(Constant.LEI_MCBS);
        mcbsSoapHeader.setReceiverLEI(Constant.LEI_PBOC);
        mcbsSoapHeader.setReceiverCBMALEI(Constant.LEI_PBOC);
        mcbsSoapHeader.setMessageDirection("S");
        return mcbsSoapHeader;
    }
}
