package com.emop.wlt.user.management.model.request;


import com.emop.wlt.common.model.flow.FlowBaseReq;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * 登录
 */

@Data
@ToString(callSuper = true)
public class Mapp10300101Req extends FlowBaseReq {

    /**
     * 设备名称
     */
    @NotBlank
    private String deviceName;

}
