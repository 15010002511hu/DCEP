/*
 * pbcdci.cn Inc.
 * Copyright © 2025 PBCDCI All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.dal.mapper.SettlementProdMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.ClearingStatus;
import com.dcep.dips.wholesalepayment.dto.dc201.Dcep20101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc211.Dcep21101001DTO;
import com.dcep.dips.wholesalepayment.dto.dc262.Dcep26201001DTO;
import com.dcep.dips.wholesalepayment.dto.dc263.Dcep26301001DTO;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.manager.CommonManager;
import com.dcep.dips.wholesalepayment.manager.PaymentManager;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import org.apache.dubbo.rpc.RpcContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;
    @Mock
    PaymentManager paymentManager;
    @Mock
    SettlementProdMapper settlementProdMapper;
    @Mock
    CommonManager commonManager;
    @Mock
    SystemStatusDOMapper systemStatusMapper;

    @Mock
    private ExecutorService asyncPool;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(paymentService, "CREDTTM_INTERVAL", Long.toString(10 * 60 * 1000));
        ReflectionTestUtils.setField(paymentService, "MSGID_DATE_INTERVAL", "1");
    }
    @Test
    @DisplayName("测试dbtrSettle成功")
    public void testDbtrSettleSuccess() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doNothing().when(paymentManager).record(any(),any());
            doAnswer(invocation -> {
                Runnable task = invocation.getArgument(0);
                task.run();
                return null;
            }).when(asyncPool).execute(any(Runnable.class));
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);

            verify(asyncPool).execute(any(Runnable.class));
//            verify(paymentManager).clearing(any());

            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试dbtrSettle检查系统日期未通过")
    public void testDbtrSettleCheckSystemDateFail() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(false);
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("DCEPO1036", dcep900.getCmonConfInf().getPrcCd());
        }
    }

    @Test
    @DisplayName("测试dbtrSettle业务检查未通过")
    public void testDbtrSettleCheckBusinessInfoFail() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("003");
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("DCEPO1051", dcep900.getCmonConfInf().getPrcCd());
        }
    }

    @Test
    @DisplayName("测试dbtrSettle幂等成功")
    public void testDbtrSettleDuplicateSuccess() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doThrow(DuplicateKeyException.class).when(paymentManager).record(any(),any());

            SettlementProdDO settlementProdDO = SettlementProdDO.builder().bizSts("PR02").msgTp("dcep.201.010.01").dbtrPtyId("C1010311000014")
                    .cbtrPtyId("C1010211000012").sttlmAmt(new BigDecimal("1")).build();

            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试dbtrSettle线程池满")
    public void testDbtrSettleRejected() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doNothing().when(paymentManager).record(any(),any());
            doThrow(RejectedExecutionException.class).when(asyncPool).execute(any(Runnable.class));

            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试dbtrSettle业务检查未通过后fail接口幂等")
    public void testDbtrSettleCheckBusinessInfoFailDuplicate() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("003");
            doThrow(DuplicateKeyException.class).when(paymentManager).fail(any(),any());
            // 发起调用
            try{
                Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);
            }
            catch (Exception e){
                assertEquals(ErrorEnum.BUSI_DUPLICATION.getDescription() ,e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("测试dbtrSettle幂等失败1")
    public void testDbtrSettleDuplicateFail1() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doThrow(DuplicateKeyException.class).when(paymentManager).record(any(),any());

            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_TWO);
            // 发起调用
            try{
                Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);
            }
            catch (Exception e){
                assertEquals(ErrorEnum.BUSI_DB_KEYWORD_REPEAT.getDescription() ,e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("测试dbtrSettle幂等失败2")
    public void testDbtrSettleDuplicateFail2() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doThrow(DuplicateKeyException.class).when(paymentManager).record(any(),any());

            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);

            SettlementProdDO settlementProdDO = SettlementProdDO.builder().bizSts("PR10").msgTp("dcep.201.010.01").dbtrPtyId("C1010311000014")
                    .cbtrPtyId("C1010211000012").sttlmAmt(new BigDecimal("1")).build();

            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
            // 发起调用
            try{
                Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);
            }
            catch (Exception e){
                assertEquals(ErrorEnum.BUSI_DUPLICATION.getDescription() ,e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("测试dbtrSettle幂等失败3")
    public void testDbtrSettleDuplicateFail3() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_201();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doThrow(DuplicateKeyException.class).when(paymentManager).record(any(),any());

            RpcContext rpcContext = RpcContext.getContext();
            rpcContext.set(Constant.PREPARE_INSERT_STEP, Constant.INSERT_STEP_ONE);

            SettlementProdDO settlementProdDO = SettlementProdDO.builder().bizSts("PR02").msgTp("dcep.201.010.01").dbtrPtyId("C1010311000014")
                    .cbtrPtyId("C1010211000012").sttlmAmt(new BigDecimal("10")).build();

            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
            // 发起调用
            try{
                Response<EnvelopeDTO<GwDTO>> response = paymentService.dbtrSettle(gwReqDTO);
            }
            catch (Exception e){
                assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription() ,e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("测试cdtrSettle成功")
    public void testCdtrSettleCheckSystemDateFail() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_211();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doNothing().when(paymentManager).record(any(),any());
            doAnswer(invocation -> {
                Runnable task = invocation.getArgument(0);
                task.run();
                return null;
            }).when(asyncPool).execute(any(Runnable.class));
            when(paymentManager.debitConfirm(any(), any())).thenReturn(true);
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.cdtrSettle(gwReqDTO);

            verify(asyncPool).execute(any(Runnable.class));
            verify(paymentManager).debitConfirm(any(), any());
//            verify(paymentManager).clearing(any());

            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试cdtrSettle成功")
    public void testCdtrSettleSuccess() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_211();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(false);
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.cdtrSettle(gwReqDTO);

            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("DCEPO1036", dcep900.getCmonConfInf().getPrcCd());
        }
    }

    @Test
    @DisplayName("测试cdtrSettle幂等成功")
    public void testCdtrSettleDuplicateSuccess() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_211();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doThrow(DuplicateKeyException.class).when(paymentManager).record(any(),any());

            SettlementProdDO settlementProdDO = SettlementProdDO.builder().bizSts("PR02").msgTp("dcep.211.010.01").dbtrPtyId("C1010211000012")
                    .cbtrPtyId("C1010311000014").sttlmAmt(new BigDecimal("1")).build();

            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.cdtrSettle(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试cdtrSettle线程池满")
    public void testCdtrSettleRejected() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_211();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doNothing().when(paymentManager).record(any(),any());
            doThrow(RejectedExecutionException.class).when(asyncPool).execute(any(Runnable.class));

            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.cdtrSettle(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试orderConfirm成功")
    public void testOrderConfirm() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_262("PR02");

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            // 发起调用
            Response<GwDTO> response = paymentService.orderConfirm(gwReqDTO);
            ClearingStatus clearingStatus = (ClearingStatus) response.getResult();
            assertEquals("PR02", clearingStatus.getPrcSts());
        }
    }

    @Test
    @DisplayName("测试orderConfirm返回失败")
    public void testOrderConfirmFail() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_262("PR01");
        Response<GwDTO> response = paymentService.orderConfirm(gwReqDTO);
        ClearingStatus clearingStatus = (ClearingStatus) response.getResult();
        assertEquals("PR01", clearingStatus.getPrcSts());
    }

    @Test
    @DisplayName("测试orderConfirm业务验证失败")
    public void testOrderConfirmFail1() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_262("PR02");

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(false);
            // 发起调用
            Response<GwDTO> response = paymentService.orderConfirm(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult();
            assertEquals("PR01", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试orderConfirm幂等成功")
    public void testOrderConfirmDuplicateSuccess() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_262("PR02");

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("002");
            when(commonManager.checkSystemDate(any())).thenReturn(true);
            when(systemStatusMapper.selectCurSysDt(any())).thenReturn(DcepDateUtils.getNowStrByPattern(DcepDateUtils.DATE_PATTERN));
            doThrow(DuplicateKeyException.class).when(paymentManager).record(any(),any());

            SettlementProdDO settlementProdDO = SettlementProdDO.builder().bizSts("PR02").msgTp("dcep.262.010.01").dbtrPtyId("C1010211000012")
                    .cbtrPtyId("C1010311000014").sttlmAmt(new BigDecimal("1")).build();

            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
            // 发起调用
            Response<GwDTO> response = paymentService.orderConfirm(gwReqDTO);
            ClearingStatus clearingStatus = (ClearingStatus) response.getResult();
            assertEquals("PR02", clearingStatus.getPrcSts());
        }
    }

    @Test
    @DisplayName("测试resultReport成功")
    public void testResultReport() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_263();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            SettlementProdDO settlementProdDO = SettlementProdDO.builder().bizSts("PR02").msgTp("dcep.262.010.01").dbtrPtyId("C1010211000012")
                    .cbtrPtyId("C1010311000014").sttlmAmt(new BigDecimal("100")).build();
            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
            when(paymentManager.resultReportProcess(any())).thenReturn(new Response<>());
            doAnswer(invocation -> {
                Runnable task = invocation.getArgument(0);
                task.run();
                return null;
            }).when(asyncPool).execute(any(Runnable.class));
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.resultReport(gwReqDTO);
            verify(asyncPool).execute(any(Runnable.class));
//            verify(paymentManager).clearing(any());
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试resultReport无原交易")
    public void testResultReportNoOriginal() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_263();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(null);
            // 发起调用
            try{
                Response<EnvelopeDTO<GwDTO>> response = paymentService.resultReport(gwReqDTO);
            }
            catch (Exception e){
                assertEquals(ErrorEnum.RESP_NO_MATCH_ORIGINAL_BUSI.getDescription() ,e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("测试resultReport原交易已为终态")
    public void testResultReportNoProcess() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_263();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            SettlementProdDO settlementProdDO = SettlementProdDO.builder().bizSts("PR10").msgTp("dcep.262.010.01").dbtrPtyId("C1010211000012")
                    .cbtrPtyId("C1010311000014").sttlmAmt(new BigDecimal("100")).build();
            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
            // 发起调用
            Response<EnvelopeDTO<GwDTO>> response = paymentService.resultReport(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    @Test
    @DisplayName("测试resultReport线程池满")
    public void testResultReportRejectedExecutionException() throws DcepException {
        EnvelopeDTO<GwDTO> gwReqDTO = loadEnvelopeDTO_263();

        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            SettlementProdDO settlementProdDO = SettlementProdDO.builder().bizSts("PR02").msgTp("dcep.262.010.01").dbtrPtyId("C1010211000012")
                    .cbtrPtyId("C1010311000014").sttlmAmt(new BigDecimal("100")).build();
            when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
            when(paymentManager.resultReportProcess(any())).thenReturn(new Response<>());
            doThrow(RejectedExecutionException.class).when(asyncPool).execute(any(Runnable.class));
            Response<EnvelopeDTO<GwDTO>> response = paymentService.resultReport(gwReqDTO);
            Dcep90000101DTO dcep900 = (Dcep90000101DTO) response.getResult().body();
            assertEquals("PR06", dcep900.getCmonConfInf().getPrcSts());
        }
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_201() {
        String msgTp = "dcep.201.010.01";
        String shortMsgTp = "201";
        String shortSender = "003";
        String shortReceiver = "002";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "payment/201.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{SysWorkDt}}", DcepDateUtils.getNowStrByPattern(DcepDateUtils.ISO_DATE_PATTERN));

        Dcep20101001DTO dcep201DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep20101001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep201DTO));
        return envelopeDTO;
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_211() {
        String msgTp = "dcep.211.010.01";
        String shortMsgTp = "211";
        String shortSender = "003";
        String shortReceiver = "002";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "payment/211.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{SysWorkDt}}", DcepDateUtils.getNowStrByPattern(DcepDateUtils.ISO_DATE_PATTERN));

        Dcep21101001DTO dcep211DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep21101001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep211DTO));
        return envelopeDTO;
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_262(String rspsnSts) {
        String msgTp = "dcep.262.010.01";
        String shortMsgTp = "262";
        String shortSender = "003";
        String shortReceiver = "002";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "payment/262.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{SysWorkDt}}", DcepDateUtils.getNowStrByPattern(DcepDateUtils.ISO_DATE_PATTERN));
        dataMap.put("{{RspsnSts}}", rspsnSts);

        Dcep26201001DTO dcep262DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep26201001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep262DTO));

        return envelopeDTO;
    }

    public EnvelopeDTO<GwDTO> loadEnvelopeDTO_263() {
        String msgTp = "dcep.263.010.01";
        String shortMsgTp = "263";
        String shortSender = "003";
        String shortReceiver = "002";
        String msgId = TestUtils.getMsgId(shortSender, shortMsgTp);
        String creDtTm = TestUtils.getCurCreDtTm();
        String xmlFile = "payment/263.xml";

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();
        SoapHeader soapHeader = TestUtils.createSoapHeader(msgTp, msgId, creDtTm, shortSender, shortReceiver);
        envelopeDTO.setSoapHeader(soapHeader);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("{{MsgId}}", msgId);
        dataMap.put("{{CurDtTm}}", creDtTm);
        dataMap.put("{{SysWorkDt}}", DcepDateUtils.getNowStrByPattern(DcepDateUtils.ISO_DATE_PATTERN));

        Dcep26301001DTO dcep263DTO = TestUtils.createDcepDTO(xmlFile, dataMap, Dcep26301001DTO.class);
        envelopeDTO.setSoapBody(new SoapBody<>(dcep263DTO));

        return envelopeDTO;
    }
}
