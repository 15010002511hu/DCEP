package com.dcep.dips.wholesalepayment.manager.impl;

import cn.hutool.core.date.DateUtil;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustRespDTO;
import com.dcep.dips.wholesalepayment.enums.ActgStsEnum;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.SettlementManager;
import com.dcep.dips.wholesalepayment.manager.impl.ChainManagerImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ChainManagerImplTest {
    @InjectMocks
    private ChainManagerImpl chainManager;

    @Mock
    private SettlementManager settlementManager;

    @Mock
    private SettlementProdMapper settlementProdMapper;
    @Mock
    private CommonStsctrlMapper commonStsctrlMapper;
    @Mock
    private SystemStatusDOMapper systemStatusDOMapper;
    @Mock
    private AccountingInstrMapper accountingInstrMapper;
    @Mock
    private CommonRecordMapper commonRecordMapper;
    private TransferRespDTO successResult;
    private TransferRespDTO failedResult;
    private AccountingInstrDO accountingInstrDO;
    private OnChainAdjustReqDTO onChainAdjustReqDTO;
    @Before
    public void setUp() throws NoSuchFieldException {
        MockitoAnnotations.openMocks(this);

        successResult = new TransferRespDTO();
        successResult.setAccountingStatus(ActgStsEnum.SUCCESS.getCode());
        successResult.setAccountingDate(DateUtil.format(new Date(),"yyyyMMdd"));

        failedResult = new TransferRespDTO();
        failedResult.setAccountingStatus(ActgStsEnum.FAILED.getCode());
        failedResult.setAccountingDate(DateUtil.format(new Date(),"yyyyMMdd"));

        accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setMsgId("20251015203120347452823150745000");

        onChainAdjustReqDTO = new OnChainAdjustReqDTO();
        onChainAdjustReqDTO.setMsgId("20251015203120347452823150745000");
        onChainAdjustReqDTO.setAmount(new BigDecimal("1"));
        onChainAdjustReqDTO.setAdjustTp("QOT01");
    }


    @Test
    public void testRecord_OnChainAdjustReqDTO() {
        when(systemStatusDOMapper.selectCurSysDt(any())).thenReturn("20231001");
        AccountingInstrDO response = chainManager.record(onChainAdjustReqDTO);

        assertNotNull(response);

        verify(settlementProdMapper, times(1)).insert(any());
        verify(accountingInstrMapper, times(1)).insert(any());
        verify(commonRecordMapper, times(1)).insert(any());
        verify(commonStsctrlMapper, times(1)).insert(any());
    }

//    @Test
//    public void testSettleComplete_Success() {
//
//
//        Response<OnChainAdjustRespDTO> response = chainManager.settleComplete(successResult, accountingInstrDO);
//
//        assertTrue(response.isSuccess());
//        assertEquals(ClearingStatusEnum.SETTLED.getCode(), response.getResult().getBizStatus());
//        assertEquals(successResult.getAccountingDate(), response.getResult().getSettlementDate());
//        assertEquals(accountingInstrDO.getActgPrcCd(), response.getResult().getBizProcessCode());
//        assertEquals(accountingInstrDO.getActgPrcInf(), response.getResult().getBizProcessInfo());
//
//        verify(settlementManager, times(1)).settled(accountingInstrDO, false);
//    }
//
//    @Test
//    public void testSettleComplete_Failed() {
//        SettlementProdDO settlementProdDO = new SettlementProdDO();
//        settlementProdDO.setMsgId("20251015203120347452823150745000");
//        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
//
//        Response<OnChainAdjustRespDTO> response = chainManager.settleComplete(failedResult, accountingInstrDO);
//
//        assertTrue(response.isSuccess());
//        assertEquals(ClearingStatusEnum.FAILED.getCode(), response.getResult().getBizStatus());
//        assertEquals(failedResult.getAccountingDate(), response.getResult().getSettlementDate());
//        assertEquals(accountingInstrDO.getActgPrcCd(), response.getResult().getBizProcessCode());
//        assertEquals(accountingInstrDO.getActgPrcInf(), response.getResult().getBizProcessInfo());
//
//        verify(settlementManager, times(1)).fail(any(), any(), eq(false));
//    }
//
//    @Test
//    public void testSettleComplete_OtherStatus() {
//        TransferRespDTO otherResult = new TransferRespDTO();
//        otherResult.setAccountingStatus("OTHER");
//        otherResult.setAccountingDate(DateUtil.format(new Date(),"yyyyMMdd"));
//
//        Response<OnChainAdjustRespDTO> response = chainManager.settleComplete(otherResult, accountingInstrDO);
//
//        assertFalse(response.isSuccess());
//        assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(), response.getErrorCode());
//        assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription(), response.getErrorMsg());
//    }
//
//    @Test
//    public void testSettleFail() {
//        String errorCode = "ERROR_CODE";
//        String errorMsg = "ERROR_MSG";
//
//        SettlementProdDO settlementProdDO = new SettlementProdDO();
//        settlementProdDO.setMsgId("20251015203120347452823150745000");
//
//        when(settlementProdMapper.selectByPrimaryKey(any())).thenReturn(settlementProdDO);
//
//        Response<OnChainAdjustRespDTO> response = chainManager.settleFail(accountingInstrDO, errorCode, errorMsg);
//
//        assertTrue(response.isSuccess());
//        assertEquals(ClearingStatusEnum.FAILED.getCode(), response.getResult().getBizStatus());
//        assertNull(response.getResult().getSettlementDate());
//        assertEquals(errorCode, response.getResult().getBizProcessCode());
//        assertEquals(errorMsg, response.getResult().getBizProcessInfo());
//
//        verify(settlementManager, times(1)).fail(any(), any(),  eq(false));
//    }
}