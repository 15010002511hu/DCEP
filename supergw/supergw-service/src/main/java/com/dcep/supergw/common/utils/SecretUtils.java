package com.dcep.supergw.common.utils;

import com.dcep.common.encryption.DataEncryption;
import com.dcep.common.encryption.TransEncryption;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.supergw.common.constant.Constant;
import com.dcep.supergw.manager.secure.AbstractEncryptionHelper;

/**
 * @author maxinyu
 * @date 2023/6/9 9:15
 */
public class SecretUtils {

    /**
     * DTO解密
     */
    public static void decrypt(EnvelopeDTO<?> dto) {
        /*若dto没有实现敏感信息，则不进行解密操作*/
        if (!(dto.body() instanceof DataEncryption)) {
            return;
        }
        //使用策略选择helper
        AbstractEncryptionHelper helper = EncryptionHelperHolder.getHelperWhenDecrypt(dto);
        helper.decryptInit(dto);
        ((DataEncryption) dto.body()).decryptData(helper);
        helper.decryptFinish(dto);
    }


    /**
     * DTO转加密
     */
    public static void transEncrypt(EnvelopeDTO<?> dto) {
        /*若dto没有实现敏感信息，则不进行解密操作*/
        if (!(dto.body() instanceof TransEncryption)) {
            return;
        }
        AbstractEncryptionHelper helper = EncryptionHelperHolder.getTransferEncryptionHelper(dto);
        helper.encryptInit(dto);
        ((TransEncryption) dto.body()).transEncrypt(helper);
        helper.encryptFinish(dto);
    }


    /**
     * DTO加密
     */
    public static void encrypt(EnvelopeDTO<?> dto) {
        /*若dto没有实现敏感信息，则不进行解密操作*/
        if (!(dto.body() instanceof DataEncryption)) {
            if (dto.body() instanceof TransEncryption) {
                return;
            }
            //设置SoapHeader中的ncrptSN和dgtlEnvlp 711为业务系统自己设置，所以711不用重置
            if (!Constant.DCEP_711_001_01.equals(dto.getSoapHeader().getMsgTp())) {
                dto.getSoapHeader().setNcrptnSN(null);
                dto.getSoapHeader().setDgtlEnvlp(null);
            }
            return;
        }
        //使用策略选择helper
        AbstractEncryptionHelper helper = EncryptionHelperHolder.getHelperWhenEncrypt(dto);
        helper.encryptInit(dto);
        ((DataEncryption) dto.body()).encryptData(helper);
        helper.encryptFinish(dto);
    }

}
