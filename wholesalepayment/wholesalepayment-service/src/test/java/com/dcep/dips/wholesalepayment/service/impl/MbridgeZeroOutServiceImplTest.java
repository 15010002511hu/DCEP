package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.common.utils.TimeUtil;
import com.dcep.dips.wholesalepayment.dal.mapper.AccountingInstrMapper;
import com.dcep.dips.wholesalepayment.dal.mapper.ZerooutCtrlDOMapper;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.dto.mcbs100.Mcbs10000101DTO;
import com.dcep.dips.wholesalepayment.dto.mcbs100.NoInf;
import com.dcep.dips.wholesalepayment.dto.mcbs101.*;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.enums.McbsStatusEnum;
import com.dcep.dips.wholesalepayment.manager.ChainZeroOutManager;
import com.dcep.dips.wholesalepayment.manager.MbridgeZeroOutManager;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.GenericGwDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsEnvelopeDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsGwDTO;
import com.dcep.gateway.mcbdc.dto.soap.McbsSoapBody;
import com.dcep.gateway.mcbdc.dto.soap.McbsSoapHeader;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MbridgeZeroOutServiceImplTest {

    @InjectMocks
    private MbridgeZeroOutServiceImpl mbridgeZeroOutService;

    @Mock
    private ZerooutCtrlDOMapper zerooutCtrlDOMapper;
    @Mock
    private ChainZeroOutManager chainZeroOutManager;
    @Mock
    private AccountingInstrMapper accountingInstrMapper;
    @Mock
    private MbridgeZeroOutManager mbridgeZeroOutManager;

    private Mcbs10100101DTO mcbs101DTO;
    private McbsEnvelopeDTO<McbsGwDTO> mcbsEnvelopeDTO;

    @Before
    public void setUp() {

        mcbs101DTO = new Mcbs10100101DTO();
        mcbs101DTO.setOrgnlGrpInf(new OrgnlGrpInf());
        mcbs101DTO.getOrgnlGrpInf().setOrgnlMsgId("20251004002120390233908295230000");
        ClrDtls clrDtls = new ClrDtls();
        List<ClrDtlInf> clrDtlInfs = new ArrayList<>();
        clrDtls.setClrDtlInf(clrDtlInfs);
        clrDtls.setClrZeDt(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATE_PATTERN));
        mcbs101DTO.setClrDtls(clrDtls);
        MsgHdr msgHdr = new MsgHdr();
        msgHdr.setMsgId("20251004002120390233908295230000");
        msgHdr.setCreDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        mcbs101DTO.setMsgHdr(msgHdr);
        McbsSoapHeader soapHeader = new McbsSoapHeader(Constant.MCBS_SOAPHEADER_VER, TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN), Constant.MCBS_MSGTYPE_900,
                "senderLEI", "senderCBMALEI",
                "reciverLEI", "receiverCBMALEI",
                Constant.DIRECTION_FROM_HLHT_TO_MBRIDGE, null, null, null, "wholesalepayment");
        mcbsEnvelopeDTO = new McbsEnvelopeDTO<>();
        McbsSoapBody<McbsGwDTO> soapBody = new McbsSoapBody<>();
        soapBody.setT(mcbs101DTO);

        mcbsEnvelopeDTO.setSoapHeader(soapHeader);
        mcbsEnvelopeDTO.setSoapBody(soapBody);

        when(chainZeroOutManager.selectByMsgId("20251004002120390233908295230000")).thenReturn(new ZerooutCtrlDO());
        when(chainZeroOutManager.selectByMsgId("20251004002120390233908295230000")).thenReturn(null);
    }

    @Test
    @DisplayName("测试 report 方法 - 正常情况")
    public void testReportSuccess() throws DcepException {
        // 模拟链上清零控制表查询成功
        when(chainZeroOutManager.selectByMsgId("20251004002120390233908295230000")).thenReturn(new ZerooutCtrlDO());
        //when(zerooutCtrlDOMapper.updateByPrimaryKey(any(ZerooutCtrlDO.class))).thenReturn(-1);;
        //when(accountingInstrMapper.insert(any(AccountingInstrDO.class))).thenReturn(-1);

        // 调用 report 方法
        Response<GenericEnvelopeDTO<GenericGwDTO>> response = mbridgeZeroOutService.report(mcbsEnvelopeDTO);

        // 验证结果
        assertTrue(response.isSuccess());
        assertEquals(McbsStatusEnum.SUCD.getCode(), response.getErrorCode());

        // 验证方法调用
        verify(chainZeroOutManager, times(1)).selectByMsgId("20251004002120390233908295230000");
        verify(mbridgeZeroOutManager, times(1)).recordZOCtrlAndAcctInstr(any(Mcbs10100101DTO.class),any(ZerooutCtrlDO.class));
    }

    @Test
    @DisplayName("测试 report 方法 - 报文类型非 mcbs101")
    public void testReportNotMcbs101() throws DcepException {
        // 模拟报文类型非 mcbs101
        McbsSoapBody<McbsGwDTO> soapBody = new McbsSoapBody<>();
        Mcbs10000101DTO mcbs10000101DTO = new Mcbs10000101DTO();
        com.dcep.dips.wholesalepayment.dto.mcbs100.GrpHdr grpHdr = new com.dcep.dips.wholesalepayment.dto.mcbs100.GrpHdr();
        grpHdr.setMsgId("20251004002120390233908295230000");
        grpHdr.setCreDtTm(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATETIME_PATTERN));
        mcbs10000101DTO.setGrpHdr(grpHdr);
        NoInf noInf = new NoInf();
        noInf.setClrZeDt(TimeUtil.getMcbsCurrentTime(DcepDateUtils.ISO_DATE_PATTERN));
        mcbs10000101DTO.setNoInf(noInf);

        soapBody.setT(mcbs10000101DTO);
        mcbsEnvelopeDTO.setSoapBody(soapBody);
        // 调用 report 方法
        Response<GenericEnvelopeDTO<GenericGwDTO>> response = mbridgeZeroOutService.report(mcbsEnvelopeDTO);

        // 验证结果
        assertFalse(response.isSuccess());
        assertEquals(McbsStatusEnum.FAIL.getCode(), response.getErrorCode());
        assertEquals(Constant.MBRIDGE_REQHDLG_DESC_TPERR, response.getErrorMsg());

        // 验证方法调用
        verify(chainZeroOutManager, never()).selectByMsgId(anyString());
        verify(mbridgeZeroOutManager, never()).recordZOCtrlAndAcctInstr(any(Mcbs10100101DTO.class),any(ZerooutCtrlDO.class));
    }

    @Test
    @DisplayName("测试 report 方法 - 原清零结果通知交易不存在")
    public void testReportNoOriginal() throws DcepException {
        // 模拟原清零结果通知交易不存在
        when(chainZeroOutManager.selectByMsgId("20251004002120390233908295230000")).thenReturn(null);

        // 调用 report 方法
        Response<GenericEnvelopeDTO<GenericGwDTO>> response = mbridgeZeroOutService.report(mcbsEnvelopeDTO);

        // 验证结果
        assertFalse(response.isSuccess());
        assertEquals(McbsStatusEnum.FAIL.getCode(), response.getErrorCode());
        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), response.getErrorMsg());

        // 验证方法调用
        verify(chainZeroOutManager, times(1)).selectByMsgId("20251004002120390233908295230000");
        verify(mbridgeZeroOutManager, never()).recordZOCtrlAndAcctInstr(any(Mcbs10100101DTO.class),any(ZerooutCtrlDO.class));
    }

    @Test
    @DisplayName("测试 report 方法 - 清零状态异常")
    public void testReportZeroOutStatusError() throws DcepException {
        // 模拟清零状态异常
        ZerooutCtrlDO zerooutCtrlDO = new ZerooutCtrlDO();
        zerooutCtrlDO.setPrcSts(Constant.ZERO_OUT_CTRL_STATUS_00);
        when(chainZeroOutManager.selectByMsgId("20251004002120390233908295230000")).thenReturn(zerooutCtrlDO);

        // 调用 report 方法
        Response<GenericEnvelopeDTO<GenericGwDTO>> response = mbridgeZeroOutService.report(mcbsEnvelopeDTO);

        // 验证结果-成功
        assertTrue(response.isSuccess());

        // 验证方法调用
        verify(chainZeroOutManager, times(1)).selectByMsgId("20251004002120390233908295230000");
        verify(mbridgeZeroOutManager, never()).recordZOCtrlAndAcctInstr(any(Mcbs10100101DTO.class),any(ZerooutCtrlDO.class));
    }
}
