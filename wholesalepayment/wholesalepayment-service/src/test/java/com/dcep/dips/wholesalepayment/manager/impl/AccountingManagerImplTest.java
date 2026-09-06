package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.api.AccountingService;
import com.dcep.dips.acctrans.constants.Constant;
import com.dcep.dips.acctrans.dto.accounting.adjust.AdjustRespDTO;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.enums.ActgMgmtTpEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountingManagerImplTest {

    @InjectMocks
    private AccountingManagerImpl accountingManagerImpl;

    @Mock
    private AccountingService accountingService;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;

    @Mock
    private SystemStatusDOMapper systemStatusMapper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("测试正常调整逻辑")
    public void testAdjustNormal() throws DcepException {
        // 准备测试数据
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("12345678901234567890123456789012");
        accountingInstrDO.setMsgId("12345678901234567890123456789012");
        accountingInstrDO.setEndToEndId("12345678901234567890123456789012");
        accountingInstrDO.setMgmtTp(ActgMgmtTpEnum.CAP_INJECT_INCR.getCode());
        accountingInstrDO.setFromWlltId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setToWlltId("12345678901234567890123456789012");
        accountingInstrDO.setToClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setToClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setCurrency("CNY");
        accountingInstrDO.setAmount(new BigDecimal("100.00"));
        accountingInstrDO.setAbstractCd("12345678901234567890123456789012");
        accountingInstrDO.setAbstractDesc("测试调整");

        // 模拟返回值
        AdjustRespDTO adjustRespDTO = new AdjustRespDTO();
        adjustRespDTO.setWalletId("12345678901234567890123456789012");
        adjustRespDTO.setAccountBalance(new BigDecimal("0.01"));
        adjustRespDTO.setAccountingDate("20250101");
        adjustRespDTO.setCurrentSystemFlag("A");
        adjustRespDTO.setAccountingStatus(Constant.AccountingStatus.SUCCESS);
        adjustRespDTO.setCiLimit(new BigDecimal("0.02"));
        adjustRespDTO.setNetQuota(new BigDecimal("0.03"));
        adjustRespDTO.setBizCode("DCEPI0000");
        adjustRespDTO.setBizMsg("记账成功");
        Response<AdjustRespDTO> response = new Response<>();
        response.setResult(adjustRespDTO);

        // 模拟调用
        when(accountingService.adjust(any())).thenReturn(response);

        // 执行测试
        Response<AdjustRespDTO> result = accountingManagerImpl.adjust(accountingInstrDO, null);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(adjustRespDTO, result.getResult());
    }

    @Test
    @DisplayName("测试注资调减")
    public void testAdjustCapInjectDecr() throws DcepException {
        // 准备测试数据
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("12345678901234567890123456789012");
        accountingInstrDO.setMsgId("12345678901234567890123456789012");
        accountingInstrDO.setEndToEndId("12345678901234567890123456789012");
        accountingInstrDO.setMgmtTp(ActgMgmtTpEnum.CAP_INJECT_DECR.getCode());
        accountingInstrDO.setFromWlltId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setToWlltId("12345678901234567890123456789012");
        accountingInstrDO.setToClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setToClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setCurrency("CNY");
        accountingInstrDO.setAmount(new BigDecimal("100.00"));
        accountingInstrDO.setAbstractCd("12345678901234567890123456789012");
        accountingInstrDO.setAbstractDesc("测试调整");

        // 模拟返回值
        AdjustRespDTO adjustRespDTO = new AdjustRespDTO();
        adjustRespDTO.setWalletId("12345678901234567890123456789012");
        adjustRespDTO.setAccountBalance(new BigDecimal("0.01"));
        adjustRespDTO.setAccountingDate("20250101");
        adjustRespDTO.setCurrentSystemFlag("A");
        adjustRespDTO.setAccountingStatus(Constant.AccountingStatus.SUCCESS);
        adjustRespDTO.setCiLimit(new BigDecimal("0.02"));
        adjustRespDTO.setNetQuota(new BigDecimal("0.03"));
        adjustRespDTO.setBizCode("DCEPI0000");
        adjustRespDTO.setBizMsg("记账成功");
        Response<AdjustRespDTO> response = new Response<>();
        response.setResult(adjustRespDTO);

        // 模拟调用
        when(accountingService.adjust(any())).thenReturn(response);

        // 执行测试
        Response<AdjustRespDTO> result = accountingManagerImpl.adjust(accountingInstrDO, null);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(adjustRespDTO, result.getResult());
    }

    @Test
    @DisplayName("测试预注资调减")
    public void testAdjustPreInjectDecr() throws DcepException {
        // 准备测试数据
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("12345678901234567890123456789012");
        accountingInstrDO.setMsgId("12345678901234567890123456789012");
        accountingInstrDO.setEndToEndId("12345678901234567890123456789012");
        accountingInstrDO.setMgmtTp(ActgMgmtTpEnum.PRE_INJECT_DECR.getCode());
        accountingInstrDO.setFromWlltId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setToWlltId("12345678901234567890123456789012");
        accountingInstrDO.setToClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setToClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setCurrency("CNY");
        accountingInstrDO.setAmount(new BigDecimal("100.00"));
        accountingInstrDO.setAbstractCd("12345678901234567890123456789012");
        accountingInstrDO.setAbstractDesc("测试调整");

        // 模拟返回值
        AdjustRespDTO adjustRespDTO = new AdjustRespDTO();
        adjustRespDTO.setWalletId("12345678901234567890123456789012");
        adjustRespDTO.setAccountBalance(new BigDecimal("0.01"));
        adjustRespDTO.setAccountingDate("20250101");
        adjustRespDTO.setCurrentSystemFlag("A");
        adjustRespDTO.setAccountingStatus(Constant.AccountingStatus.SUCCESS);
        adjustRespDTO.setCiLimit(new BigDecimal("0.02"));
        adjustRespDTO.setNetQuota(new BigDecimal("0.03"));
        adjustRespDTO.setBizCode("DCEPI0000");
        adjustRespDTO.setBizMsg("记账成功");
        Response<AdjustRespDTO> response = new Response<>();
        response.setResult(adjustRespDTO);

        // 模拟调用
        when(accountingService.adjust(any())).thenReturn(response);

        // 执行测试
        Response<AdjustRespDTO> result = accountingManagerImpl.adjust(accountingInstrDO, null);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(adjustRespDTO, result.getResult());
    }

    @Test
    @DisplayName("测试注资调增")
    public void testAdjustCapInjectIncr() throws DcepException {
        // 准备测试数据
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("12345678901234567890123456789012");
        accountingInstrDO.setMsgId("12345678901234567890123456789012");
        accountingInstrDO.setEndToEndId("12345678901234567890123456789012");
        accountingInstrDO.setMgmtTp(ActgMgmtTpEnum.CAP_INJECT_INCR.getCode());
        accountingInstrDO.setFromWlltId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setToWlltId("12345678901234567890123456789012");
        accountingInstrDO.setToClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setToClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setCurrency("CNY");
        accountingInstrDO.setAmount(new BigDecimal("100.00"));
        accountingInstrDO.setAbstractCd("12345678901234567890123456789012");
        accountingInstrDO.setAbstractDesc("测试调整");

        // 模拟返回值
        AdjustRespDTO adjustRespDTO = new AdjustRespDTO();
        adjustRespDTO.setWalletId("12345678901234567890123456789012");
        adjustRespDTO.setAccountBalance(new BigDecimal("0.01"));
        adjustRespDTO.setAccountingDate("20250101");
        adjustRespDTO.setCurrentSystemFlag("A");
        adjustRespDTO.setAccountingStatus(Constant.AccountingStatus.SUCCESS);
        adjustRespDTO.setCiLimit(new BigDecimal("0.02"));
        adjustRespDTO.setNetQuota(new BigDecimal("0.03"));
        adjustRespDTO.setBizCode("DCEPI0000");
        adjustRespDTO.setBizMsg("记账成功");
        Response<AdjustRespDTO> response = new Response<>();
        response.setResult(adjustRespDTO);

        // 模拟调用
        when(accountingService.adjust(any())).thenReturn(response);

        // 执行测试
        Response<AdjustRespDTO> result = accountingManagerImpl.adjust(accountingInstrDO, null);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(adjustRespDTO, result.getResult());
    }

    @Test
    @DisplayName("测试预注资调增")
    public void testAdjustPreInjectIncr() throws DcepException {
        // 准备测试数据
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("12345678901234567890123456789012");
        accountingInstrDO.setMsgId("12345678901234567890123456789012");
        accountingInstrDO.setEndToEndId("12345678901234567890123456789012");
        accountingInstrDO.setMgmtTp(ActgMgmtTpEnum.PRE_INJECT_INCR.getCode());
        accountingInstrDO.setFromWlltId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setToWlltId("12345678901234567890123456789012");
        accountingInstrDO.setToClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setToClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setCurrency("CNY");
        accountingInstrDO.setAmount(new BigDecimal("100.00"));
        accountingInstrDO.setAbstractCd("12345678901234567890123456789012");
        accountingInstrDO.setAbstractDesc("测试调整");

        // 模拟返回值
        AdjustRespDTO adjustRespDTO = new AdjustRespDTO();
        adjustRespDTO.setWalletId("12345678901234567890123456789012");
        adjustRespDTO.setAccountBalance(new BigDecimal("0.01"));
        adjustRespDTO.setAccountingDate("20250101");
        adjustRespDTO.setCurrentSystemFlag("A");
        adjustRespDTO.setAccountingStatus(Constant.AccountingStatus.SUCCESS);
        adjustRespDTO.setCiLimit(new BigDecimal("0.02"));
        adjustRespDTO.setNetQuota(new BigDecimal("0.03"));
        adjustRespDTO.setBizCode("DCEPI0000");
        adjustRespDTO.setBizMsg("记账成功");
        Response<AdjustRespDTO> response = new Response<>();
        response.setResult(adjustRespDTO);

        // 模拟调用
        when(accountingService.adjust(any())).thenReturn(response);

        // 执行测试
        Response<AdjustRespDTO> result = accountingManagerImpl.adjust(accountingInstrDO, null);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isSuccess());
        assertEquals(adjustRespDTO, result.getResult());
    }

    @Test
    @DisplayName("测试异常场景 - DcepException")
    public void testAdjustDcepException() {
        // 准备测试数据
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("12345678901234567890123456789012");
        accountingInstrDO.setMsgId("12345678901234567890123456789012");
        accountingInstrDO.setEndToEndId("12345678901234567890123456789012");
        accountingInstrDO.setMgmtTp(ActgMgmtTpEnum.CAP_INJECT_INCR.getCode());
        accountingInstrDO.setFromWlltId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setToWlltId("12345678901234567890123456789012");
        accountingInstrDO.setToClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setToClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setCurrency("CNY");
        accountingInstrDO.setAmount(new BigDecimal("100.00"));
        accountingInstrDO.setAbstractCd("12345678901234567890123456789012");
        accountingInstrDO.setAbstractDesc("测试调整");

        // 模拟调用
        when(accountingService.adjust(any())).thenThrow(new DcepException(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(), "测试异常"));

        // 执行测试
        Response<AdjustRespDTO> result = accountingManagerImpl.adjust(accountingInstrDO, null);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
    }

    @Test
    @DisplayName("测试异常场景 - Exception")
    public void testAdjustException() {
        // 准备测试数据
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("12345678901234567890123456789012");
        accountingInstrDO.setMsgId("12345678901234567890123456789012");
        accountingInstrDO.setEndToEndId("12345678901234567890123456789012");
        accountingInstrDO.setMgmtTp(ActgMgmtTpEnum.CAP_INJECT_INCR.getCode());
        accountingInstrDO.setFromWlltId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setFromClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setToWlltId("12345678901234567890123456789012");
        accountingInstrDO.setToClrSysId("12345678901234567890123456789012");
        accountingInstrDO.setToClrMmbId("12345678901234567890123456789012");
        accountingInstrDO.setCurrency("CNY");
        accountingInstrDO.setAmount(new BigDecimal("100.00"));
        accountingInstrDO.setAbstractCd("12345678901234567890123456789012");
        accountingInstrDO.setAbstractDesc("测试调整");

        // 模拟调用
        when(accountingService.adjust(any())).thenThrow(new RuntimeException("测试异常"));

        // 执行测试
        Response<AdjustRespDTO> result = accountingManagerImpl.adjust(accountingInstrDO, null);

        // 验证结果
        assertNotNull(result);
        assertFalse(result.isSuccess());
    }
}
