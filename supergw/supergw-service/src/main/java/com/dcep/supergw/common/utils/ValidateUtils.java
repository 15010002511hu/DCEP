/**
 * DCEP.com.cn Inc. Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.supergw.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.HibernateValidator;
import org.slf4j.MDC;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.NullValueInNestedPathException;
import com.dcep.common.Constants.CommonConstant;
import com.dcep.common.annotation.CheckAccountTag;
import com.dcep.common.annotation.CheckAccountTags;
import com.dcep.common.annotation.CheckBizCode;
import com.dcep.common.exception.DcepException;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.AccountUtil;
import com.dcep.common.validator.ValidSequence;
import com.dcep.infocache.BizRelationCache;
import com.dcep.infocache.api.dto.DateDTO;
import com.dcep.infocache.manager.NacosConsume;
import com.dcep.supergw.common.config.NacosConfigClient;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.enums.TradeParamWarningType;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.sign.client.SignClient;
import com.dcepex.trace.support.TraceContext;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author huyajun
 * @version $Id: ValidateUtils.java, v 0.1 2019年7月23日 下午9:34:41 huyajun Exp $
 */
@Slf4j
public class ValidateUtils {

    /**
     * DTO对象参数校验
     */
    private static Validator validator = Validation.byProvider(HibernateValidator.class).configure()
            .failFast(true).buildValidatorFactory().getValidator();

    /**
     * 报文头校验， 机构合法性。 以及 验签。
     *
     * @param header
     * @param signStr
     */
    public static void validateMsg(SoapHeader header, String content, String signStr) {
        try {
            validateMsg(header, content.getBytes(Constant.CHARTSET), signStr);
        } catch (UnsupportedEncodingException e) {
            throw new GwException(GwErrorEnum.VIRIFY_SIGN_ERROR, "UnsupportedEncodingException", e);
        }
    }

    /**
     * 报文头校验， 机构合法性。 以及 验签。
     *
     * @param header
     * @param signStr
     */
    public static void validateMsg(SoapHeader header, byte[] content, String signStr) {
        // 校验SoapHeader
        validate(header);

        // 校验报文环境位
        validateEnvFlag(header);

        // 校验发送机构
        if (!InfoCacheUtils.checkInst(header.getSender())) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "发送机构(" + header.getSender() + ")非法");
        }

        // 校验发送机构LEI码
        if (!InfoCacheUtils.checkInstLEI(header.getSender(), header.getSenderLEI())) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "发送机构LEI码(" + header.getSenderLEI() + ")非法");
        }

        // 校验接收机构
        if (!InfoCacheUtils.checkInst(header.getReceiver())) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "接收机构(" + header.getReceiver() + ")非法");
        }

        // 校验接收机构LEI码
        if (!InfoCacheUtils.checkInstLEI(header.getReceiver(), header.getReceiverLEI())) {
            throw new GwException(GwErrorEnum.REQUEST_PARAM_ILLEGAL, "接收机构LEI码(" + header.getReceiverLEI() + ")非法");
        }

        // 验签
        verifySign(signStr, content, header.getSender() + Constant.CERT_SEPARATOR + header.getSignSN());
    }

    /**
     * 报文环境位校验
     */
    public static void validateEnvFlag(SoapHeader header) {
        /*
         * 压测流量标志位校验规则: 
         * 1.压测流量标志位允许为空 
         * 2.但压测流量标志位不为空时，压测流量标识位必须为"true"或"false"
         */
        String loadTraffic = TraceContext.get(CommonConstant.LOAD_TRAFFIC_KEY);
        if (StringUtil.isNotBlank(loadTraffic)) {
            if (!Constant.LOAD_TRAFFIC_SWITCH_TRUE.equals(loadTraffic)
                    && !Constant.LOAD_TRAFFIC_SWITCH_FALSE.equals(loadTraffic)) {
                throw new GwException(GwErrorEnum.VALIDATION_ERROR.getCode(),
                        "压测流量标识LoadTraffic校验不通过: " + loadTraffic);
            }
        }

        /*
         * 压测流量标志位拦截规则： 
         * 1.当且仅当压测总开关打开时，允许压测流量通过 
         * 2.当请求报文为压测流量时，HTTP HEADER压测流量标识位必须为"true" 
         * 3.当请求报文为非压测流量时， HTTP HEADER压测流量标识位为"false"或空 
         * 4.当请求报文为非压测流量时，根据报文环境标志位拦截规则进一步过滤当前环境支持的环境位
         */
        String envFlag = header.getMsgSN().substring(31, 32);
        if (CommonConstant.MSN_LOAD_TRAFFIC_CODE.equals(envFlag)) {
            // 请求报文为压测流量
            Boolean pressSwitch = NacosConfigClient.getInstance().getPressFlag();
            if (!pressSwitch) {
                throw new GwException(GwErrorEnum.PERF_TEST_SWITCH_CLOSE_ERROR.getCode(),
                        GwErrorEnum.PERF_TEST_SWITCH_CLOSE_ERROR.getDescription());
            }
            if (!Constant.LOAD_TRAFFIC_SWITCH_TRUE.equals(loadTraffic)) {
                log.error("压测流量标识LoadTraffic:{}", loadTraffic);
                throw new GwException(GwErrorEnum.LOADTRAFFIC_MSGID_NOT_MATCH_ERROR.getCode(),
                        GwErrorEnum.LOADTRAFFIC_MSGID_NOT_MATCH_ERROR.getDescription());
            }
        } else {
            // 请求报文为非压测流量
            if (Constant.LOAD_TRAFFIC_SWITCH_TRUE.equals(loadTraffic)) {
                log.error("压测流量标识LoadTraffic:{}", loadTraffic);
                throw new GwException(GwErrorEnum.LOADTRAFFIC_MSGID_NOT_MATCH_ERROR.getCode(),
                        GwErrorEnum.LOADTRAFFIC_MSGID_NOT_MATCH_ERROR.getDescription());
            }
            TraceContext.set(CommonConstant.LOAD_TRAFFIC_KEY, Constant.LOAD_TRAFFIC_SWITCH_FALSE);

            /*
             * 报文环境标志位拦截规则： 
             * 1.生产环境允许通过生产环境数据(1) 
             * 2.预发环境请求报文允许通过生产环境数据(1)、预发环境数据(3) 
             * TODO 20231109 by laimincai
             * 目前预发环境拦截粒度过粗,需治理DTO注解isReturn标识后再对预发环境报文细化区分请求报文支持生产环境数据(1)、应答报文支持生产环境数据(1)、预发环境数据(3)
             * 3.测试环境允许通过测试环境数据(0)、生产环境数据(1)、预发环境数据(3)
             */
            if (!NacosConfigClient.getInstance().getEnvFlag().contains(envFlag)) {
                log.warn("不支持环境位标识: {},{},{}", header.getSender(), header.getMsgTp(), header.getMsgSN());
            }
        }
    }

    /**
     * 验签
     *
     * @param base64Str
     * @param strData
     * @param certId
     */
    public static void verifySign(String base64Str, String strData, String certId) {
        // 开关判断是否需要验签
        if (!NacosConsume.getIsSign()) {
            return;
        }

        // 验签
        try {
            verifySign(base64Str, strData.getBytes(Constant.CHARTSET), certId);
        } catch (UnsupportedEncodingException e) {
            throw new GwException(GwErrorEnum.VIRIFY_SIGN_ERROR, "UnsupportedEncodingException", e);
        }
    }

    /**
     * 验签
     *
     * @param base64Str
     * @param strData
     * @param certId
     */
    public static void verifySign(String sign, byte[] strData, String certId) {
        // 开关判断是否需要验签
        if (!NacosConsume.getIsSign()) {
            return;
        }

        // 验签
        try {
            boolean result = SignClient.getInstance().p1SignVerify(certId, sign, strData, MDC.get("msgSn"));
            if (!result) {
                throw new GwException(GwErrorEnum.VIRIFY_SIGN_ERROR, "验签失败：" + result);
            }
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.VIRIFY_SIGN_ERROR, "验签失败：" + e.getMessage(), e);
        }
    }

    /**
     * 加签
     *
     * @param signSrc
     * @param certId
     * @return
     */
    public static String sign(String signSrc, String certId) {
        // 开关判断是否需要加签
        if (!NacosConsume.getIsSign()) {
            return "";
        }

        // 加签
        try {
            return sign(signSrc.getBytes(Constant.CHARTSET), certId);
        } catch (UnsupportedEncodingException e) {
            throw new GwException(GwErrorEnum.SIGN_ERROR, "UnsupportedEncodingException", e);
        }
    }

    /**
     * 加签
     *
     * @param signSrc
     * @param certId
     * @return
     */
    public static String sign(byte[] signSrc, String certId) {
        // 开关判断是否需要加签
        if (!NacosConsume.getIsSign()) {
            return "";
        }

        // 加签
        try {
            return SignClient.getInstance().p1Sign(certId, signSrc, MDC.get("msgSn"));
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.SIGN_ERROR, "数字签名失败: " + e.getMessage(), e);
        }
    }

    public static <T> void validate(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj, ValidSequence.class);
        if (violations != null) {
            for (ConstraintViolation<T> violation : violations) {
                throw new GwException(GwErrorEnum.VALIDATION_ERROR,
                        "参数校验失败--" + violation.getPropertyPath() + ":" + violation.getMessage(),
                        new ConstraintViolationException(obj.getClass() + "参数校验异常：" + violation,
                                violations));
            }
        }
    }

    /**
     * @param envelopeDTO
     * @param <T>
     */
    public static <T extends GwDTO> void check(EnvelopeDTO<T> envelopeDTO) {
        boolean flag = true;
        try {
            flag = envelopeDTO.body().check(envelopeDTO.getSoapHeader());
            if (!flag) {
                throw new GwException(GwErrorEnum.SOAPHEADER_SOAPBODY_NOT_MATCH_ERROR);
            }

            // 只有校验开关打开时才走此逻辑
            if (!TradeParamWarningType.NO_CHECK.getType().equals(getTradeparamInvalidWarningType())) {
                // 报文类型与业务类型是否匹配、业务种类与业务类型是否匹配
                checkbizCtgyTypeAuthorized(envelopeDTO);
            }

            // 压测环境位与账号关联关系检查
            checkAccountTag(envelopeDTO);
        } catch (GwException ex) {
            throw ex;
        } catch (DcepException e) {
            throw new GwException(e);
        }

    }

    /**
     * BeanWrapperImpl的线程本地Holder
     */
    private static final ThreadLocal<BeanWrapperImpl> BEAN_WRAPPER_HOLDER =
            ThreadLocal.withInitial(() -> {
                return new BeanWrapperImpl(false);
            });

    /**
     * 从dto中获取指定表达式的值
     *
     * @param express 表达式
     * @param t dto的值
     * @return dto中表达式的值
     */
    public static <T> String getExpressValue(String express, T t) {
        try {
            BeanWrapperImpl wrapper = BEAN_WRAPPER_HOLDER.get();
            wrapper.setWrappedInstance(t);
            return (String) wrapper.getPropertyValue(express);
        } catch (NullValueInNestedPathException e) {
            return null;
        }
    }

    /**
     * 从nacos获取交易参数校验和告警类型
     *
     * @return 校验和告警类型
     */
    public static Integer getTradeparamInvalidWarningType() {
        return NacosConfigClient.getInstance().getTradeparamInvalidWarningType();
    }

    /**
     * 业务类型和业务种类校验
     *
     * @param envelopeDTO 请求dto
     */
    private static void checkbizCtgyTypeAuthorized(EnvelopeDTO<?> envelopeDTO) {
        GwDTO body = envelopeDTO.body();
        SoapHeader header = envelopeDTO.getSoapHeader();
        Class<? extends GwDTO> dtoClazz = body.getClass();
        if (dtoClazz.isAnnotationPresent(CheckBizCode.class)) {
            CheckBizCode annotation = dtoClazz.getAnnotation(CheckBizCode.class);

            String bizTypeCode = getExpressValue(annotation.bizTypeCode(), envelopeDTO.getSoapBody().getT());
            String bizCtgyCode = getExpressValue(annotation.bizCtgyCode(), envelopeDTO.getSoapBody().getT());

            BizRelationCache bizRelationCache = BizRelationCache.getInstance();

            if (isCheckTradeParam(annotation, envelopeDTO)) {
                Date currentDate = new Date();

                // 从nacos获取配置天数以及开关
                NacosConfigClient nacosConfigClient = NacosConfigClient.getInstance();
                Integer days = nacosConfigClient.getTradeparamInvalidWarningDays();

                // 报文类型与业务类型的判断
                DateDTO msgTypeBizTypeCodeDate = bizRelationCache.msgBiz(header.getMsgTp(), bizTypeCode);
                if (null == msgTypeBizTypeCodeDate) {
                    log.warn("机构：{} 报文类型与业务类型不匹配", header.getSender());
                    if (TradeParamWarningType.CHECK_AND_BLOCK.getType().equals(getTradeparamInvalidWarningType())) {
                        throw new GwException(GwErrorEnum.MSGTYPE_UNMATCH_BIZTYPE);
                    }
                }

                // 当前时间不在生效失效时间段内
                else {
                    if (!DateUtil.isIn(currentDate, msgTypeBizTypeCodeDate.getBeginTime(),
                            msgTypeBizTypeCodeDate.getEndTime())) {
                        log.warn("机构：{} 报文类型与业务类型已过失效时间", header.getSender());
                        if (TradeParamWarningType.CHECK_AND_BLOCK.getType().equals(getTradeparamInvalidWarningType())) {
                            throw new GwException(GwErrorEnum.MSGTYPE_BIZTYPE_TIME_INVALID);
                        }
                    }

                    if (DateUtil.between(currentDate, msgTypeBizTypeCodeDate.getEndTime(), DateUnit.DAY) < days) {
                        log.warn("机构：{} 报文类型：{}，业务类型：{}的业务权限已不足{}天", header.getSender(),
                                header.getMsgTp(), bizTypeCode, days);
                    }
                }

                // 业务种类与业务类型的判断
                DateDTO typeCtgyDate = bizRelationCache.bizSubBiz(bizTypeCode, bizCtgyCode);
                if (null == typeCtgyDate) {
                    log.warn("机构：{} 业务种类与业务类型不匹配", header.getSender());
                    if (TradeParamWarningType.CHECK_AND_BLOCK.getType().equals(getTradeparamInvalidWarningType())) {
                        throw new GwException(GwErrorEnum.BIZCTGY_UNMATCH_BIZTYPE);
                    }
                }

                // 当前时间不在生效失效时间段内
                else {
                    if (!DateUtil.isIn(currentDate, typeCtgyDate.getBeginTime(), typeCtgyDate.getEndTime())) {
                        log.warn("机构：{} 业务种类与业务类型已过失效时间", header.getSender());
                        if (TradeParamWarningType.CHECK_AND_BLOCK.getType().equals(getTradeparamInvalidWarningType())) {
                            throw new GwException(GwErrorEnum.BIZCTGY_BIZTYPE_TIME_INVALID);
                        }
                    }

                    if (DateUtil.between(currentDate, typeCtgyDate.getEndTime(), DateUnit.DAY) < days) {
                        log.warn("机构：{} 业务种类：{}，业务类型：{}的权限已不足{}天", header.getSender(), bizCtgyCode,
                                bizTypeCode, days);
                    }
                }
            }
        }

    }

    /**
     * 从condition中抽取表达式和值，并判断表达式的值是否与期望的一致
     *
     * @param annotation 注解的值
     * @param envelopeDTO dto
     * @return 是否需要校验
     */
    private static boolean isCheckTradeParam(CheckBizCode annotation, EnvelopeDTO<?> envelopeDTO) {
        String condition = annotation.condition();
        if (StringUtils.isBlank(condition)) {
            throw new GwException("CheckBizCode注解在有condition时，必须非空");
        }

        // 如果注解中不带condition，则默认为true
        if (StringUtils.equalsAnyIgnoreCase(condition, String.valueOf(Boolean.TRUE))) {
            return true;
        }

        String[] expressAndValue = StringUtils.split(condition, ",");
        if (ArrayUtils.getLength(expressAndValue) != 2) {
            throw new GwException("CheckBizCode注解中condition必须为 express,value格式");
        }

        String actualValue = getExpressValue(expressAndValue[0], envelopeDTO.getSoapBody().getT());
        String expectValue = expressAndValue[1];

        return StringUtils.equalsAnyIgnoreCase(actualValue, expectValue);
    }

    /**
     * 检查压测环境位与账号关联关系，校验逻辑如下： 
     * 1）当压测流量标识为压测流量时，账号类型必须为压测账号 
     * 2）当压测流量标识为非压测流量时，账号类型不能为压测账号
     * 
     * @param envelopeDTO
     */
    private static void checkAccountTag(EnvelopeDTO<?> envelopeDTO) {
        GwDTO body = envelopeDTO.body();
        if (body.getClass().isAnnotationPresent(CheckAccountTag.class)
                || body.getClass().isAnnotationPresent(CheckAccountTags.class)) {
            CheckAccountTag[] checkAccountTags =
                    body.getClass().getAnnotationsByType(CheckAccountTag.class);
            for (CheckAccountTag checkAccountTag : checkAccountTags) {
                for (String path : checkAccountTag.path()) {
                    String accountValue = getExpressValue(path, body);
                    if (StringUtils.isBlank(accountValue)) {
                        continue;
                    }
                    boolean isPerfTest =
                            AccountUtil.isPerfTestAccount(accountValue, checkAccountTag.type());
                    boolean loadTraffic =
                            Boolean.valueOf(TraceContext.get(CommonConstant.LOAD_TRAFFIC_KEY));
                    boolean isNotMatch = Boolean.logicalXor(isPerfTest, loadTraffic);
                    if (isNotMatch) {
                        log.error("账号类型{}与压测流量标识{}不匹配", accountValue, loadTraffic);
                        throw new GwException(GwErrorEnum.VALIDATION_ERROR, "账号类型与压测流量标识不匹配");
                    }
                }
            }
        }
    }
}
