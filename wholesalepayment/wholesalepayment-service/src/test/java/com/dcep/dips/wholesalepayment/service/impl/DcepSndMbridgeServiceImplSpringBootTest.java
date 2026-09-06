package com.dcep.dips.wholesalepayment.service.impl;

import com.dcep.common.exception.DcepException;
import com.dcep.common.model.Response;
import com.dcep.dips.wholesalepayment.common.constants.Constant;
import com.dcep.dips.wholesalepayment.utils.InfoCacheUtil;
import com.dcep.dips.wholesalepayment.utils.TestUtils;
import com.dcep.gateway.mcbdc.dto.common.GroupHeader;
import com.dcep.gateway.mcbdc.dto.common.OriginGroupInfoAndStatus;
import com.dcep.gateway.mcbdc.dto.mcbs203.Mcbs20300101DTO;
import com.dcep.gateway.mcbdc.dto.mcbs203.TransactionInfoAndStatus;
import com.dcep.gateway.mcbdc.dto.mcbs203.TransactionInfoAndStatusSupplementaryData;
import com.dcep.gateway.mcbdc.dto.soap.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DcepSndMbridgeServiceImplSpringBootTest {

    @Autowired
    private DcepSndMbridgeServiceImpl dcepSndMbridgeServiceImpl;

    @Before
    public void setUp() {

    }

    @Test
    @DisplayName("测试桥下发起dcep213下桥,桥上应答成功")
    public void testMbridgeAccountingSuccess() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("000");
            String orgMcbsMsgId = "20251016070000042064686501414000";
            String orgMsgTp = "dcep.213.010.01";

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(generateMcbs203EnvelopeDTO(orgMcbsMsgId, orgMsgTp));
            System.out.println(response.isSuccess() + "--" + response.getResult() + "--" + response.getErrorCode() + "--" + response.getErrorMsg());
        }
    }

    @Test
    @DisplayName("测试桥下发起dcep203上桥,桥上应答成功")
    public void testMbridgeAccountingSuccess1() throws DcepException {
        try (MockedStatic<InfoCacheUtil> mockedInfoCacheUtils = Mockito.mockStatic(InfoCacheUtil.class)) {
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.checkInstState(anyString()))
                    .thenReturn(true);
            mockedInfoCacheUtils.when(() -> InfoCacheUtil.getOrgInnerCode(anyString()))
                    .thenReturn("000");
            String orgMcbsMsgId = "20251021070000039133453594141000";
            String orgMsgTp = "dcep.203.010.01";

            Response<GenericEnvelopeDTO<GenericGwDTO>> response = dcepSndMbridgeServiceImpl.finish(generateMcbs203EnvelopeDTO(orgMcbsMsgId, orgMsgTp));
            System.out.println(response.isSuccess() + "--" + response.getResult() + "--" + response.getErrorCode() + "--" + response.getErrorMsg());
        }
    }

    private GenericEnvelopeDTO<GenericGwDTO> generateMcbs203EnvelopeDTO(String orgMcbsMsgId, String orgMsgTp){
        GroupHeader groupHeader = new GroupHeader();
        groupHeader.setMsgId(generateRandomString());
        groupHeader.setCreateDateTime(TestUtils.getCurDate());

        OriginGroupInfoAndStatus originGroupInfoAndStatus = new OriginGroupInfoAndStatus();
        originGroupInfoAndStatus.setOriginMsgId(orgMcbsMsgId);
        originGroupInfoAndStatus.setOriginMsgName(orgMsgTp);

        TransactionInfoAndStatus transactionInfoAndStatus = new TransactionInfoAndStatus();
        transactionInfoAndStatus.setStatusId("SUCD");
        transactionInfoAndStatus.setTransactionInfoAndStatusSupplementaryData(new TransactionInfoAndStatusSupplementaryData(Constant.MBRIDGE_203_PLACE_AND_NAME, null));


        Mcbs20300101DTO mcbs20300101DTO = Mcbs20300101DTO.builder()
                .grpHdr(groupHeader)
                .originGroupInfoAndStatus(originGroupInfoAndStatus)
                .transactionInfoAndStatus(transactionInfoAndStatus)
                .build();

        McbsEnvelopeDTO<McbsGwDTO> mcbsGwReqDTO = new McbsEnvelopeDTO<>();
        mcbsGwReqDTO.setSoapBody(new McbsSoapBody<>(mcbs20300101DTO));
        mcbsGwReqDTO.setSoapHeader(TestUtils.generateMcbsSoapHeader(orgMsgTp));
        return mcbsGwReqDTO;
    }

    private String generateRandomString() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(22);
        for (int i = 0; i < 22; i++) {
            sb.append(random.nextInt(10)); // 生成0-9之间的随机数字
        }
        return getSysTime("yyyyMMddHH") + sb;
    }
    private String getSysTime(String pattern){
        // 获取当前系统时间
        LocalDateTime now = LocalDateTime.now();

        // 定义日期时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);

        // 格式化当前时间
        return now.format(formatter);
    }
}
