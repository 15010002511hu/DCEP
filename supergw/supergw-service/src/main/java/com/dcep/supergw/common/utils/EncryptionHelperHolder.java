package com.dcep.supergw.common.utils;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.supergw.manager.secure.AbstractEncryptionHelper;
import com.dcep.supergw.manager.secure.BodyTransferEncryptionHelperImpl;
import com.dcep.supergw.manager.secure.HardEncryptionHelperImpl;
import com.dcep.supergw.manager.secure.HeaderTransferEncryptionHelperImpl;
import com.dcep.supergw.validation.HeaderTransfer;

/**
 * @author maxinyu
 * @date 2023/6/9 14:19
 */
public class EncryptionHelperHolder {

    public static AbstractEncryptionHelper getHelperWhenDecrypt(EnvelopeDTO<?> dto) {
        return new HardEncryptionHelperImpl();
    }


    public static AbstractEncryptionHelper getHelperWhenEncrypt(EnvelopeDTO<?> dto) {
        return new HardEncryptionHelperImpl();
    }


    public static AbstractEncryptionHelper getTransferEncryptionHelper(EnvelopeDTO<?> dto) {
        if (dto.body().getClass().getAnnotation(HeaderTransfer.class) != null) {
            return new HeaderTransferEncryptionHelperImpl();
        }
        return new BodyTransferEncryptionHelperImpl();
    }
}
