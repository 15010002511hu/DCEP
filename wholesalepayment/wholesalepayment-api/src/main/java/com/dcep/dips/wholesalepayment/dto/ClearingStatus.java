/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapHeader;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 清算状态类
 * @author sunxiaofeng
 * @version $Id: ClearResp.java, v 0.1 2019年8月22日 下午4:26:59 sunxiaofeng Exp $
 */
@Setter
@Getter
@ToString
public class ClearingStatus extends GwDTO implements Serializable {

    /**  */
    private static final long serialVersionUID = 7500500059154211593L;

    /**
     * 业务状态
     * ProcessCode  表示perpare,finish,query处理状态  
     *              PR00：成功
                    PR01：失败
     */
    @Pattern(regexp = "PR00||PR01||PR02||PR03||PR04||PR05||PR06||PR07||PR08||PR09||PR10||PR11||PR12||PR13||PR14")
    private String            prcSts;

    /**
     * 交易批次号
     */
    @Pattern(regexp = "^[B][0-9]{12}$")
    private String            batchId;

    public ClearingStatus() {

    }

    public ClearingStatus(String prcSts, String batchId) {

        this.prcSts = prcSts;
        this.batchId = batchId;
    }

    @Override
    public boolean check(SoapHeader arg0) {
        return true;
    }

    @Override
    public void init() {
    }

    @Override
    public String fetchMsgId() {
        return null;
    }

}
