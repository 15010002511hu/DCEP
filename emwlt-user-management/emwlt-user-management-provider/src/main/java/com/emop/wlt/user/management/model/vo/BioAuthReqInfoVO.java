package com.emop.wlt.user.management.model.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BioAuthReqInfoVO {

    /**
     * 生物识别请求类型
     */
    private String authActionType;

    /**
     * 生物识别设备id
     */
    private String authDeviceId;

    /**
     *生物识别校验请求报文
     */
    private String authReqMessage;

}
