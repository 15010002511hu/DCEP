/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto;

import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.validator.Priority;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 业务状态类
 * @author sunxiaofeng
 * @version $Id: ClearResp.java, v 0.1 2019年8月22日 下午4:26:59 sunxiaofeng Exp $
 */
@Data
public class BizStatusDTO implements Serializable {
    private static final long serialVersionUID = 7500500059344211593L;

    /**
     * 业务状态
     */
    @NotBlank(groups = Priority.Highest.class, message = "业务状态，不能为空")
    @Size(min = 4, max = 4, message = "业务状态，长度只能为4")
    private String bizSts;

    /**
     * 业务处理码
     */
    @Size(max = 10, message = "业务处理码，最长10")
    private String bizPrcCd;

    /**
     * 业务处理信息
     */
    @Size(max = 100, message = "业务处理信息，长度不能超过100")
    private String bizPrcInf;

    public BizStatusDTO(String bizSts, String bizPrcCd, String bizPrcInf) {
        this.bizSts = bizSts;
        this.bizPrcCd = bizPrcCd;
        this.bizPrcInf = bizPrcInf;
    }

    public BizStatusDTO(String bizSts) {
        this.bizSts = bizSts;
    }
}
