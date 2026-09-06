package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.bcsp.core.api.DcepOnChainService;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.chain.ZeroOutReportDTO;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.manager.ChainZeroOutManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChainZeroOutServiceImplTest {

    @InjectMocks
    private ChainZeroOutServiceImpl chainZeroOutServiceImpl;

    @Mock
    private ChainZeroOutManager chainZeroOutManager;

    @Mock
    private DcepOnChainService dcepOnChainService;

    private ZeroOutReportDTO zeroOutReportDTO;

    @Before
    public void setUp() {

        zeroOutReportDTO = new ZeroOutReportDTO();
        zeroOutReportDTO.setOrgnlMsgId("20251004002120390233908295230000");
        zeroOutReportDTO.setMsgId("20251004002120390233908295230000");
    }


    @Test
    @DisplayName("测试链上清零结果通知成功")
    public void testReportSuccess() throws DcepException {
        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setPrcSts("01");
        zerooutCtrlDO.setTaskId("202510231748WHOLESALETASK0002");
        when(chainZeroOutManager.selectByMsgId("20251004002120390233908295230000")).thenReturn(zerooutCtrlDO);

        Response<String> response = chainZeroOutServiceImpl.report(zeroOutReportDTO);

        assertTrue(response.isSuccess());
        assertEquals("成功", response.getResult());
        verify(chainZeroOutManager, times(1)).selectByMsgId("20251004002120390233908295230000");
        verify(chainZeroOutManager, times(1)).recordZOCtrlAndAcctInstr(zeroOutReportDTO,zerooutCtrlDO);
    }


    @Test
    @DisplayName("测试链上清零结果通知原清零结果通知交易不存在")
    public void testReportOriginalMsgIdNotFound() throws DcepException {
        when(chainZeroOutManager.selectByMsgId("20251004002120390233908295230000")).thenReturn(null);

        Response<String> response = chainZeroOutServiceImpl.report(zeroOutReportDTO);

        assertFalse(response.isSuccess());
        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), response.getErrorCode());
        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), response.getErrorMsg());
    }

    @Test
    @DisplayName("测试链上清零结果通知清零状态已处理")
    public void testReportZeroOutStatusProcessed() throws DcepException {
        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setPrcSts("00");
        when(chainZeroOutManager.selectByMsgId("20251004002120390233908295230000")).thenReturn(zerooutCtrlDO);

        Response<String> response = chainZeroOutServiceImpl.report(zeroOutReportDTO);

        assertFalse(response.isSuccess());
        assertEquals(ErrorEnum.BUSI_DUPLICATION.getCode(), response.getErrorCode());
        assertEquals(ErrorEnum.BUSI_DUPLICATION.getDescription(), response.getErrorMsg());
    }

}
