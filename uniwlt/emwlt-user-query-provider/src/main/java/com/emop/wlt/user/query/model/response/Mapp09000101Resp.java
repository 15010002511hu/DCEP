package com.emop.wlt.user.query.model.response;

import com.alibaba.fastjson.JSONObject;
import com.emop.wlt.user.query.model.vo.Common;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 公管查询-业务自定义参数 应答
 */

@Data
@Builder
@ToString
public class Mapp09000101Resp {

    /**
     * 业务参数
     */

    private String content;

    /**
     * 当前时间
     */
    private String currentDateTime;
}
