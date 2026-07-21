package com.emop.wlt.user.query.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.dubbo.dds.DDS;
import com.dubbo.dds.rule.SingleRule;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.gateway.dto.emap.EmapDTO;
import com.emop.gateway.dto.emap.RequestModel;
import com.emop.infocache.WalletCache;
import com.emop.wlt.common.constant.CommonConstant;
import com.emop.wlt.common.enums.LanguageTypeEnum;
import com.emop.wlt.common.util.TemplateUtils;
import com.emop.wlt.message.MessageConfigHolder;
import com.emop.wlt.message.PushIdConfigProperties;
import com.emop.wlt.message.push.api.AppMessageProvider;
import com.emop.wlt.message.push.dto.AppMessageDTO;
import com.emop.wlt.model.emap.emap600.Emap60000101ReqDTO;
import com.emop.wlt.user.entity.WalletUserIndex;
import com.emop.wlt.user.management.api.SystemMessageManageProvider;
import com.emop.wlt.user.pojo.SystemMessage;
import com.emop.wlt.user.query.constant.DatabaseConstant;
import com.emop.wlt.user.query.exception.ExceptionCast;
import com.emop.wlt.user.service.impl.WalletUserIndexDBService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.emop.wlt.common.constant.DatePattern.PURE_DATETIME_FORMAT;
import static com.emop.wlt.user.query.constant.PushConstant.*;

/**
 * @author bobo
 * @Description:
 * @date 2021/6/28 下午1:58
 */
@Service
@Slf4j
public class MessageService {

    @DubboReference
    private AppMessageProvider appMessageProvider;

    @DubboReference
    private SystemMessageManageProvider systemMessageManageProvider;

    private final WalletUserIndexDBService walletUserIndexDBService;

    public MessageService(WalletUserIndexDBService walletUserIndexDBService) {
        this.walletUserIndexDBService = walletUserIndexDBService;
    }

    /**
     * 客户端消息推送
     */
    @DDS(value = DatabaseConstant.USER_QUERY_DATABASE, rule = SingleRule.class)
    public void sendMessage(RequestModel<EmapDTO> request) {
        Emap60000101ReqDTO emap60000101ReqDTO = (Emap60000101ReqDTO) request.getMessageBody();
        String instNo = request.getMessageHeader().getFrom().getFiId().getFinInstnId().getClrSysMmbId().getMmbId();
        String msgId = request.getMessageHeader().getBizMsgIdr();
        this.sendMessage(emap60000101ReqDTO, instNo, msgId);
    }

    private void sendMessage(Emap60000101ReqDTO emap60000101ReqDTO, String instNo, String msgId) {
        String pushId = emap60000101ReqDTO.getPushSceneId();
        String walletId = emap60000101ReqDTO.getWalletId();

        Map<String, Object> templateDataMap = null;
        try {
            templateDataMap = JSON.parseObject(emap60000101ReqDTO.getMessageContent(),
                new TypeReference<Map<String, Object>>() {
                });
        } catch (Exception e) {
            // JSON转换异常
            ExceptionCast.cast(BaseErrorEnum.S09999, e);
        }

        //批量系统通知落库 改为pushId判断那些消息需要落库
        if (MessageConfigHolder.getMessageConfigProperties().getSystemBatchMessagePushIdArray().contains(pushId)) {
            PushIdConfigProperties pushIdConfigProperties = MessageConfigHolder.getPushIdConfigProperties(pushId);
            if (Objects.isNull(pushIdConfigProperties)) {
                ExceptionCast.buzCast(BaseErrorEnum.S09001, "未找到pushId对应的配置！");
            }
            this.saveBatchSystemMessageNew(templateDataMap, pushIdConfigProperties, instNo, pushId);
            return;
        }

        // 获取userId
        WalletUserIndex walletUserIndex = walletUserIndexDBService.selectById(walletId);
        Optional<String> userIdOptional = Optional.ofNullable(walletUserIndex).map(WalletUserIndex::getUserId);

        if (!userIdOptional.isPresent()) {
            log.warn("walletId not bind and return");
            return;
        }

        AppMessageDTO appMessageDTO = new AppMessageDTO();
        appMessageDTO.setPushId(pushId);
        appMessageDTO.setUserId(userIdOptional.get());
        appMessageDTO.setTemplateData(templateDataMap);
        appMessageDTO.setWalletId(walletId);
        appMessageDTO.setInst(instNo);
        appMessageDTO.setMsgId(msgId);

        appMessageProvider.sendAppMessage(appMessageDTO);
    }

    /**
     * 将批量系统消息落库
     *
     * @param templateDataMap
     * @param properties
     * @param inst
     * @param pushId
     */
    private void saveBatchSystemMessageNew(Map<String, Object> templateDataMap, PushIdConfigProperties properties, String inst,
        String pushId) {
        //Map<String, Object> sync = tmpCnt.getSync();
        // 非toast改为卡片模板
        String templateCode = properties.getCardTemplateCode();
        //除了停服其他都需要按照模板构造多语言
        if (StringUtils.isNotBlank(templateCode) && !templateCode.equals(CommonConstant.DEFAULT_TEMPLATECODE)) {
            if (templateDataMap.containsKey(CommonConstant.CUSTOMER_SERVICE_PHONE)) {
                String telephone = CommonConstant.TELEPHONE;
                try {
                    telephone = JSONObject.parseObject(JSONObject.parseObject(
                                WalletCache.getInstance().getRelease(CommonConstant.ELEMENT).getCurrentContent())
                            .getString(LanguageTypeEnum.ZHCN.getValue()))
                        .getString(CommonConstant.CUSTOMER_SERVICE_TELEPHONE_POPUPS_PHONE_NUMBER);
                } catch (Exception e) {
                    log.error("查询公管客服电话报错", e);
                }
                templateDataMap.put(CommonConstant.CUSTOMER_SERVICE_PHONE, telephone);
            }

            Map<String, String> templateKeyValue = templateDataMap.entrySet().stream()//update
                .filter(entry -> entry.getValue() instanceof String)
                .collect(Collectors.toMap(Entry::getKey, entry -> (String) entry.getValue()));
            Iterator<Entry<String, TemplateUtils.MpsTemplate>> iterator = TemplateUtils.TEMPLATE_MAP.entrySet()
                .iterator();
            HashMap<String, String> title = new HashMap<>();
            HashMap<String, String> content = new HashMap<>();
            while (iterator.hasNext()) {
                Entry<String, TemplateUtils.MpsTemplate> next = iterator.next();
                String nextKey = next.getKey();
                if (nextKey.startsWith(templateCode)) {
                    TemplateUtils.MpsTemplate mpsTemplate = TemplateUtils.TEMPLATE_MAP.get(nextKey);
                    String languageTag = nextKey.substring(templateCode.length() + 1);
                    TemplateUtils.checkTemplateParams(mpsTemplate.getParams(), templateKeyValue);
                    Map<String, String> tempTimes = TemplateUtils.dateFormatByTemplate(
                        mpsTemplate.getDateFormatParams(), templateKeyValue, languageTag);
                    Map<String, String> tempTexts = TemplateUtils.textLanguageFormat(mpsTemplate.getTextParams(),
                        templateKeyValue, languageTag);
                    title.put(languageTag, TemplateUtils.replacePlaceHolder(mpsTemplate.getTitle(), templateKeyValue));//update
                    content.put(languageTag, TemplateUtils.replacePlaceHolder(mpsTemplate.getContent(), templateKeyValue));//update
                    if (MapUtils.isNotEmpty(tempTimes)) {
                        templateKeyValue.putAll(tempTimes);
                    }
                    if (MapUtils.isNotEmpty(tempTexts)) {
                        templateKeyValue.putAll(tempTexts);
                    }
                }
            }
            if (title.isEmpty() || content.isEmpty()) {
                ExceptionCast.buzCast(BaseErrorEnum.S09001, "未找到对应的推送模板!" + templateCode);
            }
            templateDataMap.put(TITLE, title);
            templateDataMap.put(CONTENT, content);
        }

        templateDataMap.put(PUSH_SCENE_ID, pushId);
        templateDataMap.put(PUSH_TYPE, properties.getMessageType());

        if (StringUtils.isNotBlank(properties.getPreviewContent())) {
            templateDataMap.put(PREVIEW_CONTENT, properties.getPreviewContent());
        }
        if (StringUtils.isNotBlank(properties.getPreviewTitle())) {
            templateDataMap.put(PREVIEW_TITLE, properties.getPreviewTitle());
        }

        LocalDateTime validTime = null;
        try {
            validTime = StringUtils.isNotBlank((String) templateDataMap.get(VALID_TIME_KEY))
                ? LocalDateTime.parse((String)templateDataMap.get(VALID_TIME_KEY), PURE_DATETIME_FORMAT)
                : LocalDateTime.now();
        } catch (DateTimeParseException e) {
            // 时间转换异常
            ExceptionCast.cast(BaseErrorEnum.S09999, e);
        }
        SystemMessage systemMessage = new SystemMessage();
        systemMessage.setMessageId(UUID.randomUUID().toString().replace("-", ""));
        systemMessage.setMsgsn(MDC.get(com.emop.common.constants.CommonConstant.MSN_KEY));
        systemMessage.setContent(JSONObject.toJSONString(templateDataMap));
        systemMessage.setValidTime(validTime);
        systemMessage.setInst(inst);
        systemMessageManageProvider.save(systemMessage);

    }

}
