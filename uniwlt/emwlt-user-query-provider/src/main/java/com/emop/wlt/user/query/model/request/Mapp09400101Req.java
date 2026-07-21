package com.emop.wlt.user.query.model.request;

import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 公管查询-银行卡信息参数 请求
 */

@Data
@ToString
public class Mapp09400101Req {

    /**
     * 当前配置信息版本号
     */
    @Length(max = 16)
    private String version;

    /**
     * 语言标识
     */
    @Length(max = 32)
    private String language;

}
