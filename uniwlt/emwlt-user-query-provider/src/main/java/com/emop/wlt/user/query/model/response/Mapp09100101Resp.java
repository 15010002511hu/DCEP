package com.emop.wlt.user.query.model.response;

import com.emop.wlt.user.query.model.vo.Element;
import com.emop.wlt.user.query.model.vo.Protocol;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 公管查询-多语言参数和协议参数 应答
 */

@Data
@Builder
@ToString
public class Mapp09100101Resp {

    private String language;

    private String appVersion;

    private Element element;

    private Protocol protocol;
}
