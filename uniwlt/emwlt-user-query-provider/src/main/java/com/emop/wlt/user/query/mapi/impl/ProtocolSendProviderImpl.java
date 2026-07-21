package com.emop.wlt.user.query.mapi.impl;

import static com.emop.wlt.common.constant.LanguageTypeConstant.ENUS;
import static com.emop.wlt.common.constant.LanguageTypeConstant.PTPT;
import static com.emop.wlt.common.constant.LanguageTypeConstant.ZHCN;
import static com.emop.wlt.common.enums.CaptchaTypeEnum.ST21;

import com.alibaba.fastjson.JSONObject;
import com.emop.infocache.WalletCache;
import com.emop.infocache.api.dto.ReleaseHistoryDTO;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.captcha.model.SendEmailDTO;
import com.emop.wlt.captcha.provider.EmailOperator;

import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.util.Base64Utils;
import com.emop.wlt.user.query.manager.GrayUserCheckManager;
import com.emop.wlt.user.query.mapi.ProtocolSendProvider;

import com.emop.wlt.user.query.model.request.Mapp20700101Req;
import com.emop.wlt.user.query.model.response.Mapp20700101Resp;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProtocolSendProviderImpl implements ProtocolSendProvider {

    @Autowired
    private EmailOperator emailOperator;
    @Autowired
    private WalletCache walletCache;
    @Autowired
    private GrayUserCheckManager grayUserCheckManager;

    private static final String PROTOCOL = "200";
    private static final String PROTOCOL_NO = "protocolNo";
    private static final String INNER_PDF_URL = "innerPdfUrl";
    private static final String TITLE = "title";
    private static final String SUFFIX_PDF = ".pdf";
    private static final String PROTOCOL_EMAIL_SUBJECT_ZHCN = "数字澳门元APP";
    private static final String PROTOCOL_EMAIL_SUBJECT_ZHHK = "數字澳門元APP";
    private static final String PROTOCOL_EMAIL_SUBJECT_ENUS = "";//todo
    private static final String PROTOCOL_EMAIL_SUBJECT_PTPT = "";//todo

    @Override
    @AppResponse
    public ResponseModel<Mapp20700101Resp> sendProtocolByEmail(RequestModel<Mapp20700101Req> request) {
        String mobileNumber = request.getMessageBody().getMobileNumber();
        String language = request.getMessageBody().getLanguage();
        String protocolNo = request.getMessageBody().getProtocolNo();

        //通过协议号获取协议标题和下载地址
        Pair<String, String> protocolPair = getProtocolPair(mobileNumber, language, protocolNo);
        if (StringUtils.isBlank(protocolPair.getLeft()) || StringUtils.isBlank(protocolPair.getRight())) {
            log.info("协议标题或下载地址为空,protocolPair:{}", protocolPair);
            return ResponseModel.<Mapp20700101Resp>builder().messageBody(new Mapp20700101Resp()).build();
        }
        log.info("协议标题和下载地址,protocolPair:{}", protocolPair);

        try (InputStream inputStream = new URL(protocolPair.getRight()).openStream();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            //邮件内容base64编码
            String encodeContent = Base64Utils.encode(outputStream.toByteArray());

            //发送邮件
            SendEmailDTO sendEmailDTO = SendEmailDTO.builder()
                .email(request.getMessageBody().getEmail())
                .svcTp(ST21.toString())
                .language(language)
                .attachmentName(protocolPair.getLeft().concat(SUFFIX_PDF))//附件名=协议名.pdf
                .attachmentContent(encodeContent)
                .subject(getSubjectNameByLanguage(language)).build();
            emailOperator.sendEmailWithAttachment(sendEmailDTO);
        } catch (Exception e) {
            log.info("获取协议PDF并发邮件异常", e);
        }

        return ResponseModel.<Mapp20700101Resp>builder().messageBody(new Mapp20700101Resp()).build();
    }

    private String getSubjectNameByLanguage(String language) {
        return switch (language) {
            case ZHCN -> PROTOCOL_EMAIL_SUBJECT_ZHCN;
            case ENUS -> PROTOCOL_EMAIL_SUBJECT_ENUS;
            case PTPT -> PROTOCOL_EMAIL_SUBJECT_PTPT;
            default -> PROTOCOL_EMAIL_SUBJECT_ZHHK;
        };
    }

    private Pair<String, String> getProtocolPair(String mobileNumber, String language, String protocolNo) {
        ReleaseHistoryDTO protocolResult = walletCache.getRelease(PROTOCOL);
        log.debug("信息缓存获取协议:{}", protocolResult);

        String content = grayUserCheckManager.isGray(mobileNumber) ?
            protocolResult.getGrayContent() : protocolResult.getCurrentContent();

        JSONObject jsonObject = getContentByLanguage(content, language);
        if (Objects.isNull(jsonObject)) {
            return Pair.of(null, null);
        }

        Optional<JSONObject> protocolJsonObject = jsonObject.keySet().stream()
            .map(item -> (JSONObject) jsonObject.get(item))
            .filter(protocol ->protocolNo.equals(protocol.getString(PROTOCOL_NO))).findAny();

        return protocolJsonObject.isPresent() ?
            Pair.of(protocolJsonObject.get().getString(TITLE),protocolJsonObject.get().getString(INNER_PDF_URL)) :
            Pair.of(null, null);
    }

    private JSONObject getContentByLanguage(String content, String language) {
        if (StringUtils.isNotBlank(content)) {
            return JSONObject.parseObject(content).getJSONObject(language);
        }
        return null;
    }
}
