package com.emop.wlt.user.query.model.response;

import com.alibaba.fastjson.JSONObject;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 公管查询-银行卡信息参数 应答
 */

@Data
@Builder
@ToString
public class Mapp09400101Resp {

    /**
     * 绑卡，各个银行的详细信息
     */
    private JSONObject bindCardDetailBanksInfo;

    /**
     * 是否更新
     */
    private String uptodate;

    /**
     * 配置信息版本号
     */
    private String version;
}
