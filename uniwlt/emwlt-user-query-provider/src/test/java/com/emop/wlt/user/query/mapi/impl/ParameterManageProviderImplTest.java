package com.emop.wlt.user.query.mapi.impl;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.alibaba.fastjson.JSONObject;
import com.emop.doms.cloudcontrol.query.api.UserInfoProvider;
import com.emop.doms.cloudcontrol.query.dto.CloudControlUserInfoRespDTO;
import com.emop.infocache.HelpCache;
import com.emop.infocache.OrgCache;
import com.emop.infocache.ParamCache;
import com.emop.infocache.WalletCache;
import com.emop.infocache.api.dto.FiTechParamAttachMasterDTO;
import com.emop.infocache.api.dto.HardWalletFreePayConfigDTO;

import com.emop.infocache.api.dto.OrgDTO;
import com.emop.infocache.api.dto.ReleaseHistoryDTO;
import com.emop.infocache.api.dto.help.HelpClassifyDetailDTO;
import com.emop.infocache.api.dto.help.HelpClassifySortDTO;
import com.emop.infocache.api.dto.help.HelpDetailDTO;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.user.query.exception.UserQueryException;
import com.emop.wlt.user.query.helper.BankCardInfoHelper;
import com.emop.wlt.user.query.helper.ElementHelper;
import com.emop.wlt.user.query.helper.BusinessParamHelper;
import com.emop.wlt.user.query.helper.CloudControlHelper;
import com.emop.wlt.user.query.helper.HelpQuestionHelper;
import com.emop.wlt.user.query.helper.ProtocolHelper;
import com.emop.wlt.user.query.helper.RequestModelHelper;
import com.emop.wlt.user.query.manager.CountryAndRegionManager;
import com.emop.wlt.user.query.manager.GrayUserCheckManager;
import com.emop.wlt.user.query.manager.WalletLimitManager;
import com.emop.wlt.user.query.model.request.Mapp09000101Req;
import com.emop.wlt.user.query.model.request.Mapp09100101Req;
import com.emop.wlt.user.query.model.request.Mapp09200101Req;
import com.emop.wlt.user.query.model.request.Mapp09300101Req;
import com.emop.wlt.user.query.model.request.Mapp09400101Req;
import com.emop.wlt.user.query.model.request.Mapp09600101Req;
import com.emop.wlt.user.query.model.request.Mapp20200101Req;
import com.emop.wlt.user.query.model.request.Mapp20300101Req;
import com.emop.wlt.user.query.model.response.Mapp09000101Resp;
import com.emop.wlt.user.query.model.response.Mapp09100101Resp;
import com.emop.wlt.user.query.model.response.Mapp09200101Resp;
import com.emop.wlt.user.query.model.response.Mapp09300101Resp;
import com.emop.wlt.user.query.model.response.Mapp09400101Resp;
import com.emop.wlt.user.query.model.response.Mapp20200101Resp;
import com.emop.wlt.user.query.model.response.Mapp20300101Resp;
import com.emop.wlt.user.query.model.vo.Common;
import com.emop.wlt.user.query.model.vo.HelpClassify;
import com.emop.wlt.user.query.model.vo.QuestionDetail;
import com.emop.wlt.user.query.model.vo.QuestionSummary;
import com.emop.wlt.user.query.model.vo.UserConfig;
import com.emop.wlt.user.query.util.ConfigConstructorUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class ParameterManageProviderImplTest {

    @InjectMocks
    private ParameterManageProviderImpl parameterManageProvider;
    @Mock
    private HelpCache helpCache;
    @Mock
    private ParamCache paramCache;
    @Mock
    private WalletCache walletCache;
    @Mock
    private OrgCache orgCache;
    @Mock
    private UserInfoProvider userInfoProvider;
    @Mock
    private CountryAndRegionManager countryAndRegionManager;
    @Mock
    private WalletLimitManager walletLimitManager;
    @Mock
    private GrayUserCheckManager grayUserCheckManager;
    @Mock
    private ConfigConstructorUtils configConstructorUtils;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(parameterManageProvider, "commonConfig", "commonConfig");
        ReflectionTestUtils.setField(parameterManageProvider, "instConfigVersion", "instConfigVersion");

        //构建帮助详情信息
        HelpDetailDTO helpDetailDTO1 = HelpQuestionHelper.buildHelpDetail1();
        when(helpCache.getDetail("0", "QU0005202410140245")).thenReturn(helpDetailDTO1);
        HelpDetailDTO helpDetailDTO2 = HelpQuestionHelper.buildHelpDetail2();
        when(helpCache.getDetail("0", "QU0005202410143607")).thenReturn(helpDetailDTO2);

        //构建分类排序
        HelpClassifySortDTO helpClassifySortDTO1 = new HelpClassifySortDTO();
        helpClassifySortDTO1.setSortId(1);
        when(helpCache.getClassifySort("0", "CC0005202410147401")).thenReturn(helpClassifySortDTO1);
        HelpClassifySortDTO helpClassifySortDTO2 = new HelpClassifySortDTO();
        helpClassifySortDTO2.setSortId(2);
        when(helpCache.getClassifySort("0", "CC0005202410145482")).thenReturn(helpClassifySortDTO2);

        when(grayUserCheckManager.isGray(anyString())).thenReturn(false);
    }

    @Test
    public void queryHelpClassifyList1() {
        //正常
        QuestionSummary questionSummary1 = new QuestionSummary();
        questionSummary1.setQuestionName("手機號註冊不了數字澳門元怎麼辦？");
        questionSummary1.setQuestionNo("QU0005202410140245");
        List<QuestionSummary> questionSummaryList1 = new ArrayList<>();
        questionSummaryList1.add(questionSummary1);
        QuestionSummary questionSummary2 = new QuestionSummary();
        questionSummary2.setQuestionName("為什麼我的銀行卡沒法綁定？");
        questionSummary2.setQuestionNo("QU0005202410143607");
        List<QuestionSummary> questionSummaryList2 = new ArrayList<>();
        questionSummaryList2.add(questionSummary2);
        HelpClassify helpClassify1 = new HelpClassify();
        helpClassify1.setClassifySortId("1");
        helpClassify1.setClassifyKeyword("常见问题HK");
        helpClassify1.setClassifyName("常见问题HK");
        helpClassify1.setClassifyIcon("https");
        helpClassify1.setQuestionSummaryList(questionSummaryList1);
        HelpClassify helpClassify2 = new HelpClassify();
        helpClassify2.setClassifySortId("2");
        helpClassify2.setClassifyKeyword("硬钱包HK");
        helpClassify2.setClassifyName("硬钱包HK");
        helpClassify2.setClassifyIcon("https");
        helpClassify2.setQuestionSummaryList(questionSummaryList2);
        List<HelpClassify> excptedResponseList = new ArrayList<>();
        excptedResponseList.add(helpClassify1);
        excptedResponseList.add(helpClassify2);

        when(helpCache.listClassifyNo(anyString())).thenReturn(HelpQuestionHelper.buildClassifyDetailDTOList());

        Mapp20200101Req mapp20200101Req =  new Mapp20200101Req();
        mapp20200101Req.setMobileNumber("");
        mapp20200101Req.setLanguage("zh-HK");
        mapp20200101Req.setPlatform("android");
        mapp20200101Req.setAppVersion("1.0");
        RequestModel<Mapp20200101Req> request = RequestModelHelper.buildRequestModel(mapp20200101Req);
        ResponseModel<Mapp20200101Resp> actualResponseModel = parameterManageProvider.queryHelpClassifyList(request);
        Assert.assertEquals(excptedResponseList, actualResponseModel.getMessageBody().getHelpClassifyList());
    }

    @Test
    public void queryHelpClassifyList2() {
        //异常-帮助分类列表为null
        when(helpCache.listClassifyNo(anyString())).thenReturn(null);

        Mapp20200101Req mapp20200101Req =  new Mapp20200101Req();
        mapp20200101Req.setMobileNumber("");
        mapp20200101Req.setLanguage("zh-HK");
        mapp20200101Req.setPlatform("android");
        mapp20200101Req.setAppVersion("1.0");
        RequestModel<Mapp20200101Req> request = RequestModelHelper.buildRequestModel(mapp20200101Req);
        ResponseModel<Mapp20200101Resp> actualResponseModel = parameterManageProvider.queryHelpClassifyList(request);
        Assert.assertNull(actualResponseModel.getMessageBody().getHelpClassifyList());
    }


    @Test
    public void queryQuestionDetail_QT00() {
        QuestionDetail questionDetail= new QuestionDetail();
        questionDetail.setQuestionNo("QU0005202410143607");
        questionDetail.setQuestion("為什麼我的銀行卡沒法綁定？");
        questionDetail.setAnswer("請確保......");
        List<QuestionDetail> excptedResponseList = new ArrayList<>();
        excptedResponseList.add(questionDetail);

        Mapp20300101Req mapp20300101Req =  new Mapp20300101Req();
        mapp20300101Req.setMobileNumber("");
        mapp20300101Req.setQuestionType("QT00");
        mapp20300101Req.setLanguage("zh-HK");
        mapp20300101Req.setPlatform("android");
        mapp20300101Req.setAppVersion("1.0");
        mapp20300101Req.setQuestionNo("QU0005202410143607");
        RequestModel<Mapp20300101Req> request = RequestModelHelper.buildRequestModel(mapp20300101Req);
        ResponseModel<Mapp20300101Resp> actualResponseModel = parameterManageProvider.queryQuestionDetail(request);
        Assert.assertEquals(excptedResponseList, actualResponseModel.getMessageBody().getQuestionDetailList());
    }

    @Test
    public void queryQuestionDetail_QT01() {
        QuestionDetail questionDetail= new QuestionDetail();
        questionDetail.setQuestionNo("QU0005202410143607");
        questionDetail.setQuestion("为什么我的银行卡没法绑定？");
        questionDetail.setAnswer("请确保......");
        List<QuestionDetail> excptedResponseList = new ArrayList<>();
        excptedResponseList.add(questionDetail);

        HelpClassifyDetailDTO helpClassifyDetailDTO = HelpQuestionHelper.buildHelpClassifyDetail1();
        when(helpCache.getClassifyDetail(anyString(), any())).thenReturn(helpClassifyDetailDTO);

        Mapp20300101Req mapp20300101Req =  new Mapp20300101Req();
        mapp20300101Req.setMobileNumber("");
        mapp20300101Req.setQuestionType("QT01");
        mapp20300101Req.setLanguage("zh-CN");
        mapp20300101Req.setPlatform("iOS");
        mapp20300101Req.setAppVersion("1.0");
        mapp20300101Req.setQuestionNo("");
        RequestModel<Mapp20300101Req> request = RequestModelHelper.buildRequestModel(mapp20300101Req);
        ResponseModel<Mapp20300101Resp> actualResponseModel = parameterManageProvider.queryQuestionDetail(request);
        Assert.assertEquals(excptedResponseList, actualResponseModel.getMessageBody().getQuestionDetailList());
    }

    @Test
    public void queryQuestionDetail_QT02() {
        QuestionDetail questionDetail= new QuestionDetail();
        questionDetail.setQuestionNo("QU0005202410140245");
        questionDetail.setQuestion("What shall I do when I cannot sign up with mobile number?");
        questionDetail.setAnswer("Please try to ......");
        List<QuestionDetail> excptedResponseList = new ArrayList<>();
        excptedResponseList.add(questionDetail);

        HelpClassifyDetailDTO helpClassifyDetailDTO = HelpQuestionHelper.buildHelpClassifyDetail2();
        when(helpCache.getClassifyDetail(anyString(), any())).thenReturn(helpClassifyDetailDTO);

        Mapp20300101Req mapp20300101Req =  new Mapp20300101Req();
        mapp20300101Req.setMobileNumber("");
        mapp20300101Req.setQuestionType("QT02");
        mapp20300101Req.setLanguage("en-US");
        mapp20300101Req.setPlatform("android");
        mapp20300101Req.setAppVersion("1.0");
        mapp20300101Req.setQuestionNo("");
        RequestModel<Mapp20300101Req> request = RequestModelHelper.buildRequestModel(mapp20300101Req);
        ResponseModel<Mapp20300101Resp> actualResponseModel = parameterManageProvider.queryQuestionDetail(request);
        Assert.assertEquals(excptedResponseList, actualResponseModel.getMessageBody().getQuestionDetailList());
    }

    @Test
    public void queryQuestionDetail_QTXX() {
        Mapp20300101Req mapp20300101Req =  new Mapp20300101Req();
        mapp20300101Req.setMobileNumber("");
        mapp20300101Req.setQuestionType("QTXX");
        mapp20300101Req.setLanguage("en-US");
        mapp20300101Req.setPlatform("android");
        mapp20300101Req.setAppVersion("1.0");
        mapp20300101Req.setQuestionNo("");

        RequestModel<Mapp20300101Req> request = RequestModelHelper.buildRequestModel(mapp20300101Req);
        ResponseModel<Mapp20300101Resp> actualResponseModel = parameterManageProvider.queryQuestionDetail(request);
        Assert.assertEquals(0, actualResponseModel.getMessageBody().getQuestionDetailList().size());
    }

    @Test
    public void queryBusinessParam_ST00() {
        String expectedContent = "\"content\":{\"#\":[{\"code\":\"+853\",\"countryCode\":\"MO\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"mobileNumberRule\":\"checkRule_MO\",\"name\":\"中國澳門\"},{\"code\":\"+86\",\"countryCode\":\"CN\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"mobileNumberRule\":\"checkRule_CN\",\"name\":\"中國內地\"},{\"code\":\"+852\",\"countryCode\":\"HK\",\"firstLetter\":\"#\",\"isAllowRegister\":\"1\",\"mobileNumberRule\":\"checkRule_HK\",\"name\":\"中國香港\"}]}";
        when(countryAndRegionManager.queryCountryAndRegionCode(anyString(), anyString())).thenReturn(expectedContent);

        Mapp09000101Req mapp09000101Req = new Mapp09000101Req();
        mapp09000101Req.setSceneType("ST00");
        mapp09000101Req.setLanguage("zh-HK");
        mapp09000101Req.setBizType("BT00");
        RequestModel<Mapp09000101Req> request = RequestModelHelper.buildRequestModel(mapp09000101Req);
        ResponseModel<Mapp09000101Resp> actualResponseModel = parameterManageProvider.queryBusinessParam(request);
        Assert.assertEquals(expectedContent, actualResponseModel.getMessageBody().getContent());
        Assert.assertNotNull(actualResponseModel.getMessageBody().getCurrentDateTime());
    }

    @Test
    public void queryBusinessParam_ST01() {
        List<HardWalletFreePayConfigDTO> hardWalletFreePayConfigDTOList = BusinessParamHelper.buildHardWalletFreePayConfigDTO();
        when(paramCache.getHardWalletFreePayConfig()).thenReturn(hardWalletFreePayConfigDTOList);
        String expectedContent = JSONObject.toJSONString(hardWalletFreePayConfigDTOList);

        Mapp09000101Req mapp09000101Req = new Mapp09000101Req();
        mapp09000101Req.setSceneType("ST01");
        RequestModel<Mapp09000101Req> request = RequestModelHelper.buildRequestModel(mapp09000101Req);
        ResponseModel<Mapp09000101Resp> actualResponseModel = parameterManageProvider.queryBusinessParam(request);
        Assert.assertEquals(expectedContent, actualResponseModel.getMessageBody().getContent());
        Assert.assertNotNull(actualResponseModel.getMessageBody().getCurrentDateTime());
    }

    @Test
    public void queryBusinessParam_ST02() {
        String expectedContent = "钱包限额数据";
        when(walletLimitManager.queryWalletLimit(anyString(), anyString())).thenReturn(expectedContent);

        Mapp09000101Req mapp09000101Req = new Mapp09000101Req();
        mapp09000101Req.setSceneType("ST02");
        mapp09000101Req.setInstNo("002000");
        mapp09000101Req.setWalletLevel("WL04");
        RequestModel<Mapp09000101Req> request = RequestModelHelper.buildRequestModel(mapp09000101Req);
        ResponseModel<Mapp09000101Resp> actualResponseModel = parameterManageProvider.queryBusinessParam(request);
        Assert.assertEquals(expectedContent, actualResponseModel.getMessageBody().getContent());
        Assert.assertNotNull(actualResponseModel.getMessageBody().getCurrentDateTime());
    }

    @Test
    public void queryBusinessParam_STXX() {
        Mapp09000101Req mapp09000101Req = new Mapp09000101Req();
        mapp09000101Req.setSceneType("STXX");
        mapp09000101Req.setInstNo("002000");
        mapp09000101Req.setWalletLevel("WL04");
        RequestModel<Mapp09000101Req> request = RequestModelHelper.buildRequestModel(mapp09000101Req);
        ResponseModel<Mapp09000101Resp> actualResponseModel = parameterManageProvider.queryBusinessParam(request);
        Assert.assertNull(actualResponseModel.getMessageBody().getContent());
        Assert.assertNotNull(actualResponseModel.getMessageBody().getCurrentDateTime());
    }

    @Test
    public void queryLanguageAndProtocol1() {
        //正式数据
        ReleaseHistoryDTO elementData = ElementHelper.buildElementData();
        when(walletCache.getRelease("100")).thenReturn(elementData);
        ReleaseHistoryDTO protocolData = ProtocolHelper.buildProtocolData();
        when(walletCache.getRelease("200")).thenReturn(protocolData);

        Mapp09100101Req mapp09100101Req = new Mapp09100101Req();
        mapp09100101Req.setMobileNumber("");
        mapp09100101Req.setLanguage("zh-HK");
        mapp09100101Req.setAppVersion("AppVersion");
        mapp09100101Req.setElementReleaseId("ElementReleaseId");
        mapp09100101Req.setProtocolReleaseId("ProtocolReleaseId");
        mapp09100101Req.setAppPrivacyProtocolNo("2024101415321928");
        RequestModel<Mapp09100101Req> request = RequestModelHelper.buildRequestModel(mapp09100101Req);
        ResponseModel<Mapp09100101Resp> actualResponseModel = parameterManageProvider.queryLanguageAndProtocol(request);
        Assert.assertEquals("zh-HK", actualResponseModel.getMessageBody().getLanguage());
        Assert.assertEquals("AppVersion", actualResponseModel.getMessageBody().getAppVersion());
        Assert.assertEquals(ElementHelper.buildExpectedElementOfficial(), actualResponseModel.getMessageBody().getElement());
        Assert.assertEquals(ProtocolHelper.buildExpectedProtocolDataOfficial(), actualResponseModel.getMessageBody().getProtocol());
    }

    @Test
    public void queryLanguageAndProtocol2() {
        //灰度数据
        when(grayUserCheckManager.isGray(anyString())).thenReturn(true);

        ReleaseHistoryDTO elementData = ElementHelper.buildElementData();
        when(walletCache.getRelease("100")).thenReturn(elementData);
        ReleaseHistoryDTO protocolData = ProtocolHelper.buildProtocolData();
        when(walletCache.getRelease("200")).thenReturn(protocolData);

        Mapp09100101Req mapp09100101Req = new Mapp09100101Req();
        mapp09100101Req.setMobileNumber("+853-60001234");
        mapp09100101Req.setLanguage("zh-HK");
        mapp09100101Req.setAppVersion("AppVersion");
        mapp09100101Req.setElementReleaseId("ElementReleaseId");
        mapp09100101Req.setProtocolReleaseId("ProtocolReleaseId");
        mapp09100101Req.setAppPrivacyProtocolNo("2024101415321928");
        RequestModel<Mapp09100101Req> request = RequestModelHelper.buildRequestModel(mapp09100101Req);
        ResponseModel<Mapp09100101Resp> actualResponseModel = parameterManageProvider.queryLanguageAndProtocol(request);
        Assert.assertEquals("zh-HK", actualResponseModel.getMessageBody().getLanguage());
        Assert.assertEquals("AppVersion", actualResponseModel.getMessageBody().getAppVersion());
        Assert.assertEquals(ElementHelper.buildExpectedElementGray(), actualResponseModel.getMessageBody().getElement());
        Assert.assertEquals(ProtocolHelper.buildExpectedProtocolDataGray(), actualResponseModel.getMessageBody().getProtocol());
    }

    @Test
    public void queryCommonParam_1() {
        Map<String, String> commonCache = new HashMap<>();
        commonCache.put("trackEventsAndroid", "{\"ANDROID_Event_1\":\"ANDROID1\",\"ANDROID_Event_2\":\"ANDROID2\"}");
        commonCache.put("trackEventsIOS", "{\"IOS_Event_1\":\"IOS1\",\"IOS_Event_2\":\"IOS2\"}");
        when(paramCache.getCommonMap()).thenReturn(commonCache);
        when(countryAndRegionManager.queryMobileNumberLengthRule()).thenReturn(
            "{\"transferMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"},\"registerMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"}}");

        JSONObject expectedCommonMap = new JSONObject();
        expectedCommonMap.put("trackEventsAndroid", "{\"ANDROID_Event_1\":\"ANDROID1\",\"ANDROID_Event_2\":\"ANDROID2\"}");
        expectedCommonMap.put("mobileNumberLength", "{\"transferMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"},\"registerMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"}}");
        Common expectedCommon = Common.builder().content(expectedCommonMap).build();

        Mapp09200101Req mapp09200101Req = new Mapp09200101Req();
        mapp09200101Req.setPlatform("android");
        RequestModel<Mapp09200101Req> request = RequestModelHelper.buildRequestModel(mapp09200101Req);
        ResponseModel<Mapp09200101Resp> actualResponseModel = parameterManageProvider.queryCommonParam(request);
        Assert.assertEquals(expectedCommon, actualResponseModel.getMessageBody().getCommon());
    }

    @Test
    public void queryCommonParam_2() {
        Map<String, String> commonCache = new HashMap<>();
        commonCache.put("trackEventsAndroid", "{\"ANDROID_Event_1\":\"ANDROID1\",\"ANDROID_Event_2\":\"ANDROID2\"}");
        commonCache.put("trackEventsIOS", "{\"IOS_Event_1\":\"IOS1\",\"IOS_Event_2\":\"IOS2\"}");
        when(paramCache.getCommonMap()).thenReturn(commonCache);
        when(countryAndRegionManager.queryMobileNumberLengthRule()).thenReturn(
            "{\"transferMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"},\"registerMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"}}");

        JSONObject expectedCommonMap = new JSONObject();
        expectedCommonMap.put("trackEventsIOS", "{\"IOS_Event_1\":\"IOS1\",\"IOS_Event_2\":\"IOS2\"}");
        expectedCommonMap.put("mobileNumberLength", "{\"transferMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"},\"registerMobileNumberLength\":{\"+86\":\"11\",\"+852\":\"8\",\"+853\":\"8\"}}");
        Common expectedCommon = Common.builder().content(expectedCommonMap).build();

        Mapp09200101Req mapp09200101Req = new Mapp09200101Req();
        mapp09200101Req.setPlatform("iOS");
        RequestModel<Mapp09200101Req> request = RequestModelHelper.buildRequestModel(mapp09200101Req);
        ResponseModel<Mapp09200101Resp> actualResponseModel = parameterManageProvider.queryCommonParam(request);
        Assert.assertEquals(expectedCommon, actualResponseModel.getMessageBody().getCommon());
    }

    @Test
    public void queryCloudControl() {
        List<CloudControlUserInfoRespDTO> cloudControlUserInfoRespDTOList = CloudControlHelper.buildCloudControlConfigData();
        when(userInfoProvider.getFunctionConfigList(anyString())).thenReturn(cloudControlUserInfoRespDTOList);
        UserConfig userConfig = CloudControlHelper.buildUserConfig();
        when(configConstructorUtils.configConstructorNew(any(), anyString())).thenReturn(userConfig);

        Mapp09300101Req mapp09300101Req = new Mapp09300101Req();
        mapp09300101Req.setDeviceModel("Samsung-5G");
        RequestModel<Mapp09300101Req> request = RequestModelHelper.buildRequestModel(mapp09300101Req);
        ResponseModel<Mapp09300101Resp> actualResponseModel = parameterManageProvider.queryCloudControl(request);
        Assert.assertEquals(userConfig.getConfig(), actualResponseModel.getMessageBody().getConfig());
        Assert.assertEquals(userConfig.getVersion(), actualResponseModel.getMessageBody().getUserConfigVersion());
        Assert.assertEquals("123456", actualResponseModel.getMessageBody().getUserId());
        Assert.assertEquals("1800", actualResponseModel.getMessageBody().getRefreshTime());
    }

    @Test
    public void queryBankCardInfo_1() {
        List<CloudControlUserInfoRespDTO> cloudControlUserInfoRespDTOList = CloudControlHelper.buildCloudControlConfigData();
        when(userInfoProvider.getFunctionConfigList(anyString())).thenReturn(cloudControlUserInfoRespDTOList);

        Mapp09400101Req mapp09400101Req = new Mapp09400101Req();
        mapp09400101Req.setVersion("2.0");
        mapp09400101Req.setLanguage("zh-HK");
        RequestModel<Mapp09400101Req> request = RequestModelHelper.buildRequestModel(mapp09400101Req);
        ResponseModel<Mapp09400101Resp> actualResponseModel = parameterManageProvider.queryBankCardInfo(request);
        ResponseModel<Mapp09400101Resp> expectedResponseModel =
            ResponseModel.<Mapp09400101Resp>builder().messageBody(Mapp09400101Resp.builder().uptodate("1").build()).build();
        Assert.assertEquals(expectedResponseModel, actualResponseModel);
    }

    @Test
    public void queryBankCardInfo_2() {
        List<CloudControlUserInfoRespDTO> cloudControlUserInfoRespDTOList = CloudControlHelper.buildCloudControlConfigData();
        when(userInfoProvider.getFunctionConfigList(anyString())).thenReturn(cloudControlUserInfoRespDTOList);
        List<FiTechParamAttachMasterDTO> fiTechParamAttachMasterDTOList = BankCardInfoHelper.buildBankCardInfo();
        when(walletCache.listBankCardInfos()).thenReturn(fiTechParamAttachMasterDTOList);
        Map<String, OrgDTO> fiInf = BankCardInfoHelper.buildFiInf();
        when(orgCache.getFiInf("002000")).thenReturn(fiInf.get("002000"));
        when(orgCache.getFiInf("003000")).thenReturn(fiInf.get("003000"));
        when(orgCache.getFiInf("005000")).thenReturn(fiInf.get("005000"));

        Mapp09400101Req mapp09400101Req = new Mapp09400101Req();
        mapp09400101Req.setVersion("1.0");
        mapp09400101Req.setLanguage("zh-HK");
        RequestModel<Mapp09400101Req> request = RequestModelHelper.buildRequestModel(mapp09400101Req);
        ResponseModel<Mapp09400101Resp> actualResponseModel = parameterManageProvider.queryBankCardInfo(request);
        Assert.assertEquals("2.0", actualResponseModel.getMessageBody().getVersion());
        Assert.assertEquals("0", actualResponseModel.getMessageBody().getUptodate());
        Assert.assertEquals(BankCardInfoHelper.buildExpectedBindCardDetailBanksInfoZHHK(),
            actualResponseModel.getMessageBody().getBindCardDetailBanksInfo());
    }

    @Test
    public void validateMobileNumber1() {
        //正常 BT00
        when(paramCache.getComplexMultiList(anyString())).thenReturn(BusinessParamHelper.buildCountryAndAreaList());
        Mapp09600101Req mapp09600101Req = new Mapp09600101Req();
        mapp09600101Req.setMobileNumber("+853-60001234");
        mapp09600101Req.setBizType("BT00");
        RequestModel<Mapp09600101Req> request = RequestModelHelper.buildRequestModel(mapp09600101Req);
        ResponseModel actualResponseModel = parameterManageProvider.validateMobileNumber(request);
        Assert.assertNotNull(actualResponseModel);
    }

    @Test
    public void validateMobileNumber2() {
        //正常 BT01
        when(paramCache.getComplexMultiList(anyString())).thenReturn(BusinessParamHelper.buildCountryAndAreaList());
        Mapp09600101Req mapp09600101Req = new Mapp09600101Req();
        mapp09600101Req.setMobileNumber("+86-13800001235");
        mapp09600101Req.setBizType("BT01");
        RequestModel<Mapp09600101Req> request = RequestModelHelper.buildRequestModel(mapp09600101Req);
        ResponseModel actualResponseModel = parameterManageProvider.validateMobileNumber(request);
        Assert.assertNotNull(actualResponseModel);
    }

    @Test
    public void validateMobileNumber3() {
        when(paramCache.getComplexMultiList(anyString())).thenReturn(BusinessParamHelper.buildCountryAndAreaList());
        Mapp09600101Req mapp09600101Req = new Mapp09600101Req();
        mapp09600101Req.setMobileNumber("+853-10001234");
        mapp09600101Req.setBizType("BT00");
        RequestModel<Mapp09600101Req> request = RequestModelHelper.buildRequestModel(mapp09600101Req);
        Assert.assertThrows("手机号码格式不正确", UserQueryException.class,
            ()-> parameterManageProvider.validateMobileNumber(request));
    }

    @Test
    public void validateMobileNumber4() {
        //异常 BTXX
        Mapp09600101Req mapp09600101Req = new Mapp09600101Req();
        mapp09600101Req.setMobileNumber("+853-60001234");
        mapp09600101Req.setBizType("BTXX");
        RequestModel<Mapp09600101Req> request = RequestModelHelper.buildRequestModel(mapp09600101Req);
        Assert.assertThrows("手机号码格式不正确", UserQueryException.class,
            ()-> parameterManageProvider.validateMobileNumber(request));
        verify(paramCache, times(0)).getComplexMultiList(anyString());
    }

    @Test
    public void getBindCardDetailBanksInfo_ENUS()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<FiTechParamAttachMasterDTO> fiTechParamAttachMasterDTOList = BankCardInfoHelper.buildBankCardInfo();
        when(walletCache.listBankCardInfos()).thenReturn(fiTechParamAttachMasterDTOList);
        Map<String, OrgDTO> fiInf = BankCardInfoHelper.buildFiInf();
        when(orgCache.getFiInf("002000")).thenReturn(fiInf.get("002000"));
        when(orgCache.getFiInf("003000")).thenReturn(fiInf.get("003000"));
        when(orgCache.getFiInf("005000")).thenReturn(fiInf.get("005000"));

        Method method = ParameterManageProviderImpl.class.getDeclaredMethod("getBindCardDetailBanksInfo", String.class);
        method.setAccessible(true);
        JSONObject actualResp = (JSONObject) method.invoke(parameterManageProvider, "en-US");
        Assert.assertEquals(BankCardInfoHelper.buildExceptedBindCardDetailBanksInfoENUS(), actualResp);
    }

    @Test
    public void getBindCardDetailBanksInfo_ZHCN()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<FiTechParamAttachMasterDTO> fiTechParamAttachMasterDTOList = BankCardInfoHelper.buildBankCardInfo();
        when(walletCache.listBankCardInfos()).thenReturn(fiTechParamAttachMasterDTOList);

        Method method = ParameterManageProviderImpl.class.getDeclaredMethod("getBindCardDetailBanksInfo", String.class);
        method.setAccessible(true);
        JSONObject actualResp = (JSONObject) method.invoke(parameterManageProvider, "zh-CN");
        Assert.assertEquals(BankCardInfoHelper.buildExceptedBindCardDetailBanksInfoZHCN(), actualResp);
    }

    @Test
    public void getBindCardDetailBanksInfo_UNKNOWN()
        throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<FiTechParamAttachMasterDTO> fiTechParamAttachMasterDTOList = BankCardInfoHelper.buildBankCardInfo();
        when(walletCache.listBankCardInfos()).thenReturn(fiTechParamAttachMasterDTOList);

        Method method = ParameterManageProviderImpl.class.getDeclaredMethod("getBindCardDetailBanksInfo", String.class);
        method.setAccessible(true);
        JSONObject actualResp = (JSONObject) method.invoke(parameterManageProvider, "unknown");
        Assert.assertEquals(BankCardInfoHelper.buildExceptedBindCardDetailBanksInfoZHCN(), actualResp);
    }


}