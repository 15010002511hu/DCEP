package com.emop.wlt.user.management.model.response;

import com.emop.wlt.common.model.flow.FlowBaseResp;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class Mapp03200101Resp extends FlowBaseResp {

    private String authRespMessage;
}

