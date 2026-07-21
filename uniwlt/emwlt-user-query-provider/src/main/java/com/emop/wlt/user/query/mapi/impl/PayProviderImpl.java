package com.emop.wlt.user.query.mapi.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alipay.gateway.mobileservice.invoke.MobileRpcHolder;
import com.emop.common.enums.errorcode.BaseErrorEnum;
import com.emop.gateway.api.sdk.service.dto.GwPojoMsgDTO;
import com.emop.infocache.UniCodeCache;
import com.emop.infocache.api.dto.WhiteReceiveDomainDTO;
import com.emop.infocache.api.dto.WhiteReceiveDomainDetailDTO;
import com.emop.wlt.aspect.annotation.AppResponse;
import com.emop.wlt.aspect.app.util.HeaderUtils;
import com.emop.wlt.common.exception.EmwltException;
import com.emop.wlt.common.model.mapp.RequestModel;
import com.emop.wlt.common.model.mapp.ResponseModel;
import com.emop.wlt.common.util.Safes;
import com.emop.wlt.common.util.UserUtils;
import com.emop.wlt.common.util.VersionUtils;
import com.emop.wlt.gateway.manager.EmapGatewayManager;
import com.emop.wlt.model.emap.EmapDTOBuildHelper;
import com.emop.wlt.model.emap.emap214.*;
import com.emop.wlt.redis.service.RedisOperator;
import com.emop.wlt.user.info.api.QRCodeProvider;
import com.emop.wlt.user.query.constant.QRCodeConstant;
import com.emop.wlt.user.query.constant.RedisKeyConstant;
import com.emop.wlt.user.query.exception.ExceptionCast;
import com.emop.wlt.user.query.manager.CloudControlUserManager;
import com.emop.wlt.user.query.mapi.PayProvider;
import com.emop.wlt.user.query.model.request.Mapp09500101Req;
import com.emop.wlt.user.query.model.response.Mapp09500101Resp;
import com.emop.wlt.user.query.model.vo.MerchantInfoVO;
import com.emop.wlt.user.query.model.vo.PayeeWalletInfoVO;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.redisson.api.RBucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.emop.wlt.common.constant.CommonConstant.BOC_MACAO_CODE;
import static com.emop.wlt.common.constant.HttpHeaderConstant.PLATFORM;
import static com.emop.wlt.common.constant.SplitConstant.SPLITTER_FORWARD_SLASH;
import static com.emop.wlt.user.query.constant.QRCodeConstant.*;

/**
 * 支付相关实现类
 *
 * @author liuyuntao
 * @since 1.0.0
 */
@Slf4j
@Service
public class PayProviderImpl implements PayProvider {

    private static final String QR_CODE_QT01_PREFIX = "000201";
    private Set<String> cnyDomainSet = Sets.newHashSet("qr.pbcdci.cn");

    @Autowired
    private UniCodeCache uniCodeCache;
    @Autowired
    private EmapGatewayManager emapGatewayManager;
    @Autowired
    private CloudControlUserManager cloudControlUserManager;
    @Autowired
    private RedisOperator redisOperator;
    @DubboReference
    private QRCodeProvider qrCodeProvider;

    @Override
    @AppResponse
    public ResponseModel<Mapp09500101Resp> qrCodeQuery(RequestModel<Mapp09500101Req> requestModel) {
        String userId = UserUtils.getUserId();
        Mapp09500101Req mapp09500101Req = requestModel.getMessageBody();

        // 根据域名获取公管配置
        WhiteReceiveDomainDTO domainDTO = getWhiteReceiveDomain(mapp09500101Req.getQrCode());
        /*
        if (!QRCodeConstant.QT01.equals(domainDTO.getCodeType())) {
            // 判断是否需要引导升级
            this.checkUserCanUsedQRCode(domainDTO);
        }
         */

        // 根据模式进行二维码解码
        String decodeMode = domainDTO.getDecodeMode();
        String instNo = domainDTO.getFiCode();

        Mapp09500101Resp mapp09500101Resp = new Mapp09500101Resp();
        mapp09500101Resp.setQrCodeEncodingType(domainDTO.getCodeType());
        mapp09500101Resp.setQrCodeDecodingMode(decodeMode);

        // 如果不是 网关访问URL模式需要去机构查询收款码
        if (StringUtils.equals(decodeMode, QRCodeConstant.QDM03)) {
            // 网关跳转模式新增响应授权code
            String userAuthCode = qrCodeProvider.queryUserAuthCode(userId, mapp09500101Req.getUserUniqueId());
            mapp09500101Resp.setUserAuthCode(userAuthCode);
            // 跳转模式直接返回原url
            mapp09500101Resp.setAcquiringRedirectUrl(mapp09500101Req.getQrCode());
        } else if (StringUtils.equals(decodeMode, QRCodeConstant.QDM01)
                || StringUtils.equals(decodeMode, QRCodeConstant.QDM02)) {
            // 收款码查询
            Emap21400101ReqDTO emap21400101ReqDTO = new Emap21400101ReqDTO();
            emap21400101ReqDTO.setWalletId(mapp09500101Req.getWalletId());
            emap21400101ReqDTO.setQrCode(mapp09500101Req.getQrCode());
            emap21400101ReqDTO.setMobilePhoneNumber(mapp09500101Req.getMobileNumber());
            emap21400101ReqDTO.setUserUniqueId(mapp09500101Req.getUserUniqueId());
            GwPojoMsgDTO gwPojoMsgDTO = EmapDTOBuildHelper.createGwPojoMsgDTO(emap21400101ReqDTO, userId, instNo);
            Emap21400101RespDTO emap21400101RespDTO =
                    (Emap21400101RespDTO) emapGatewayManager.send(gwPojoMsgDTO).getMessageBody();
            // 转换为响应参数
            this.buildMapp095Resp(mapp09500101Resp, emap21400101RespDTO, decodeMode);
        } else {
            ExceptionCast.buzCast(BaseErrorEnum.B34002);
        }

        return ResponseModel.<Mapp09500101Resp>builder().messageBody(mapp09500101Resp).build();
    }

    /**
     * 检查用户是否可以使用此二维码
     */
    private void checkUserCanUsedQRCode(WhiteReceiveDomainDTO domainDTO) {
        // 判断用户是否可以使用此配置
        boolean isWhite = cloudControlUserManager.isQRCodePayWhite(UserUtils.getUserPhone());
        log.info("isWhite:[{}]", isWhite);
        // 如果是白名单
        List<WhiteReceiveDomainDetailDTO> domainDetailList = domainDTO.getDetail();

        // 如果规则不为空,则进行规则判断
        if (CollectionUtils.isEmpty(domainDetailList)) {
            // 没有匹配上默认不支持
            // 您扫描的二维码暂不支持数字人民币支付
            ExceptionCast.cast(BaseErrorEnum.B34001);
        }
        // 获取客户端当前版本号
        String appVersion = HeaderUtils.getEnvInfo().getAppVersion();
        // 获取客户端当前版本号
        String platform = Optional.ofNullable(MobileRpcHolder.getHeaders())
                .map(headers -> headers.get(PLATFORM))
                .orElse(null);

        if (StringUtils.isBlank(appVersion) || StringUtils.isBlank(platform)) {
            ExceptionCast.cast(BaseErrorEnum.S02021);
        }

        // 对所有规则进行匹配，没有匹配上则默认可用
        for (WhiteReceiveDomainDetailDTO domainDetail : domainDetailList) {
            String maxVersion = domainDetail.getMaxVersion();
            String minVersion = domainDetail.getMinVersion();
            String processRule = domainDetail.getProcessRule();
            String systemType = domainDetail.getSystemType();
            String userGroup = domainDetail.getUserGroup();

            // 如果分组是内测分组且用户不是内测则进行下一个规则匹配
            if (StringUtils.equals(QRCODE_DOMS_TEST_USER_GROUP, userGroup) && !isWhite) {
                continue;
            }

            // 版本不匹配进行下一个规则匹配
            if (!StringUtils.equalsIgnoreCase(systemType, platform)) {
                continue;
            }

            // 如果最小版本为不限制比较最小版本
            if (!Objects.equals(APP_UNLIMITED_VERSION, minVersion)
                    && VersionUtils.compare(minVersion, appVersion) > 0) {
                continue;
            }

            // 如果最大版本为不限制比较最大版本
            if (!Objects.equals(APP_UNLIMITED_VERSION, maxVersion)
                    && VersionUtils.compare(appVersion, maxVersion) > 0) {
                continue;
            }

            // 满足规则，处理并结束循环
            // 引导升级异常
            if (StringUtils.equals(QRCODE_DOMS_APP_UPGRADE_KEY, processRule)) {
                // 二维码需要升级app使用
                ExceptionCast.cast(BaseErrorEnum.B12304);
            }

            //  不可用规则
            if (StringUtils.equals(QRCODE_DOMS_APP_UNUSABLE, processRule)) {
                // 您扫描的二维码暂不支持数字人民币支付
                ExceptionCast.cast(BaseErrorEnum.B34001);
            }

            //  可用规则
            if (StringUtils.equals(QRCODE_DOMS_APP_USABLE, processRule)) {
                return;
            }
        }
    }

    /**
     * 根据域名解析公管配置
     *
     * @param qrCodeUrl
     * @return
     */
    private WhiteReceiveDomainDTO getWhiteReceiveDomain(String qrCodeUrl) {
        // 获取二维码，根据公管配置进行二维码解析
        URI uri;
        //  解析URI出错返回不是有效地数币码
        try {
            uri = new URI(StringUtils.trim(qrCodeUrl));
        } catch (Exception e) {
            throw new EmwltException(BaseErrorEnum.B34001, e);
        }

        // 协议
        String protocol = uri.getScheme();
        // 域名
        String domain = uri.getHost();
        if (StringUtils.isBlank(domain)) {
            // 一期：
            // 个人收款码：快捷模式、000201前缀
            // 商户收款码：公管配置、url格式
            if (StringUtils.startsWith(qrCodeUrl, QR_CODE_QT01_PREFIX)) {
                WhiteReceiveDomainDTO whiteReceiveDomainDTO = new WhiteReceiveDomainDTO();
                whiteReceiveDomainDTO.setCodeType(QRCodeConstant.QT01);
                whiteReceiveDomainDTO.setDecodeMode(QRCodeConstant.QDM01);
                whiteReceiveDomainDTO.setFiCode(BOC_MACAO_CODE);
                return whiteReceiveDomainDTO;
            }
            // 您扫描的二维码暂不支持数字人民币支付
            ExceptionCast.cast(BaseErrorEnum.B34001);
        }

        int port = uri.getPort();

        // 如果不是-1说明有端口，需要再域名上加上端口进行匹配
        if (-1 != port) {
            domain = domain + QRCodeConstant.DOMAIN_POORT_SPLIT + port;
        }

        log.info("protocol:[{}] domain:[{}] port:[{}] path:[{}]", protocol, domain, port, uri.getPath());

        Map<String, WhiteReceiveDomainDTO> domainDTOMap = this.whiteReceiveDomainToCache();

        // 无配置不支持域名解析
        if (MapUtils.isEmpty(domainDTOMap)) {
            // 您扫描的二维码暂不支持数字人民币支付
            ExceptionCast.cast(BaseErrorEnum.B34001);
        }

        // 先根据协议来匹配，如果是wxp则是微信
        WhiteReceiveDomainDTO domainDTO = domainDTOMap.get(protocol);
        if (Objects.nonNull(domainDTO)) {
            return domainDTO;
        }

        // 如果协议匹配失败继续拿域名进行匹配
        domainDTO = domainDTOMap.get(domain);
        if (Objects.nonNull(domainDTO)) {
            return domainDTO;
        }

        // 域名匹配失败尝试用受理机构标识码机型匹配
        // 银联收款码url分割后长度应该大于2
        String path = uri.getPath();
        if (StringUtils.isBlank(path)) {
            // 您扫描的二维码暂不支持数字人民币支付
            ExceptionCast.cast(BaseErrorEnum.B34001);
        }

        List<String> pathSplit = SPLITTER_FORWARD_SLASH.splitToList(path);
        if (CollectionUtils.isEmpty(pathSplit)) {
            // 您扫描的二维码暂不支持数字人民币支付
            ExceptionCast.cast(BaseErrorEnum.B34001);
        }

        String acceptAgencyCode = Safes.first(pathSplit);

        // 数币码：https://qr.pbcdci.cn/002030210704250342771111111111111111111
        // 如果是数币码，code取前3位002来进行匹配
        if (cnyDomainSet.contains(domain)) {
            if (acceptAgencyCode.length() >= 3) {
                acceptAgencyCode = StringUtils.substring(acceptAgencyCode, 0, 3);
            } else {
                // 您扫描的二维码暂不支持数字人民币支付
                ExceptionCast.cast(BaseErrorEnum.B34001);
            }
        }

        // 标准码需要用域名加机构号进行匹配
        domainDTO = domainDTOMap.get(domain + acceptAgencyCode);
        // 还没有匹配到或者域名不对则返回参数错误
        if (Objects.isNull(domainDTO)) {
            log.info("acceptAgencyCode:[{}]", acceptAgencyCode);
            // 您扫描的二维码暂不支持数字人民币支付
            ExceptionCast.cast(BaseErrorEnum.B34001);
        }

        return domainDTO;
    }

    /**
     * 设置889参数
     *
     * @param mapp09500101Resp
     * @param emap21400101RespDTO
     * @param decodeMode
     */
    private void buildMapp095Resp(Mapp09500101Resp mapp09500101Resp,
                                  Emap21400101RespDTO emap21400101RespDTO, String decodeMode) {
        QRCodeInfoDTO qrCodeInfo = emap21400101RespDTO.getQrCodeInfo();
        String qrCodeType = qrCodeInfo.getQrCodeType();
        mapp09500101Resp.setQrType(qrCodeType);
        mapp09500101Resp.setTransactionAmount(qrCodeInfo.getTransactionAmount());

        if (StringUtils.equals(QT01, qrCodeType)) {
            PayeeWalletInfoDTO payeeWalletInfo = emap21400101RespDTO.getPayeeWalletInfo();
            PayeeWalletInfoVO payeeWalletInfoVO = new PayeeWalletInfoVO();
            payeeWalletInfoVO.setPayeeName(payeeWalletInfo.getPayeeName());
            payeeWalletInfoVO.setPayeeWalletId(payeeWalletInfo.getPayeeWalletId());
            payeeWalletInfoVO.setPayeeWalletType(payeeWalletInfo.getPayeeWalletType());
            payeeWalletInfoVO.setPayeeWalletLevel(payeeWalletInfo.getPayeeWalletLevel());
            payeeWalletInfoVO.setScanId(payeeWalletInfo.getScanId());
            payeeWalletInfoVO.setCountryCode(payeeWalletInfo.getCountryAndRegionCode());

            mapp09500101Resp.setPayeeWalletInfo(payeeWalletInfoVO);
        } else if (StringUtils.equals(QT04, qrCodeType) && StringUtils.equals(QDM01, decodeMode)) {
            OrderInfoDTO orderInfo = emap21400101RespDTO.getOrderInfo();
            mapp09500101Resp.setTransactionAmount(orderInfo.getTransactionAmount());
        }

        MerchantInfoDTO merchantInfo = emap21400101RespDTO.getMerchantInfo();
        if (Objects.nonNull(merchantInfo)) {
            MerchantInfoVO merchantInfoVO = new MerchantInfoVO();
            merchantInfoVO.setMerchantName(merchantInfo.getMerchantName());
            merchantInfoVO.setMerchantShortName(merchantInfo.getMerchantShortName());
            merchantInfoVO.setMerchantNumber(merchantInfo.getMerchantNumber());
            merchantInfoVO.setPayeeInstitutionCode(merchantInfo.getPayeeInstitutionCode());
            merchantInfoVO.setMerchantInstitutionCode(merchantInfo.getMerchantInstitutionCode());
            merchantInfoVO.setMerchantCategoryCode(merchantInfo.getMerchantCategoryCode());
            mapp09500101Resp.setMerchantInfo(merchantInfoVO);
        }
    }

    /**
     * 二次解析公管配置为缓存，方便快速解析
     */
    private Map<String, WhiteReceiveDomainDTO> whiteReceiveDomainToCache() {

        RBucket<String> rBucket = redisOperator.getRBucket(RedisKeyConstant.QRPAY_DOMAIN_REDIS_KEY);
        // 异常情况降级
        String domainCache = redisOperator.getValueOrExceptionDefault(rBucket, null);
        if (StringUtils.isNotBlank(domainCache)) {
            return JSON.parseObject(domainCache, new TypeReference<>() {
            });
        }

        // 获取公管配置信息
        List<WhiteReceiveDomainDTO> whiteReceiveDomainDTOS = uniCodeCache.listWhiteReceiveDomain();
        log.info("infocache qr cache:[{}]", whiteReceiveDomainDTOS);
        Map<String, WhiteReceiveDomainDTO> domainDTOMap = new HashMap<>(
                Objects.isNull(whiteReceiveDomainDTOS) ? 0 : whiteReceiveDomainDTOS.size() * 2);
        Set<String> cnyDomainSet = Sets.newHashSet("qr.pbcdci.cn");
        if (CollectionUtils.isNotEmpty(whiteReceiveDomainDTOS)) {
            whiteReceiveDomainDTOS.forEach(whiteReceiveDomainDTO -> {
                String protocolType = whiteReceiveDomainDTO.getProtocolType();
                String domainName = whiteReceiveDomainDTO.getDomainName();
                // 如果协议不是http的话，是私有协议，可以直接通过协议来确定解码方式
                if (!Objects.equals(protocolType, QRCodeConstant.HTTPS_PROTOCOL) && !Objects.equals(protocolType,
                        QRCodeConstant.HTTP_PROTOCOL)) {
                    domainDTOMap.put(protocolType, whiteReceiveDomainDTO);
                } else if (Objects.equals(whiteReceiveDomainDTO.getCodeType(), QRCodeConstant.QET02)) {
                    // 银联标准码或者数币标准码按照域名+受理机构标识识别
                    domainDTOMap.put(
                            whiteReceiveDomainDTO.getDomainName() + whiteReceiveDomainDTO.getAcceptAgencyCode(),
                            whiteReceiveDomainDTO);
                } else if (Objects.equals(whiteReceiveDomainDTO.getCodeType(), QRCodeConstant.QET01)) {
                    // 银联标准码或者数币标准码按照域名+受理机构标识识别
                    domainDTOMap.put(
                            whiteReceiveDomainDTO.getDomainName() + whiteReceiveDomainDTO.getAcceptAgencyCode(),
                            whiteReceiveDomainDTO);
                    cnyDomainSet.add(whiteReceiveDomainDTO.getDomainName());
                } else {
                    // 否则按域名进行匹配
                    domainDTOMap.put(domainName, whiteReceiveDomainDTO);
                }
            });
            log.info("local qr cache:[{}]", domainDTOMap);
        }
        this.cnyDomainSet = cnyDomainSet;
        redisOperator.setAsync(rBucket, JSON.toJSONString(domainDTOMap),
                RedisKeyConstant.QRPAY_DOMAIN_REDIS_KEY_EXPIRE, TimeUnit.MINUTES);
        return domainDTOMap;
    }

}
