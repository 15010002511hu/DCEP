package com.emop.wlt.user.query.model.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Common {

    /**
     * 具体的key及value
     */
    private JSONObject content;

}
