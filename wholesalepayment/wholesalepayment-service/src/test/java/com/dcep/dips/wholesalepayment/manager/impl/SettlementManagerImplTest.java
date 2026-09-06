package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.enums.AccountingStatusEnum;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.mapper.*;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.SettlementProdDO;
import com.dcep.dips.wholesalepayment.dal.model.StorageForwardDO;
import com.dcep.dips.wholesalepayment.dto.chain.OnChainAdjustReqDTO;
import com.dcep.dips.wholesalepayment.enums.ClearingStatusEnum;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.manager.StorageForwardManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.math.BigDecimal;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SettlementManagerImplTest {
    @InjectMocks
    private SettlementManagerImpl settlementManager;

    @Mock
    private SettlementProdMapper settlementProdMapper;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;


    @Mock
    private CommonStsctrlMapper commonStsctrlMapper;

    @Mock
    private StorageForwardManager storageForwardManager;

    private OnChainAdjustReqDTO onChainAdjustReqDTO;
    private SettlementProdDO settlementProdDO;
    private AccountingInstrDO accountingInstrDO;
    private MockedStatic<IdUtils> idUtils;
    private MockedStatic<DtoUtil> dtoUtil;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        onChainAdjustReqDTO = new OnChainAdjustReqDTO();
        onChainAdjustReqDTO.setMsgId("20251015203120347452823150745000");
        onChainAdjustReqDTO.setAmount(new BigDecimal("1"));
        onChainAdjustReqDTO.setAdjustTp("QOT01");

        settlementProdDO = new SettlementProdDO();
        settlementProdDO.setBizSts(ClearingStatusEnum.WAIT_SETTLE.getCode());
        settlementProdDO.setMsgId("20251015203120347452823150745000");

        accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setMsgId("20251015203120347452823150745000");
        accountingInstrDO.setTransId("20251014000100000493996562772000");
        idUtils = Mockito.mockStatic(IdUtils.class);
        idUtils.when(() -> IdUtils.randomTransIdWithBizDt(any())).thenReturn("20251014000100000493996562772000");

        dtoUtil = Mockito.mockStatic(DtoUtil.class);
        dtoUtil.when(() -> DtoUtil.assembly200Msg(any(),any())).thenReturn(new EnvelopeDTO());
    }
    @After
    public void testDown() throws NoSuchFieldException {
        // 清理静态模拟
        idUtils.close();
        dtoUtil.close();
    }

//
//
//    @Test
//    public void testFail_WaitSettle() {
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
//        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(1);
//
//        Response<StorageForwardDO> response = settlementManager.fail(settlementProdDO, accountingInstrDO,  true);
//
//        assertNull(response);
//        verify(accountingInstrMapper, times(1)).updateActgSts(any(), any());
//        verify(settlementProdMapper, times(1)).updateBizSts(any(), any());
//        verify(commonStsctrlMapper, times(1)).deleteByPrimaryKey(any());
//        verify(storageForwardManager, times(1)).saveForInst(any(), eq(true));
//    }
//
//    @Test
//    public void testFail_Process() {
//        settlementProdDO.setBizSts(ClearingStatusEnum.PROCESS.getCode());
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
//        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(1);
//
//        Response<StorageForwardDO> response = settlementManager.fail(settlementProdDO, accountingInstrDO, true);
//
//        assertNull(response);
//        verify(accountingInstrMapper, times(1)).updateActgSts(any(), any());
//        verify(settlementProdMapper, times(1)).updateBizSts(any(), any());
//        verify(commonStsctrlMapper, times(1)).deleteByPrimaryKey(any());
//        verify(storageForwardManager, times(1)).saveForInst(any(), eq(true));
//    }
//
//    @Test
//    public void testFail_OtherStatus() {
//        settlementProdDO.setBizSts("OTHER_STATUS");
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
//        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(1);
//        DcepException exception = assertThrows(DcepException.class, () -> {
//            settlementManager.fail(settlementProdDO, accountingInstrDO,  true);
//        });
//
//        assertEquals(WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getCode(), exception.getCode());
//        assertEquals(WholesaleErrorEnum.BIZSTS_NOT_SUPPORTED.getDescription(), exception.getMessage());
//    }
//
//    @Test
//    public void testFail_NoMatchOriginal() {
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(0);
//
//        DcepException exception = assertThrows(DcepException.class, () -> {
//            settlementManager.fail(settlementProdDO, accountingInstrDO,  true);
//        });
//
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), exception.getCode());
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), exception.getMessage());
//    }
//    @Test
//    public void testFail_NoMatchOriginalSettlement() {
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
//        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(0);
//
//        settlementProdDO.setBizSts(ClearingStatusEnum.WAIT_SETTLE.getCode());
//        DcepException exception = assertThrows(DcepException.class, () -> {
//            settlementManager.fail(settlementProdDO, accountingInstrDO,  true);
//        });
//
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), exception.getCode());
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), exception.getMessage());
//
//
//        settlementProdDO.setBizSts(ClearingStatusEnum.PROCESS.getCode());
//        exception = assertThrows(DcepException.class, () -> {
//            settlementManager.fail(settlementProdDO, accountingInstrDO,  true);
//        });
//
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), exception.getCode());
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), exception.getMessage());
//    }
//    @Test
//    public void testSettled_Success() {
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
//        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(1);
////        when(DtoUtil.assembly200Msg(any(), any(), any(), any())).thenReturn(outReq);
//        when(storageForwardManager.saveForInst(any(EnvelopeDTO.class),any(String.class))).thenReturn(new StorageForwardDO());
//
//        StorageForwardDO response = settlementManager.settled(accountingInstrDO, false);
//
//        assertNull(response);
//        verify(accountingInstrMapper, times(1)).updateActgSts(any(), any());
//        verify(settlementProdMapper, times(1)).updateBizSts(any(), any());
//        verify(commonStsctrlMapper, times(1)).deleteByPrimaryKey(any());
//        verify(storageForwardManager, times(0)).saveForInst(any(EnvelopeDTO.class),any(String.class));
//
//        accountingInstrDO.setSendPtyId("2323232");
//        response = settlementManager.settled(accountingInstrDO, true);
//        verify(storageForwardManager, times(1)).saveForInst(any(EnvelopeDTO.class),any(String.class));
//    }
//
//    @Test
//    public void testSettled_NoMatchOriginal_UpdateActgSts() {
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(0);
//
//        DcepException exception = assertThrows(DcepException.class, () -> {
//            settlementManager.settled(accountingInstrDO, true);
//        });
//
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), exception.getCode());
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), exception.getMessage());
//    }
//
//    @Test
//    public void testSettled_NoMatchOriginal_UpdateBizSts() {
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
//        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(0);
//
//        DcepException exception = assertThrows(DcepException.class, () -> {
//            settlementManager.settled(accountingInstrDO, true);
//        });
//
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), exception.getCode());
//        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), exception.getMessage());
//    }
//
//    @Test
//    public void testSettled_NoOut() {
//        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
//        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(1);
//
//        StorageForwardDO response = settlementManager.settled(accountingInstrDO, false);
//
//        assertNull(response);
//        verify(accountingInstrMapper, times(1)).updateActgSts(any(), any());
//        verify(settlementProdMapper, times(1)).updateBizSts(any(), any());
//        verify(commonStsctrlMapper, times(1)).deleteByPrimaryKey(any());
//        verify(storageForwardManager, never()).saveForInst(any(EnvelopeDTO.class),any(String.class));
//    }


    @Test
    public void testReversalSuccess_Success() {
        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(1);
//        when(DtoUtil.assembly200Msg(any(), any(), any(), any())).thenReturn(outReq);
        when(storageForwardManager.saveForInst(any(EnvelopeDTO.class),any(String.class))).thenReturn(new StorageForwardDO());

        accountingInstrDO.setActgSts(AccountingStatusEnum.QUEUED.getCode());
        settlementProdDO.setBizSts(ClearingStatusEnum.SETTLE_QUEUE.getCode());
        StorageForwardDO response = settlementManager.reversalSuccess(accountingInstrDO, settlementProdDO,true);

        assertNull(response);
        verify(accountingInstrMapper, times(1)).updateActgSts(any(), any());
        verify(settlementProdMapper, times(1)).updateBizSts(any(), any());
//        verify(storageForwardManager, times(1)).saveForInst(any());
    }

    @Test
    public void testReversalSuccess_NoMatchOriginal_UpdateActgSts() {
        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(0);
        accountingInstrDO.setActgSts("3");
        settlementProdDO.setBizSts("PR11");
        DcepException exception = assertThrows(DcepException.class, () -> {
            settlementManager.reversalSuccess(accountingInstrDO, settlementProdDO,true);
        });

        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), exception.getCode());
        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), exception.getMessage());
    }

    @Test
    public void testReversalSuccess_NoMatchOriginal_UpdateBizSts() {
        when(accountingInstrMapper.updateActgSts(any(), any())).thenReturn(1);
        when(settlementProdMapper.updateBizSts(any(), any())).thenReturn(0);
        accountingInstrDO.setActgSts("3");
        settlementProdDO.setBizSts("PR11");
        DcepException exception = assertThrows(DcepException.class, () -> {
            settlementManager.reversalSuccess(accountingInstrDO, settlementProdDO,true);
        });

        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), exception.getCode());
        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), exception.getMessage());
    }
}