package com.emop.wlt.user.management.model.vo;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CustomerIdInfoVO implements Serializable {

    private static final long serialVersionUID = -1987885240990775722L;

    /**
     * 证件号码
     */
    private String idNumber;

    /**
     * 证件类型
     */
    private String idType;

}
