
package com.dcep.dips.wholesalepayment.manager.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.acctrans.api.AccountingService;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferReqDTO;
import com.dcep.dips.acctrans.dto.accounting.transfer.TransferRespDTO;
import com.dcep.dips.common.constant.CommonConstant;
import com.dcep.dips.wholesalepayment.common.utils.IdUtils;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.SystemStatusDOMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ZerooutCtrlDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.AccountingInstrDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutReportDTO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutResult;
import com.dcep.dips.wholesalepayment.enums.WholesaleErrorEnum;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChainZeroOutManagerImplTest {

    @InjectMocks
    private ChainZeroOutManagerImpl chainZeroOutManager;

    @Mock
    private ZerooutCtrlDOMapper zerooutCtrlDOMapper;

    @Mock
    private AccountingInstrMapper accountingInstrMapper;

    @Mock
    private SystemStatusDOMapper systemStatusMapper;

    @Mock
    private AccountingService accountingService;

    @Mock
    private ExecutorService asyncPool;

    @Before
    public void setUp() {
        // 初始化Mock对象
        when(systemStatusMapper.selectCurSysDt(CommonConstant.SysCode.WHOLESALE)).thenReturn("20231010");

    }

    @Test
    @DisplayName("测试记录清零控制表和记账指令表成功")
    public void testRecordZOCtrlAndAcctInstrSuccess() {
        ZeroOutReportDTO zeroOutReportDto = new ZeroOutReportDTO();
        zeroOutReportDto.setMsgId("testMsgId");
        zeroOutReportDto.setOrgnlMsgId("testOrgnlMsgId");

        List<ZeroOutResult> zeroOutResultList = new ArrayList<>();
        ZeroOutResult zeroOutResult = new ZeroOutResult();
        zeroOutResultList.add(zeroOutResult);
        zeroOutReportDto.setZeroOutResultlList(zeroOutResultList);

        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setMsgId("testOrgnlMsgId");

        MockedStatic<IdUtils> idUtilsMockedStatic = mockStatic(IdUtils.class);
        when(IdUtils.randomTransIdWithBizDt(anyString())).thenReturn("1234567890");
        MockedStatic<InfoCacheUtil> idUtilsMockedStatic2 = mockStatic(InfoCacheUtil.class);
        when(InfoCacheUtil.getPbocInf()).thenReturn("test");
        when(accountingInstrMapper.insert(any(AccountingInstrDO.class))).thenReturn(1);
        when(zerooutCtrlDOMapper.updateByPrimaryKey(any(ZerooutCtrlDO.class))).thenReturn(1);
        try {
          chainZeroOutManager.recordZOCtrlAndAcctInstr(zeroOutReportDto, zerooutCtrlDO);
        }catch (Exception e){
            Assert.assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),e.getMessage());
        }finally {
            idUtilsMockedStatic.close();
            idUtilsMockedStatic2.close();
        }
        verify(accountingInstrMapper, times(1)).insert(any(AccountingInstrDO.class));
        verify(zerooutCtrlDOMapper, times(1)).updateByPrimaryKey(any(ZerooutCtrlDO.class));
    }

    @Test
    @DisplayName("测试记录清零控制表和记账指令表失败")
    public void testRecordZOCtrlAndAcctInstrFailure() {
        ZeroOutReportDTO zeroOutReportDto = new ZeroOutReportDTO();
        zeroOutReportDto.setMsgId("20251014001WHOLESALE994443200001");
        zeroOutReportDto.setOrgnlMsgId("20251014001WHOLESALE994443200000");

        List<ZeroOutResult> zeroOutResultList = new ArrayList<>();
        ZeroOutResult zeroOutResult = new ZeroOutResult();
        zeroOutResultList.add(zeroOutResult);
        zeroOutReportDto.setZeroOutResultlList(zeroOutResultList);

        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setMsgId("testOrgnlMsgId");
        MockedStatic<IdUtils> idUtilsMockedStatic = mockStatic(IdUtils.class);
        MockedStatic<InfoCacheUtil> idUtilsMockedStatic2 = mockStatic(InfoCacheUtil.class);
        when(InfoCacheUtil.getPbocInf()).thenReturn("test");
        when(IdUtils.randomTransIdWithBizDt(anyString())).thenReturn("1234567890");
        when(accountingInstrMapper.insert(any(AccountingInstrDO.class))).thenReturn(0);
        try {
            chainZeroOutManager.recordZOCtrlAndAcctInstr(zeroOutReportDto, zerooutCtrlDO);
            verify(accountingInstrMapper, times(1)).insert(any(AccountingInstrDO.class));
        }catch (Exception e){
            Assert.assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(),e.getMessage());
        }finally {
            idUtilsMockedStatic.close();
            idUtilsMockedStatic2.close();
        }


    }

    @Test
    @DisplayName("测试获取原交易成功")
    public void testSelectByMsgIdSuccess() {
        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setMsgId("testMsgId");
        when(zerooutCtrlDOMapper.selectByPrimaryKey(anyString())).thenReturn(zerooutCtrlDO);

        ZerooutCtrlDO result = chainZeroOutManager.selectByMsgId("testMsgId");

        assertEquals(zerooutCtrlDO, result);
        verify(zerooutCtrlDOMapper, times(1)).selectByPrimaryKey(anyString());
    }

    @Test
    @DisplayName("测试获取原交易失败")
    public void testSelectByMsgIdFailure() {
        when(zerooutCtrlDOMapper.selectByPrimaryKey(anyString())).thenReturn(null);

        ZerooutCtrlDO result = chainZeroOutManager.selectByMsgId("testMsgId");

        assertEquals(null, result);
        verify(zerooutCtrlDOMapper, times(1)).selectByPrimaryKey(anyString());
    }

    @Test
    @DisplayName("测试异步调用结算钱包成功")
    public void testAsyncTransferSuccess() {
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("1234567890");
        accountingInstrDO.setMsgId("testMsgId");

        Response<TransferRespDTO> response = new Response<>(true,null,null,null);
        TransferRespDTO transferRespDTO = new TransferRespDTO();
        transferRespDTO.setAccountingDate("20231010");
        response.setResult(transferRespDTO);

        doAnswer(invocation -> {
            Runnable runnable = (Runnable) invocation.getArguments()[0];
            runnable.run();
            return null;
        }).when(asyncPool).execute(any(Runnable.class));

        when(accountingService.transfer(any(TransferReqDTO.class))).thenReturn(response);

        chainZeroOutManager.asyncTransfer(accountingInstrDO);

        verify(accountingInstrMapper, times(1)).updateAccountingInstr(accountingInstrDO);
    }

    @Test
    @DisplayName("测试异步调用结算钱包失败")
    public void testAsyncTransferFailure() {
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("1234567890");
        accountingInstrDO.setMsgId("testMsgId");

        Response<TransferRespDTO> response = new Response<>(false,null,null,null);

        doAnswer(invocation -> {
            Runnable runnable = (Runnable) invocation.getArguments()[0];
            runnable.run();
            return null;
        }).when(asyncPool).execute(any(Runnable.class));

        when(accountingService.transfer(any(TransferReqDTO.class))).thenReturn(response);

        chainZeroOutManager.asyncTransfer(accountingInstrDO);

        verify(accountingInstrMapper, times(0)).updateAccountingInstr(accountingInstrDO);
    }

    @Test
    @DisplayName("测试调用结算钱包成功")
    public void testTransferSuccess() {
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("1234567890");
        accountingInstrDO.setMsgId("testMsgId");

        Response<TransferRespDTO> response = new Response<>(true,null,null,null);
        TransferRespDTO transferRespDTO = new TransferRespDTO();
        transferRespDTO.setAccountingDate("20231010");
        response.setResult(transferRespDTO);

        when(accountingService.transfer(any(TransferReqDTO.class))).thenReturn(response);

        Response<TransferRespDTO> result = chainZeroOutManager.transfer(accountingInstrDO);

        assertEquals(response, result);
        verify(accountingService, times(1)).transfer(any(TransferReqDTO.class));
    }

    @Test
    @DisplayName("测试调用结算钱包异常")
    public void testTransferException() {
        AccountingInstrDO accountingInstrDO = new AccountingInstrDO();
        accountingInstrDO.setTransId("1234567890");
        accountingInstrDO.setMsgId("testMsgId");

        when(accountingService.transfer(any(TransferReqDTO.class))).thenThrow(new RuntimeException("未知异常"));

        Response<TransferRespDTO> result = chainZeroOutManager.transfer(accountingInstrDO);

        assertFalse(result.isSuccess());
        assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getCode(), result.getErrorCode());
        assertEquals(WholesaleErrorEnum.UNKNOWN_EXCEPTION.getDescription(), result.getErrorMsg());
        verify(accountingService, times(1)).transfer(any(TransferReqDTO.class));
    }
}