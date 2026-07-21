package com.emop.wlt.user.query.model.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Protocol {

    /**
     * 协议版本号
     */
    private String releaseId;

    /**
     * 协议具体的key及value
     */
    private JSONObject content;

    private String updateTime;

}
