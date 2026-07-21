package com.emop.wlt.user.query.model.vo;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Element {

    /**
     * 页面元素版本号
     */
    private String releaseId;

    /**
     * 页面元素具体的key及value
     */
    private JSONObject content;

    private String updateTime;
}
