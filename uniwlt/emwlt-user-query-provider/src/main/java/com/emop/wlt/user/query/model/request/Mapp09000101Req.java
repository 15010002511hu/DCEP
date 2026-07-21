package com.emop.wlt.user.query.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 公管查询-业务自定义参数 请求
 */

@Data
@ToString
public class Mapp09000101Req {

    /**
     * 场景类型
     */
    @NotBlank
    @Pattern(regexp = "ST00|ST01|ST02", message = "场景类型格式错误")
    @Length(max = 5)
    private String sceneType;

    /**
     * 语言
     * ST00时，必传
     */
    @Length(max = 32)
    private String language;

    /**
     * 业务场景
     * ST00时，必传
     */
    @Pattern(regexp = "BT00|BT01", message = "业务场景格式错误")
    private String bizType;

    /**
     * 机构号
     * ST02时，使用
     */
    @Length(min = 6, max = 6)
    private String instNo;

    /**
     * 钱包等级
     * ST02时，使用
     */
    @Pattern(regexp = "WL01|WL02|WL03|WL04", message = "钱包等级格式错误")
    private String walletLevel;

}
