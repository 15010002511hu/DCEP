package com.emop.wlt.user.query.mapi.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.doms.cloudcontrol.query.api.UserInfoProvider;
import com.emop.doms.cloudcontrol.query.dto.CloudControlUserInfoRespDTO;
import com.emop.infocache.HelpCache;
import com.emop.infocache.OrgCache;
import com.emop.infocache.ParamCache;
import com.emop.infocache.WalletCache;
import com.emop.infocache.api.dto.FiTechParamAttachMasterDTO;

import com.emop.infocache.api.dto.ReleaseHistoryDTO;
import com.emop.infocache.api.dto.help.HelpClassifyDetailDTO;
import com.emop.infocache.api.dto.help.HelpClassifyLangDTO;
import com.emop.infocache.api.dto.help.HelpDetailDTO;
import com.emop.infocache.api.dto.help.HelpDetailLangDTO;
import com.emop.infocache.api.dto.help.HelpQuestSortDTO;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.common.constant.CommonConstant;
import com.emop.wlt.common.constant.DatePattern;
import com.emop.wlt.common.enums.CardTypeEnum;
import com.emop.wlt.common.enums.LanguageTypeEnum;
import com.emop.wlt.common.model.dto.MobileNumber;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.util.DateUtils;
import com.emop.wlt.common.util.UserUtils;
import com.emop.wlt.user.query.exception.ExceptionCast;
import com.emop.wlt.user.query.manager.CountryAndRegionManager;
import com.emop.wlt.user.query.manager.GrayUserCheckManager;
import com.emop.wlt.user.query.manager.WalletLimitManager;
import com.emop.wlt.user.query.mapi.ParameterManageProvider;
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
import com.emop.wlt.user.query.model.vo.Element;
import com.emop.wlt.user.query.model.vo.GroupconfigDB;
import com.emop.wlt.user.query.model.vo.HelpClassify;
import com.emop.wlt.user.query.model.vo.Protocol;
import com.emop.wlt.user.query.model.vo.QuestionDetail;
import com.emop.wlt.user.query.model.vo.QuestionSummary;
import com.emop.wlt.user.query.model.vo.UserConfig;
import com.emop.wlt.user.query.util.ConfigConstructorUtils;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.Version;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.emop.wlt.user.query.constant.ControlConstant.DEFAULT_REFRESH_TIME;
import static com.emop.wlt.user.query.constant.ParameterConstant.*;

@Service
@Slf4j
public class ParameterManageProviderImpl implements ParameterManageProvider {

    @NacosValue(value = "${uniwltApp.cloudcontrol.commonConfig:commonConfig}", autoRefreshed = true)
    private String commonConfig;
    @NacosValue(value = "${uniwltApp.cloudcontrol.instConfigVersion:instConfigVersion}", autoRefreshed = true)
    private String instConfigVersion;
    @NacosValue(value = "${uniwltApp.hardwareWallet.helpClassifyNo:CC0005202410310002}", autoRefreshed = true)
    private String hardwareWalletHelpClassifyNo;
    @NacosValue(value = "${uniwltApp.noLoginFrequently.helpClassifyNo:CC0005202410310001}", autoRefreshed = true)
    private String noLoginFrequentlyHelpClassifyNo;

    @Autowired
    private ParamCache paramCache;
    @Autowired
    private WalletCache walletCache;
    @Autowired
    private OrgCache orgCache;
    @Autowired
    private HelpCache helpCache;
    @Autowired
    private ConfigConstructorUtils configConstructorUtils;
    @Autowired
    private CountryAndRegionManager countryAndRegionManager;
    @Autowired
    private WalletLimitManager walletLimitManager;
    @Autowired
    private GrayUserCheckManager grayUserCheckManager;
    @DubboReference
    private UserInfoProvider userInfoProvider;

    private static final String ELEMENT = "100";
    private static final String PROTOCOL = "200";
    private static final String APP_PRIVACY = "appPrivacy";
    private static final String UPDATE_TRUE = "0";
    private static final String UPDATE_FALSE = "1";
    private static final String DEFAULT_PHONE = "12222222222";
    private static final String DEFAULT_FIRST_NAME = "A";
    private static final String NAME_INDEX = "A";
    private static final String GRAY = "1";
    private static final String OFFICIAL = "0";
    private static final String MOBILE_NUMBER_LENGTH = "mobileNumberLength";
    private static final String APP_USER_TRANSFER_COUNTRY_CODE_LIST = "1001AppTransferCountryCodeList";
    private static final String APP_USER_REGISTER_COUNTRY_CODE_LIST = "1001AppUserRegisterCountryCodeList";
    private static final String BIZ_TYPE_REGISTER = "BT00";
    private static final String BIZ_TYPE_TRANSFER = "BT01";


    @Override
    @AppResponse
    public ResponseModel<Mapp20200101Resp> queryHelpClassifyList(RequestModel<Mapp20200101Req> request) {
        Mapp20200101Req mapp20200101Req = request.getMessageBody();
        String language = mapp20200101Req.getLanguage();
        String appVersion = mapp20200101Req.getAppVersion();
        String platform = mapp20200101Req.getPlatform();
        String mobileNumber = mapp20200101Req.getMobileNumber();
        String grayFlag = grayUserCheckManager.isGray(mobileNumber) ? GRAY : OFFICIAL;
        log.info("用户:{}灰度标识:{}", mobileNumber, grayFlag);

        List<HelpClassify> helpClassifyList = new ArrayList<>();

        //获取帮助分类列表
        List<HelpClassifyDetailDTO> helpClassifyDetailDTOList = helpCache.listClassifyNo(grayFlag);
        log.info("帮助分类列表={}", helpClassifyDetailDTOList);

        if (Objects.nonNull(helpClassifyDetailDTOList)) {
            //过滤帮助分类类型=通用
            helpClassifyDetailDTOList = helpClassifyDetailDTOList.stream()
                .filter(item->HELP_CLASSIFY_TYPE_GENERAL.equals(item.getClassifyType())).collect(Collectors.toList());
            log.info("一级通用分类={}", helpClassifyDetailDTOList);

            for (HelpClassifyDetailDTO helpClassifyDetailDTO : helpClassifyDetailDTOList) {
                Optional<HelpClassifyLangDTO> helpClassifyLangDTO = helpClassifyDetailDTO.getLangs().stream()
                    .filter(item -> language.equals(item.getLanguageType())).findFirst();

                if (helpClassifyLangDTO.isPresent()) {
                    HelpClassify helpClassify = new HelpClassify();
                    helpClassify.setClassifyKeyword(helpClassifyLangDTO.get().getKeyword());
                    helpClassify.setClassifyName(helpClassifyLangDTO.get().getClassifyName());
                    helpClassify.setClassifyIcon(helpClassifyDetailDTO.getIcon());
                    String classifySortId = helpCache.getClassifySort(grayFlag, helpClassifyDetailDTO.getClassifyNo())
                        .getSortId().toString();
                    helpClassify.setClassifySortId(classifySortId);
                    //获取问题汇总列表
                    List<QuestionSummary> questionSummaryList =
                        getQuestionSummaryList(grayFlag, platform, appVersion, language, helpClassifyDetailDTO.getQuestions());
                    log.info("问题汇总列表={}", questionSummaryList);
                    helpClassify.setQuestionSummaryList(questionSummaryList);
                    helpClassifyList.add(helpClassify);
                }
            }
        } else {
            log.info("帮助分类列表为null");
            return ResponseModel.<Mapp20200101Resp>builder().messageBody(Mapp20200101Resp.builder().build()).build();
        }

        //对帮助分类进行排序
        helpClassifyList = helpClassifyList.stream()
            .sorted(Comparator.comparing(HelpClassify::getClassifySortId)).collect(Collectors.toList());

        Mapp20200101Resp mapp20200101Resp = Mapp20200101Resp.builder().helpClassifyList(helpClassifyList).build();
        return ResponseModel.<Mapp20200101Resp>builder().messageBody(mapp20200101Resp).build();
    }

    @Override
    @AppResponse
    public ResponseModel<Mapp20300101Resp> queryQuestionDetail(RequestModel<Mapp20300101Req> request) {
        Mapp20300101Req mapp20300101Req = request.getMessageBody();
        String mobileNumber = mapp20300101Req.getMobileNumber();
        String questionType = mapp20300101Req.getQuestionType();
        String appVersion = mapp20300101Req.getAppVersion();
        String language = mapp20300101Req.getLanguage();
        String platform = mapp20300101Req.getPlatform();

        String grayFlag = grayUserCheckManager.isGray(mobileNumber) ? GRAY : OFFICIAL;
        log.info("用户:{}灰度标识:{}", mobileNumber, grayFlag);

        List<QuestionDetail> questionDetailList = new ArrayList<>();
        List<HelpQuestSortDTO> questionList = switch (questionType) {
            case GENERAL_QUESTION_TYPE ->
                addQuestionList(mapp20300101Req.getQuestionNo());
            case HARDWARE_WALLET_QUESTION_TYPE ->
                Optional.ofNullable(helpCache.getClassifyDetail(grayFlag, hardwareWalletHelpClassifyNo))
                    .map(i->i.getQuestions()).orElse(null);
            case NO_LOGIN_FREQUENTLY_QUESTION_TYPE ->
                Optional.ofNullable(helpCache.getClassifyDetail(grayFlag, noLoginFrequentlyHelpClassifyNo))
                    .map(i->i.getQuestions()).orElse(null);
            default -> null;
        };

        if (CollectionUtils.isEmpty(questionList)) {
            return ResponseModel.<Mapp20300101Resp>builder()
                .messageBody(Mapp20300101Resp.builder().questionDetailList(questionDetailList).build()).build();
        }

        //对帮助问题进行排序
        questionList = questionList.stream()
            .sorted(Comparator.comparing(HelpQuestSortDTO::getSortId)).collect(Collectors.toList());
        for (HelpQuestSortDTO question : questionList) {
            HelpDetailDTO helpDetailDTO = helpCache.getDetail(grayFlag, question.getQuestionNo());
            log.info("获取的问题详情{}", helpDetailDTO);

            if (Objects.nonNull(helpDetailDTO)) {
                Optional<HelpDetailLangDTO> helpDetailLangDTOList =
                    filterHelpDetailLangDTOList(helpDetailDTO, platform, appVersion, language);

                if (helpDetailLangDTOList.isPresent()) {
                    QuestionDetail questionDetail = new QuestionDetail();
                    questionDetail.setQuestionNo(helpDetailDTO.getQuestionNo());
                    questionDetail.setQuestion(helpDetailLangDTOList.get().getQuestion());
                    questionDetail.setAnswer(helpDetailLangDTOList.get().getAnswer());
                    questionDetailList.add(questionDetail);
                }
            }
        }

        return ResponseModel.<Mapp20300101Resp>builder()
            .messageBody(Mapp20300101Resp.builder().questionDetailList(questionDetailList).build()).build();
    }

    @Override
    @AppResponse
    public ResponseModel<Mapp09000101Resp> queryBusinessParam(RequestModel<Mapp09000101Req> request) {
        Mapp09000101Req mapp09000101Req = request.getMessageBody();
        String sceneType = mapp09000101Req.getSceneType();
        String content;

        content = switch (sceneType) {
            case COUNTRY_AND_REGION_CODE_TYPE ->
                countryAndRegionManager.queryCountryAndRegionCode(mapp09000101Req.getLanguage(),
                    mapp09000101Req.getBizType());
            case HARDWARE_WALLET_INST_LIMIT_TYPE ->
                JSONObject.toJSONString(paramCache.getHardWalletFreePayConfig());
            case WALLET_INST_LIMIT_TYPE ->
                walletLimitManager.queryWalletLimit(mapp09000101Req.getInstNo(), mapp09000101Req.getWalletLevel());
            default -> null;
        };

        Mapp09000101Resp mapp09000101Resp = Mapp09000101Resp.builder()
            .content(content)
            .currentDateTime(LocalDateTime.now().format(DatePattern.NORM_DATETIME_FORMAT)).build();
        return ResponseModel.<Mapp09000101Resp>builder().messageBody(mapp09000101Resp).build();
    }

    @Override
    @AppResponse
    public ResponseModel<Mapp09100101Resp> queryLanguageAndProtocol(RequestModel<Mapp09100101Req> request) {
        Mapp09100101Req mapp09100101Req = request.getMessageBody();
        String mobileNumber = mapp09100101Req.getMobileNumber();
        String language = mapp09100101Req.getLanguage();
        String elementReleaseId = mapp09100101Req.getElementReleaseId();
        String protocolReleaseId = mapp09100101Req.getProtocolReleaseId();
        String appPrivacyProtocolNo = mapp09100101Req.getAppPrivacyProtocolNo();
        ReleaseHistoryDTO elementResult = walletCache.getRelease(ELEMENT);
        ReleaseHistoryDTO protocolResult = walletCache.getRelease(PROTOCOL);
        String newElementReleaseId = null;
        String newProtocolReleaseId = null;
        String elementTime = null;
        String elementContentResult = null;
        String protocolContentResult = null;
        String protocolTime = null;
        boolean flag = grayUserCheckManager.isGray(mobileNumber);
        //根据灰度决定取公共或者灰度数据
        if (flag) {
            if (elementResult != null) {
                newElementReleaseId = elementResult.getGrayReleaseSnapshotId();
                elementContentResult = elementResult.getGrayContent();
                elementTime = DateUtils.dateToDateTime(elementResult.getGrayTime());
            }
            if (protocolResult != null) {
                newProtocolReleaseId = protocolResult.getGrayReleaseSnapshotId();
                protocolContentResult = protocolResult.getGrayContent();
                protocolTime = DateUtils.dateToDateTime(protocolResult.getGrayTime());
            }
        } else {
            if (elementResult != null) {
                newElementReleaseId = elementResult.getCurrentReleaseSnapshotId();
                elementContentResult = elementResult.getCurrentContent();
                elementTime = DateUtils.dateToDateTime(elementResult.getCurTime());
            }
            if (protocolResult != null) {
                newProtocolReleaseId = protocolResult.getCurrentReleaseSnapshotId();
                protocolContentResult = protocolResult.getCurrentContent();
                protocolTime = DateUtils.dateToDateTime(protocolResult.getCurTime());
            }
        }
        //获取数据
        JSONObject elementContent = getContent(elementReleaseId, newElementReleaseId, elementContentResult, language);
        JSONObject protocolContent = getContent(protocolReleaseId, newProtocolReleaseId, protocolContentResult, language);

        //过滤多余数据
        JSONObject protocolMap = new JSONObject();
        if (protocolContent != null) {
            for (String key : protocolContent.keySet()) {
                if (!key.startsWith(APP_PRIVACY)) {
                    protocolMap.put(key, protocolContent.get(key));
                }
            }
            setAppPrivacyProtocol(protocolContent, protocolMap, appPrivacyProtocolNo);
        }

        Element element = Element.builder()
            .releaseId(newElementReleaseId).content(elementContent).updateTime(elementTime).build();
        Protocol protocol = Protocol.builder()
            .releaseId(newProtocolReleaseId).content(protocolMap).updateTime(protocolTime).build();

        Mapp09100101Resp mapp09100101Resp = Mapp09100101Resp.builder()
            .language(language)
            .appVersion(mapp09100101Req.getAppVersion())
            .element(element)
            .protocol(protocol).build();

        return ResponseModel.<Mapp09100101Resp>builder().messageBody(mapp09100101Resp).build();
    }

    @Override
    @AppResponse
    public ResponseModel<Mapp09200101Resp> queryCommonParam(RequestModel<Mapp09200101Req> request) {
        String platform = request.getMessageBody().getPlatform();
        JSONObject commonMap = new JSONObject();
        Map<String, String> commonCache = paramCache.getCommonMap();
        if (commonCache != null && !commonCache.isEmpty()) {
            Iterator<Entry<String, String>> iterator = commonCache.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                if (Objects.equals(PLATFORM_ANDROID, platform)) {
                    if (Objects.equals(TRACK_EVENTS_IOS, next.getKey())) {
                        //客户端为android，过滤trackEventsAndroid
                        continue;
                    }
                }
                if (Objects.equals(PLATFORM_IOS, platform)) {
                    if (Objects.equals(TRACK_EVENTS_ANDROID, next.getKey())) {
                        //客户端为iOS，过滤trackEventsIOS
                        continue;
                    }
                }
                commonMap.put(next.getKey(), next.getValue());
            }
        }

        commonMap.put(MOBILE_NUMBER_LENGTH, countryAndRegionManager.queryMobileNumberLengthRule());

        Mapp09200101Resp mapp09200101Resp = Mapp09200101Resp.builder()
            .common(Common.builder().content(commonMap).build()).build();
        return ResponseModel.<Mapp09200101Resp>builder().messageBody(mapp09200101Resp).build();
    }

    @Override
    @AppResponse
    public ResponseModel<Mapp09300101Resp> queryCloudControl(RequestModel<Mapp09300101Req> request) {
        // 根据手机号查公管获取该手机号对应的配置
        List<CloudControlUserInfoRespDTO> cloudControlUserInfoRespDTOList = userInfoProvider.getFunctionConfigList(
            UserUtils.getUserPhone());
        log.info("调用信息缓存查询的云控配置,配置信息{}", cloudControlUserInfoRespDTOList);

        // 从信息缓存查对应配置List
        List<GroupconfigDB> groupconfigDBList = new ArrayList<>(cloudControlUserInfoRespDTOList.size());

        for (CloudControlUserInfoRespDTO cloudControlDTO : cloudControlUserInfoRespDTOList) {
            GroupconfigDB groupconfigDB = GroupconfigDB.builder()
                .groupId(cloudControlDTO.getGroupCode())
                .moduleKey(cloudControlDTO.getModuleProtocolKey())
                .functionKey(cloudControlDTO.getFunctionProtocolKey())
                .config(cloudControlDTO.getConfig())
                .version(cloudControlDTO.getVersion())
                .offlineWallet(cloudControlDTO.getDeviceTypes())
                .offlineWalletSwitch(cloudControlDTO.getDeviceTypeSwitch())
                .updateTime(cloudControlDTO.getUpdateTime() == null ? LocalDateTime.now() :
                    cloudControlDTO.getUpdateTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                .build();
            groupconfigDBList.add(groupconfigDB);
        }

        //done 构造用户配置
        UserConfig userConfig = configConstructorUtils.configConstructorNew(groupconfigDBList,
            request.getMessageBody().getDeviceModel());

        Mapp09300101Resp mapp09300101Resp = Mapp09300101Resp.builder()
            .config(userConfig.getConfig())
            .userConfigVersion(userConfig.getVersion())
            .userId(request.getMessageHeader().getUserId())
            .refreshTime(DEFAULT_REFRESH_TIME).build();
        return ResponseModel.<Mapp09300101Resp>builder().messageBody(mapp09300101Resp).build();
    }

    @Override
    @AppResponse
    public ResponseModel<Mapp09400101Resp> queryBankCardInfo(RequestModel<Mapp09400101Req> request) {
        String languageTag = request.getMessageBody().getLanguage();
        String currentVersion = request.getMessageBody().getVersion();
        String phone = UserUtils.getUserPhone();
        String newVersion = getVersion(phone);

        Mapp09400101Resp mapp09400101Resp;
        if (StringUtils.isNotBlank(currentVersion) && currentVersion.equals(newVersion)) {
            mapp09400101Resp = Mapp09400101Resp.builder().uptodate(UPDATE_FALSE).build();
            return ResponseModel.<Mapp09400101Resp>builder().messageBody(mapp09400101Resp).build();
        }

        JSONObject bindCardDetailBanksInfo = getBindCardDetailBanksInfo(languageTag);
        mapp09400101Resp = Mapp09400101Resp.builder()
            .uptodate(UPDATE_TRUE)
            .version(newVersion)
            .bindCardDetailBanksInfo(bindCardDetailBanksInfo).build();
        return ResponseModel.<Mapp09400101Resp>builder().messageBody(mapp09400101Resp).build();
    }

    @Override
    @AppResponse
    public ResponseModel validateMobileNumber(RequestModel<Mapp09600101Req> request) {
        MobileNumber mobileNumberObject = MobileNumber.buildFrom(request.getMessageBody().getMobileNumber());
        String configKey = switch (request.getMessageBody().getBizType()) {
            case BIZ_TYPE_TRANSFER -> APP_USER_TRANSFER_COUNTRY_CODE_LIST;
            case BIZ_TYPE_REGISTER -> APP_USER_REGISTER_COUNTRY_CODE_LIST;
            default -> null;
        };
        if (StringUtils.isBlank(configKey)) {
            ExceptionCast.cast(BaseErrorEnum.B06701);
        }

        List<Map<String, String>> configList = paramCache.getComplexMultiList(configKey);
        log.info("configList:{}", configList);

        if (!mobileNumberObject.validatePhoneNumber(configList)) {
            ExceptionCast.cast(BaseErrorEnum.B06701);
        }
        return ResponseModel.builder().messageBody(new Object()).build();
    }

    /**
     * 获取机构信息升级版
     */
    private JSONObject getBindCardDetailBanksInfo(String languageTag) {
        List<FiTechParamAttachMasterDTO> fiTechParamAttachMasterDTOs = JSONArray.parseArray(
            JSONObject.toJSONString(walletCache.listBankCardInfos()),
            FiTechParamAttachMasterDTO.class);
        HashMap<String, HashMap<String, String>> fiTechParamAttachMasterDTO = new HashMap<String, HashMap<String, String>>();
        for (FiTechParamAttachMasterDTO fiTechParamAttachMaster : fiTechParamAttachMasterDTOs) {
            if (StringUtils.isNotBlank(fiTechParamAttachMaster.getOrgCode())) {
                if (StringUtils.isBlank(fiTechParamAttachMaster.getBankFirstLetter())) {
                    fiTechParamAttachMaster.setBankFirstLetter(DEFAULT_FIRST_NAME);
                }
                HashMap<String, String> info = new HashMap<>();
                if (LanguageTypeEnum.ZHCN.getValue().equals(languageTag)) {
                    info.put("ShortName", fiTechParamAttachMaster.getBankName());
                    info.put("NameIndex", fiTechParamAttachMaster.getBankFirstLetter().substring(0, 1));
                } else if (LanguageTypeEnum.ZHHK.getValue().equals(languageTag)) {
                    info.put("ShortName",
                        orgCache.getFiInf(fiTechParamAttachMaster.getOrgCode()).getComplexShortName());
                    info.put("NameIndex", fiTechParamAttachMaster.getBankFirstLetter().substring(0, 1));
                } else if (LanguageTypeEnum.ENUS.getValue().equals(languageTag)) {
                    info.put("ShortName",
                        orgCache.getFiInf(fiTechParamAttachMaster.getOrgCode()).getFiEnglishShortName());
                    if (StringUtils.isNotBlank(
                        orgCache.getFiInf(fiTechParamAttachMaster.getOrgCode()).getFiEnglishShortName())) {
                        info.put("NameIndex",
                            orgCache.getFiInf(fiTechParamAttachMaster.getOrgCode()).getFiEnglishShortName()
                                .substring(0, 1));
                    } else {
                        info.put("NameIndex", NAME_INDEX);
                    }
                } else {
                    info.put("ShortName", fiTechParamAttachMaster.getBankName());
                    info.put("NameIndex", fiTechParamAttachMaster.getBankFirstLetter().substring(0, 1));
                }
                info.put("BindCardAppID", fiTechParamAttachMaster.getPullAppId());
                info.put("ColorLogo", fiTechParamAttachMaster.getColorLogo());
                info.put("WhiteLogo", fiTechParamAttachMaster.getWhiteLogo());
                info.put("DarkLogo", fiTechParamAttachMaster.getDarkLogo());
                info.put("Color1", fiTechParamAttachMaster.getNewStartColor());
                info.put("Color2", fiTechParamAttachMaster.getNewEndColor());
                info.put("Color3", fiTechParamAttachMaster.getNewBackgroundColor());
                info.put("CardType", CardTypeEnum.getAppValueByCacheValue(fiTechParamAttachMaster.getCardTypes()));
                info.put("EnableWalletAutoTopup",
                    StringUtils.isBlank(fiTechParamAttachMaster.getWalletAutoTopup()) ? CommonConstant.NO
                        : fiTechParamAttachMaster.getWalletAutoTopup());
                info.put("EnableCardAutoTopup",
                    StringUtils.isBlank(fiTechParamAttachMaster.getCardAutoTopup()) ? CommonConstant.NO
                        : fiTechParamAttachMaster.getCardAutoTopup());
                fiTechParamAttachMasterDTO.put(fiTechParamAttachMaster.getOrgCode(), info);
            }

        }

        JSONObject bindCardDetailBanksInfo = new JSONObject();
        bindCardDetailBanksInfo.putAll(fiTechParamAttachMasterDTO);
        return bindCardDetailBanksInfo;
    }

    private String getVersion(String phone) {
        // 根据手机号查公管获取该手机号对应的配置
        String userCode = StringUtils.isBlank(phone) ? DEFAULT_PHONE : phone;
        List<CloudControlUserInfoRespDTO> cloudControlUserInfoRespDTOList =
            userInfoProvider.getFunctionConfigList(userCode);
        String version = "";
        for (CloudControlUserInfoRespDTO cloudControlUserInfoRespDTO : cloudControlUserInfoRespDTOList) {
            if (commonConfig.equals(cloudControlUserInfoRespDTO.getFunctionProtocolKey())) {
                String config = cloudControlUserInfoRespDTO.getConfig();
                JSONObject jsonObject = JSONObject.parseObject(config);
                version = (String) jsonObject.get(instConfigVersion);
                break;
            }
        }
        return version;
    }

    /**
     * 比对版本号获取最新数据
     *
     * @param releaseId
     * @param newReleaseId
     * @param content
     * @param language
     * @return
     */
    private JSONObject getContent(String releaseId, String newReleaseId, String content, String language) {
        if (!StringUtils.equals(releaseId, newReleaseId)) {
            if (!StringUtils.isBlank(content)) {
                return JSONObject.parseObject(content).getJSONObject(language);
            }
        }
        return null;
    }

    /**
     * 获取版本+系统的隐私协议
     *
     * @param protocol
     * @param protocolMap
     * @param appPrivacyProtocolNo
     */
    private void setAppPrivacyProtocol(JSONObject protocol, JSONObject protocolMap, String appPrivacyProtocolNo) {
        if (protocol.get(APP_PRIVACY + "_" + appPrivacyProtocolNo) != null) {
            protocolMap.put(APP_PRIVACY, protocol.get(APP_PRIVACY + "_" + appPrivacyProtocolNo));
        }
    }

    private List<QuestionSummary> getQuestionSummaryList(String grayFlag, String platform, String appVersion,
        String language, List<HelpQuestSortDTO> questionList) {

        List<QuestionSummary> questionSummaryList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(questionList)) {
            HelpDetailDTO helpDetailDTO;

            //对帮助问题进行排序
            questionList = questionList.stream()
                .sorted(Comparator.comparing(HelpQuestSortDTO::getSortId)).collect(Collectors.toList());

            for (HelpQuestSortDTO question : questionList) {
                helpDetailDTO = helpCache.getDetail(grayFlag, question.getQuestionNo());
                log.info("帮助信息={}", helpDetailDTO);

                if (Objects.nonNull(helpDetailDTO)) {
                    Optional<HelpDetailLangDTO> helpDetailLangDTOList =
                        filterHelpDetailLangDTOList(helpDetailDTO, platform, appVersion, language);

                    if (helpDetailLangDTOList.isPresent()) {
                        QuestionSummary questionSummary = new QuestionSummary();
                        questionSummary.setQuestionName(helpDetailLangDTOList.get().getQuestion());
                        questionSummary.setQuestionNo(helpDetailDTO.getQuestionNo());
                        questionSummaryList.add(questionSummary);
                    }
                }
            }
        }
        return questionSummaryList;
    }

    private List<HelpQuestSortDTO> addQuestionList(String questionNo) {
        HelpQuestSortDTO helpQuestSortDTO = new HelpQuestSortDTO();
        helpQuestSortDTO.setQuestionNo(questionNo);
        helpQuestSortDTO.setSortId(1);
        List<HelpQuestSortDTO> questionList = new ArrayList<>();
        questionList.add(helpQuestSortDTO);
        return questionList;
    }

    private Optional<HelpDetailLangDTO> filterHelpDetailLangDTOList(HelpDetailDTO helpDetailDTO,
        String platform, String appVersion, String language) {

        Optional<HelpDetailLangDTO> helpDetailLangDTOList;
        if (PLATFORM_ANDROID.equals(platform)) {
            helpDetailLangDTOList = helpDetailDTO.getVersions().stream()
                .filter(versionItem ->
                    (StringUtils.isBlank(versionItem.getAndroidMin())
                        || Version.compare(appVersion, versionItem.getAndroidMin()) >= 0)
                        && (StringUtils.isBlank(versionItem.getAndroidMax())
                        || Version.compare(appVersion, versionItem.getAndroidMax()) <= 0))
                .flatMap(versionItem ->
                    versionItem.getLangs().stream()
                        .filter(detailLangItem->language.equals(detailLangItem.getLanguageType())))
                .findFirst();
        } else {
            helpDetailLangDTOList = helpDetailDTO.getVersions().stream()
                .filter(versionItem ->
                    (StringUtils.isBlank(versionItem.getIosMin())
                        || Version.compare(appVersion, versionItem.getIosMin()) >= 0)
                        && (StringUtils.isBlank(versionItem.getIosMax())
                        || Version.compare(appVersion, versionItem.getIosMax()) <= 0))
                .flatMap(versionItem ->
                    versionItem.getLangs().stream()
                        .filter(detailLangItem->language.equals(detailLangItem.getLanguageType())))
                .findFirst();
        }

        return helpDetailLangDTOList;
    }
}
