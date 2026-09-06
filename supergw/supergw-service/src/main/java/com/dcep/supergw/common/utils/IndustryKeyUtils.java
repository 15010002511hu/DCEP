/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.common.utils;

import cn.com.platform.security.adapter.Algorithm;
import cn.com.platform.security.adapter.exception.PlatformException;
import cn.com.platform.security.adapter.key.PSecretKey;
import cn.com.platform.security.adapter.key.PX509CertKey;
import cn.com.platform.security.adapter.key.format.KeyEncFormat;
import cn.com.platform.security.adapter.key.format.KeyLabelFormat;
import cn.com.platform.security.adapter.key.format.X509CertRawFormat;
import cn.com.platform.security.adapter.result.RetKDFParameter;
import cn.com.platform.security.adapter.result.RetWrapKeyParameter;
import com.crypto.CryptoAdapter;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.ArrayUtils;

/**
 * 行业密钥生成工具类 TODO 该工具类临时存放网关，后续迁移至密服SDK维护
 *
 * @author laimincai
 * @date 2023/12/28
 */
public class IndustryKeyUtils {

    private static final Algorithm ALG_SM4_ECB_KDF = new Algorithm(Algorithm.GP_SM4_ECB_KDF);
    private static final Algorithm ALG_SM4 = new Algorithm(Algorithm.SM4_KEY);
    private static final Algorithm ALG_SM2_SM3_ENC = new Algorithm(Algorithm.SM2_SM3_ENC);

    private static byte[] expandToFactor(byte[] industryData) throws PlatformException {
        if (industryData == null || industryData.length == 0 || industryData.length > 8) {
            throw new PlatformException("Factor Information error");
        }
        byte[] result = new byte[16];
        int i = 0;
        for (; i < industryData.length; i++) {
            result[i] = industryData[i];
        }
        for (; i < 8; i++) {
            result[i] = (byte) 255;
        }
        for (int j = 8; j < result.length; j++) {
            result[j] = (byte) (~result[j - 8]);
        }
        return result;
    }

    public static byte[] create(String requestId, CryptoAdapter api, String reqKeyLabel, String industryID,
        String industryInformation, String industryFactorId, byte[] encryptCertificate) throws PlatformException {
        try {
            //step1
            byte[] factor1 = expandToFactor(ArrayUtils.addAll(DatatypeConverter.parseHexBinary(industryID),
                DatatypeConverter.parseHexBinary(industryInformation)));
            PSecretKey rootKey = new PSecretKey(new KeyLabelFormat(reqKeyLabel));
            RetKDFParameter ret1 = api.KDF(requestId, rootKey, ALG_SM4_ECB_KDF, factor1, ALG_SM4);

            //step2
            byte[] factor2 = expandToFactor(DatatypeConverter.parseHexBinary(industryFactorId));
            PSecretKey key = new PSecretKey(Algorithm.SM4_KEY, new KeyEncFormat(ret1.getKey()));
            RetKDFParameter ret2 = api.KDF(requestId, key, ALG_SM4_ECB_KDF, factor2, ALG_SM4);

            //wrap
            PSecretKey retKey = new PSecretKey(new KeyEncFormat(ret2.getKey()));
            PX509CertKey px509CertKey = new PX509CertKey(new X509CertRawFormat(encryptCertificate));
            RetWrapKeyParameter ret3 = api.wrapKey(requestId, px509CertKey, ALG_SM2_SM3_ENC, retKey);

            return ret3.getKeyEvp();
        } catch (PlatformException e) {
            throw e;
        } catch (Exception e) {
            throw new PlatformException(e.getMessage());
        }
    }
}