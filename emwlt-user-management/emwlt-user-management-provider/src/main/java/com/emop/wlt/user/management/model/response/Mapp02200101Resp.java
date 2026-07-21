package com.emop.wlt.user.management.model.response;

import com.emop.wlt.common.model.flow.FlowBaseResp;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Mapp02200101Resp extends FlowBaseResp {
    /**
     * 生物识别返回信息
     */
    private String authRespMessage;

}
