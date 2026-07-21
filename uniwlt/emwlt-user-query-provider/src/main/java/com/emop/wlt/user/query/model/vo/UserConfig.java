package com.emop.wlt.user.query.model.vo;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class UserConfig {

    private String version;

    private JSON config;
}
