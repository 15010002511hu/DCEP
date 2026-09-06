package com.dcep.supergw.manager.secure;

import com.dcep.common.encryption.EncryptionHelper;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.supergw.common.constant.Constant;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author maxinyu
 * @date 2023/6/9 16:07
 */
public abstract class AbstractEncryptionHelper implements EncryptionHelper {
    /**
     * 加密之前完成的操作
     *
     * @param dto
     */
    public abstract void encryptInit(EnvelopeDTO<?> dto);

    /**
     * 加密之后完成的操作
     *
     * @param dto
     */
    public abstract void encryptFinish(EnvelopeDTO<?> dto);

    /**
     * 解密之前完成的操作
     *
     * @param dto
     */
    public abstract void decryptInit(EnvelopeDTO<?> dto);

    /**
     * 解密之后完成的操作
     *
     * @param dto
     */
    public abstract void decryptFinish(EnvelopeDTO<?> dto);

    public List<String> formatRequest(List<String> request) {
        List<String> list = new ArrayList<>();
        for (String s : request) {
            if (StringUtils.isNotBlank(s)) {
                list.add(s);
            }
        }
        return list;
    }

    public List<String> formatResult(List<String> result, List<String> origin) {
        if (origin.size() == result.size()) {
            return result;
        }
        List<String> list = new ArrayList<>();

        for (int i = 0, j = 0; i < origin.size(); i++) {
            if (origin.get(i) != null) {
                if ("".equals(origin.get(i))) {
                    list.add("");
                } else {
                    list.add(result.get(j));
                    j++;
                }
            } else {
                list.add(null);

            }
        }
        return list;
    }

    public String formatCipherKey(String orgCode, String number) {
        StringBuilder sb = new StringBuilder();
        sb.append(orgCode);
        //下划线
        sb.append(Constant.CERT_SEPARATOR);
        sb.append(number);
        //_e
        sb.append(Constant.ENCRYPT_SUFFIX);
        return sb.toString();
    }
}
