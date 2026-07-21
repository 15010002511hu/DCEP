package com.emop.wlt.user.management.dto;

import com.emop.common.utils.MaskUtils;
import java.io.Serializable;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CustomerSaveDTO implements Serializable {

    private String walletId;

    private String idNumber;

    private String idType;

    /**
     * 国家地区代码
     */
    private String countryAndRegionCode;

    /**
     * 用户类型:
     */
    private String customerType;

    @ToString.Include(name = "idNumber")
    private String idNoMasker() {
        return MaskUtils.IDNumber(idNumber);
    }
}
