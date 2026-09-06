package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.dips.operatingcontrol.dto.TaskRequestDTO;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SystemDateServiceImplSpringBootTest {

    private TaskRequestDTO taskRequestDTO;

    public void setTaskRequestDTO(String taskId, String taskCode, String taskName, String curSysFlg, String sysDt) {
        taskRequestDTO = new TaskRequestDTO();
        taskRequestDTO.setTaskId(taskId);
        taskRequestDTO.setTaskCode(taskCode);
        taskRequestDTO.setTaskName(taskName);
        taskRequestDTO.setSystemDate(sysDt);
        Map<String, Object> params = new HashMap<>();
        params.put("currentSystemFlag", curSysFlg);
        params.put("currentSystemDate", sysDt);
        params.put("previousSystemFlag", "A");
        params.put("originalSystemStatus", "B");
        params.put("originalSystemDate", "20251026");
        params.put("nextSystemDate", "20251027");
        params.put("EodStatus", "00");
        taskRequestDTO.setParams(params);
    }

    /**
     * 货币桥清零通知
     */
    public void testexecuteTASK_CODE_B0101() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0101", "货币桥清零通知", "A", "20251024");


    }

    /**
     * JISR清零通知
     */
    public void testexecuteTASK_CODE_B0102() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0102", "JISR清零通知", "A", "20251024");
    }


    /**
     * 货币桥清零完成通知
     */
    public void testexecuteTASK_CODE_C0201() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "C0201", "货币桥清零完成通知", "A", "20251024");
    }

    /**
     * JISR清零完成通知
     */
    public void testexecuteTASK_CODE_B0202() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0202", "JISR清零完成通知", "A", "20251024");
    }

    /**
     * 区块链服务平台清零
     */
    public void testexecuteTASK_CODE_B0103() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "B0103", "区块链服务平台清零", "A", "20251024");
    }


    /**
     * 日切状态通知
     */
    public void testexecuteTASK_CODE_D0401() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "D0401", "日切状态通知", "A", "20251024");
    }

    /**
     * 终止T日业务受理通知
     */
    public void testexecuteTASK_CODE_D0402() {
        setTaskRequestDTO("202510231748WHOLESALETASK0002",
                "D0402", "终止T日业务受理通知", "A", "20251024");
    }


}



