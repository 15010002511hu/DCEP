package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

/**
 * 重置登录密码（非登录态） 请求
 */
@Data
@ToString(callSuper = true)
public class Mapp11200101Req extends FlowBaseReq {

    /**
     * 密码密文
     */
    @NotBlank
    private String pwdEnc;

    /**
     * 设备名称
     */
    @NotBlank
    private String deviceName;

}
