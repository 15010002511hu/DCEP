package com.emop.wlt.user.query.model.response;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 公管查询-云控参数 应答
 */

@Data
@Builder
@ToString
public class Mapp09300101Resp {

    private String userConfigVersion;

    private JSON config;

    private String userId;

    private String refreshTime;
}
