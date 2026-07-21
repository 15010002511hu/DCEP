package com.emop.wlt.user.query.model.response;

import com.emop.wlt.user.query.model.vo.Common;
import com.emop.wlt.user.query.model.vo.Element;
import com.emop.wlt.user.query.model.vo.Protocol;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 公管查询-通用业务参数 应答
 */

@Data
@Builder
@ToString
public class Mapp09200101Resp {

    private Common common;
}
