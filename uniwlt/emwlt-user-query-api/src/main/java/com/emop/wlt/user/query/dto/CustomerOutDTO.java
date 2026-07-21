package com.emop.wlt.user.query.dto;

import com.emop.common.utils.MaskUtils;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
public class CustomerOutDTO implements Serializable {

    /**
     * customerId
     */
    private String cid;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 证件号
     */
    private String idNumber;

    @ToString.Include(name = "idNumber")
    private String idNoMasker() {
        return MaskUtils.IDNumber(idNumber);
    }
}
