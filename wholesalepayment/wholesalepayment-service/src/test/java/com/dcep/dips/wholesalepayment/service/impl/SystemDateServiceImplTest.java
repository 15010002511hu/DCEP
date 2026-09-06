package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.bcsp.core.api.DcepOnChainService;
import com.dcep.common.model.Response;
import com.dcep.dips.operatingcontrol.dto.TaskRequestDTO;
import com.dcep.dips.operatingcontrol.dto.TaskResultDTO;
import com.dcep.dips.wholesalepayment.dal.model.SystemStatusDO;
import com.dcep.dips.wholesalepayment.dal.model.TaskExecutionControlDO;
import com.dcep.dips.wholesalepayment.dal.model.ZerooutCtrlDO;
import com.dcep.dips.wholesalepayment.enums.ErrorEnum;
import com.dcep.dips.wholesalepayment.manager.*;
import com.dcep.gateway.mcbdc.dto.soap.GenericEnvelopeDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DuplicateKeyException;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SystemDateServiceImplTest {

    @InjectMocks
    private SystemDateServiceImpl systemDateServiceImpl;

    @Mock
    private SystemStatusManager systemStatusManager;

    @Mock
    private TaskExecutionManager taskExecutionManager;

    @Mock
    private ChainZeroOutManager chainZeroOutManager;

    @Mock
    private MbridgeZeroOutManager mbridgeZeroOutManager;

    @Mock
    private CommonStsctrlManager commonStsctrlManager;

    @Mock
    private DcepOnChainService dcepOnChainService;

    private TaskRequestDTO taskRequestDTO;

    public void setTaskRequestDTO(String taskId,String taskCode, String taskName, String curSysFlg,String sysDt) {
        taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTaskId(taskId);
        taskRequestDTO.setTaskCode(taskCode);
        taskRequestDTO.setTaskName(taskName);
        taskRequestDTO.setSystemDate(sysDt);
        Map<String, Object> params = new HashMap<>();
        params.put("currentSystemFlag", curSysFlg);
        params.put("currentSystemDate", sysDt);
        params.put("previousSystemFlag", "A");
        params.put("originalSystemStatus","B");
        params.put("originalSystemDate","20251026");
        params.put("nextSystemDate","20251027");
        params.put("EodStatus","00");
        taskRequestDTO.setParams(params);
    }

    @Test
    @DisplayName("测试货币桥清零任务成功")
    public void testB0101Success() {

        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "货币桥清零", "A", "20251024");

        //模拟依赖方法
        when(taskExecutionManager.recordTaskExecute(any(TaskExecutionControlDO.class))).thenReturn(1);
        when(mbridgeZeroOutManager.mbridgeGateway(any(GenericEnvelopeDTO.class))).thenReturn(new Response<>(true, null, null, null));
        when(chainZeroOutManager.recordZeroOutCtrl(any(ZerooutCtrlDO.class))).thenReturn(1);

        // 执行测试
        Response<TaskResultDTO> response = systemDateServiceImpl.execute(taskRequestDTO);

        // 验证结果
        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
        assertEquals("202510231748WHOLESALETASK0002", response.getResult().getTaskId());
        assertEquals("B0101", response.getResult().getTaskCode());
        assertEquals("货币桥清零", response.getResult().getTaskName());
        assertEquals("20251024", response.getResult().getSystemDate());
        assertEquals("1", response.getResult().getTaskStatus());

        // 验证调用次数
        verify(taskExecutionManager, times(1)).recordTaskExecute(any(TaskExecutionControlDO.class));
        verify(mbridgeZeroOutManager, times(1)).mbridgeGateway(any(GenericEnvelopeDTO.class));
    }

    @Test
    @DisplayName("测试货币桥清零任务失败")
    public void testB0101Fail() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "货币桥清零", "A", "20251024");
        // 模拟依赖方法
        when(taskExecutionManager.recordTaskExecute(any(TaskExecutionControlDO.class))).thenReturn(1);
        when(mbridgeZeroOutManager.mbridgeGateway(any(GenericEnvelopeDTO.class))).thenReturn(new Response<>(false, null, ErrorEnum.BUSI_COMP_ERROR.getCode(), ErrorEnum.BUSI_COMP_ERROR.getDescription()));

        // 执行测试
        Response<TaskResultDTO> response = systemDateServiceImpl.execute(taskRequestDTO);

        // 验证结果
        assertFalse(response.isSuccess());
        assertNotNull(response.getResult());
        assertEquals("202510231748WHOLESALETASK0002", response.getResult().getTaskId());
        assertEquals("B0101", response.getResult().getTaskCode());
        assertEquals("货币桥清零", response.getResult().getTaskName());
        assertEquals("20251024", response.getResult().getSystemDate());
        assertEquals("2", response.getResult().getTaskStatus());

        // 验证调用次数
        verify(taskExecutionManager, times(1)).recordTaskExecute(any(TaskExecutionControlDO.class));
        verify(mbridgeZeroOutManager, times(1)).mbridgeGateway(any(GenericEnvelopeDTO.class));
    }

    @Test
    @DisplayName("测试任务编码不合法")
    public void testInvalidTaskCode() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "货币桥清零", "A", "20251024");
        taskRequestDTO.setTaskCode("INVALID");

        // 执行测试
        Response<TaskResultDTO> response = systemDateServiceImpl.execute(taskRequestDTO);

        // 验证结果
        assertFalse(response.isSuccess());
        assertNull(response.getResult());
        assertEquals(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), response.getErrorCode());
        assertEquals(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getDescription(), response.getErrorMsg());
    }

    @Test
    @DisplayName("测试参数为空")
    public void testNullParams() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "货币桥清零", "A", "20251024");

        taskRequestDTO.setParams(null);

        // 执行测试
        Response<TaskResultDTO> response = systemDateServiceImpl.execute(taskRequestDTO);

        // 验证结果
        assertFalse(response.isSuccess());
        assertNull(response.getResult());
        assertEquals(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), response.getErrorCode());
        assertEquals(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getDescription(), response.getErrorMsg());
    }

    @Test
    @DisplayName("测试参数含空值")
    public void testParamsWithNullValue() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "货币桥清零", "A", "20251024");

        Map<String, Object> params = new HashMap<>();
        params.put("sysId", null);
        taskRequestDTO.setParams(params);

        // 执行测试
        Response<TaskResultDTO> response = systemDateServiceImpl.execute(taskRequestDTO);

        // 验证结果
        assertFalse(response.isSuccess());
        assertNull(response.getResult());
        assertEquals(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getCode(), response.getErrorCode());
        assertEquals(com.dcep.common.enums.ErrorEnum.REQUEST_PARAM_ILLEGAL.getDescription(), response.getErrorMsg());
    }

    @Test
    @DisplayName("测试幂等性")
    public void testIdempotency() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "货币桥清零", "A", "20251024");
        // 模拟依赖方法
        when(taskExecutionManager.recordTaskExecute(any(TaskExecutionControlDO.class))).thenThrow(new DuplicateKeyException(""));

        // 执行测试
        Response<TaskResultDTO> response = systemDateServiceImpl.execute(taskRequestDTO);

        // 验证结果
        assertFalse(response.isSuccess());
        assertNull(response.getResult());
        assertEquals(ErrorEnum.BUSI_DUPLICATION.getCode(), response.getErrorCode());
        assertEquals(ErrorEnum.BUSI_DUPLICATION.getDescription(), response.getErrorMsg());
    }

    @Test
    @DisplayName("测试日切状态通知成功")
    public void testD0401Success() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "日切状态通知", "A", "20251024");

        taskRequestDTO.setTaskCode("D0401");

        // 模拟依赖方法
        when(systemStatusManager.selectByPrimaryKey(anyString())).thenReturn(new SystemStatusDO());
        when(systemStatusManager.updateByPrimaryKey(any(SystemStatusDO.class))).thenReturn(1);

        // 执行测试
        Response<TaskResultDTO> response = systemDateServiceImpl.execute(taskRequestDTO);

        // 验证结果
        assertTrue(response.isSuccess());
        assertNotNull(response.getResult());
        assertEquals("202510231748WHOLESALETASK0002", response.getResult().getTaskId());
        assertEquals("D0401", response.getResult().getTaskCode());
        assertEquals("日切状态通知", response.getResult().getTaskName());
        assertEquals("20251024", response.getResult().getSystemDate());
        assertEquals("1", response.getResult().getTaskStatus());

        // 验证调用次数
        verify(systemStatusManager, times(1)).selectByPrimaryKey(anyString());
        verify(systemStatusManager, times(1)).updateByPrimaryKey(any(SystemStatusDO.class));
    }

    @Test
    @DisplayName("测试日切状态通知失败")
    public void testD0401Fail() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "货币桥清零", "A", "20251024");
        taskRequestDTO.setTaskCode("D0401");

        // 模拟依赖方法
        when(systemStatusManager.selectByPrimaryKey(anyString())).thenReturn(null);

        // 执行测试
        Response<TaskResultDTO> response = systemDateServiceImpl.execute(taskRequestDTO);

        // 验证结果
        assertFalse(response.isSuccess());
        assertNull(response.getResult());
        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getCode(), response.getErrorCode());
        assertEquals(ErrorEnum.NO_MATCH_ORIGNAL.getDescription(), response.getErrorMsg());

        // 验证调用次数
        verify(systemStatusManager, times(1)).selectByPrimaryKey(anyString());
        verify(systemStatusManager, never()).updateByPrimaryKey(any(SystemStatusDO.class));
    }
}
