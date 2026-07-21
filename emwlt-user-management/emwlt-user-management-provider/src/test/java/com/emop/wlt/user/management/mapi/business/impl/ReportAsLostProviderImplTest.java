package com.emop.wlt.user.management.mapi.business.impl;

import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.gateway.manager.EmapGatewayManager;
import com.emop.wlt.user.management.exception.UserManageBuzException;
import com.emop.wlt.user.management.exception.UserManageException;
import com.emop.wlt.user.management.helper.MockFlowContext;
import com.emop.wlt.user.management.helper.RequestModelHelper;
import com.emop.wlt.user.management.model.request.Mapp31300101Req;
import com.emop.wlt.user.management.model.request.Mapp31400101Req;
import com.emop.wlt.user.management.model.response.Mapp31300101Resp;
import com.emop.wlt.user.management.model.response.Mapp31400101Resp;
import com.emop.wlt.user.management.provider.UserProviderImpl;
import com.emop.wlt.user.management.service.MappingIndexManagementService;
import com.emop.wlt.user.wallet.api.UserWalletQueryProvider;
import com.emop.wlt.user.wallet.dto.CustomerIdInfoDTO;
import com.emop.wlt.user.wallet.dto.WalletBasicInfoDTO;
import com.emop.wlt.user.wallet.dto.WalletInfoDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReportAsLostProviderImplTest {

    @InjectMocks
    private ReportAsLostProviderImpl reportAsLostProvider;
    @Mock
    private UserWalletQueryProvider userWalletQueryProvider;
    @Mock
    private MappingIndexManagementService mappingIndexManagementService;
    @Mock
    private EmapGatewayManager emapGatewayManager;
    @Mock
    private UserProviderImpl userProviderImpl;

    @BeforeClass
    public static void beforeClass() {
        MockFlowContext.buildFlowContextMock();
    }

    @Before
    public void before() {
        MockFlowContext.mockFlowContext(null);
    }

    @AfterClass
    public static void afterClass() {
        MockFlowContext.closeFlowContextMock();
    }

    @Test
    public void testPreCheck1() {
        //正常 - WL03
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        WalletBasicInfoDTO walletBasicInfo = new WalletBasicInfoDTO();
        walletBasicInfo.setWalletId("1122334455667788");
        walletBasicInfo.setWalletLevel("WL03");
        walletBasicInfo.setWalletStatus("WS01");
        walletBasicInfo.setWalletType("01");
        CustomerIdInfoDTO customerIdInfoDTO = new CustomerIdInfoDTO();
        customerIdInfoDTO.setIdNumber("idNumber");
        customerIdInfoDTO.setIdType("IT01");
        WalletInfoDTO walletInfoDTO = new WalletInfoDTO();
        walletInfoDTO.setWalletBasicInfo(walletBasicInfo);
        walletInfoDTO.setCustomerIdInfo(customerIdInfoDTO);
        List<WalletInfoDTO> walletInfoDTOList = new ArrayList<>();
        walletInfoDTOList.add(walletInfoDTO);
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(walletInfoDTOList);

        Mapp31300101Req mapp31300101Req = new Mapp31300101Req();
        mapp31300101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp31300101Req> request = RequestModelHelper.buildRequestModel(mapp31300101Req);
        ResponseModel<Mapp31300101Resp> result = reportAsLostProvider.preCheck(request);
        Assert.assertEquals(customerIdInfoDTO.getIdType(), result.getMessageBody().getCustomerIdInfo().getIdType());
        Assert.assertEquals(customerIdInfoDTO.getIdNumber(), result.getMessageBody().getCustomerIdInfo().getIdNumber());
        Assert.assertEquals(walletBasicInfo.getWalletId(), result.getMessageBody().getWalletBasicInfo().getWalletId());
        Assert.assertEquals(walletBasicInfo.getWalletLevel(), result.getMessageBody().getWalletBasicInfo().getWalletLevel());
        Assert.assertEquals(walletBasicInfo.getWalletType(), result.getMessageBody().getWalletBasicInfo().getWalletType());
        Assert.assertEquals("WL01_02_03", result.getMessageBody().getFlowRespInfo().getRouteCondition());
    }

    @Test
    public void testPreCheck2() {
        //正常 - WL04
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        WalletBasicInfoDTO walletBasicInfo = new WalletBasicInfoDTO();
        walletBasicInfo.setWalletId("1122334455667788");
        walletBasicInfo.setWalletLevel("WL04");
        walletBasicInfo.setWalletStatus("WS01");
        walletBasicInfo.setWalletType("01");
        CustomerIdInfoDTO customerIdInfoDTO = new CustomerIdInfoDTO();
        customerIdInfoDTO.setIdNumber("idNumber");
        customerIdInfoDTO.setIdType("IT01");
        WalletInfoDTO walletInfoDTO = new WalletInfoDTO();
        walletInfoDTO.setWalletBasicInfo(walletBasicInfo);
        walletInfoDTO.setCustomerIdInfo(customerIdInfoDTO);
        List<WalletInfoDTO> walletInfoDTOList = new ArrayList<>();
        walletInfoDTOList.add(walletInfoDTO);
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(walletInfoDTOList);

        Mapp31300101Req mapp31300101Req = new Mapp31300101Req();
        mapp31300101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp31300101Req> request = RequestModelHelper.buildRequestModel(mapp31300101Req);
        ResponseModel<Mapp31300101Resp> result = reportAsLostProvider.preCheck(request);
        Assert.assertEquals(customerIdInfoDTO.getIdType(), result.getMessageBody().getCustomerIdInfo().getIdType());
        Assert.assertEquals(customerIdInfoDTO.getIdNumber(), result.getMessageBody().getCustomerIdInfo().getIdNumber());
        Assert.assertEquals(walletBasicInfo.getWalletId(), result.getMessageBody().getWalletBasicInfo().getWalletId());
        Assert.assertEquals(walletBasicInfo.getWalletLevel(), result.getMessageBody().getWalletBasicInfo().getWalletLevel());
        Assert.assertEquals(walletBasicInfo.getWalletType(), result.getMessageBody().getWalletBasicInfo().getWalletType());
        Assert.assertEquals("WL04", result.getMessageBody().getFlowRespInfo().getRouteCondition());
    }

    @Test
    public void testPreCheck3() {
        //未注册
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("");
        Mapp31300101Req mapp31300101Req = new Mapp31300101Req();
        mapp31300101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp31300101Req> request = RequestModelHelper.buildRequestModel(mapp31300101Req);
        Assert.assertThrows("账号未注册", UserManageException.class,
            ()-> reportAsLostProvider.preCheck(request));
    }

    @Test
    public void testPreCheck4() {
        //钱包状态正常-无钱包
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(new ArrayList<>());

        Mapp31300101Req mapp31300101Req = new Mapp31300101Req();
        mapp31300101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp31300101Req> request = RequestModelHelper.buildRequestModel(mapp31300101Req);
        Assert.assertThrows("钱包状态正常", UserManageBuzException.class,
            ()-> reportAsLostProvider.preCheck(request));
    }

    @Test
    public void testPreCheck5() {
        //超过钱包列表最大长度限制
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        List<WalletInfoDTO> walletInfoDTOList = new ArrayList<>();
        walletInfoDTOList.add(new WalletInfoDTO());
        walletInfoDTOList.add(new WalletInfoDTO());
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(walletInfoDTOList);

        Mapp31300101Req mapp31300101Req = new Mapp31300101Req();
        mapp31300101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp31300101Req> request = RequestModelHelper.buildRequestModel(mapp31300101Req);
        Assert.assertThrows("超过钱包列表最大长度限制", UserManageBuzException.class,
            ()-> reportAsLostProvider.preCheck(request));
    }

    @Test
    public void testPreCheck6() {
        //钱包已挂失
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        WalletBasicInfoDTO walletBasicInfo = new WalletBasicInfoDTO();
        walletBasicInfo.setWalletId("1122334455667788");
        walletBasicInfo.setWalletLevel("WL03");
        walletBasicInfo.setWalletStatus("WS03");
        walletBasicInfo.setWalletType("01");
        CustomerIdInfoDTO customerIdInfoDTO = new CustomerIdInfoDTO();
        customerIdInfoDTO.setIdNumber("idNumber");
        customerIdInfoDTO.setIdType("IT01");
        WalletInfoDTO walletInfoDTO = new WalletInfoDTO();
        walletInfoDTO.setWalletBasicInfo(walletBasicInfo);
        walletInfoDTO.setCustomerIdInfo(customerIdInfoDTO);
        List<WalletInfoDTO> walletInfoDTOList = new ArrayList<>();
        walletInfoDTOList.add(walletInfoDTO);
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(walletInfoDTOList);

        Mapp31300101Req mapp31300101Req = new Mapp31300101Req();
        mapp31300101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp31300101Req> request = RequestModelHelper.buildRequestModel(mapp31300101Req);
        Assert.assertThrows("钱包状态正常", UserManageBuzException.class,
            ()-> reportAsLostProvider.preCheck(request));
    }

    @Test
    public void testPreCheck7() {
        //钱包状态异常
        when(mappingIndexManagementService.selectByPhone(anyString())).thenReturn("123456");
        WalletBasicInfoDTO walletBasicInfo = new WalletBasicInfoDTO();
        walletBasicInfo.setWalletId("1122334455667788");
        walletBasicInfo.setWalletLevel("WL03");
        walletBasicInfo.setWalletStatus("WS05");
        walletBasicInfo.setWalletType("01");
        CustomerIdInfoDTO customerIdInfoDTO = new CustomerIdInfoDTO();
        customerIdInfoDTO.setIdNumber("idNumber");
        customerIdInfoDTO.setIdType("IT01");
        WalletInfoDTO walletInfoDTO = new WalletInfoDTO();
        walletInfoDTO.setWalletBasicInfo(walletBasicInfo);
        walletInfoDTO.setCustomerIdInfo(customerIdInfoDTO);
        List<WalletInfoDTO> walletInfoDTOList = new ArrayList<>();
        walletInfoDTOList.add(walletInfoDTO);
        when(userWalletQueryProvider.queryUserWalletFromInst(anyString())).thenReturn(walletInfoDTOList);

        Mapp31300101Req mapp31300101Req = new Mapp31300101Req();
        mapp31300101Req.setMobileNumber("+853-60001234");
        RequestModel<Mapp31300101Req> request = RequestModelHelper.buildRequestModel(mapp31300101Req);
        Assert.assertThrows("钱包状态异常", UserManageBuzException.class,
            ()-> reportAsLostProvider.preCheck(request));
    }

    @Test
    public void testReportAsLost() {
        doNothing().when(userProviderImpl).reportAsLost(anyString());
        when(emapGatewayManager.send(any())).thenReturn(new com.emop.gateway.dto.emap.ResponseModel<EmapDTO>());

        ResponseModel<Mapp31400101Resp> result = reportAsLostProvider.reportAsLost(
            new RequestModel<Mapp31400101Req>());
        Assert.assertNotNull(result);
    }
}
