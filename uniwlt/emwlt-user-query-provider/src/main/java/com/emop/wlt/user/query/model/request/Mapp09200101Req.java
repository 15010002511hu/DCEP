package com.emop.wlt.user.query.model.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 公管查询-通用业务参数 请求
 */

@Data
@ToString
public class Mapp09200101Req {

    /**
     * 系统
     */
    @NotBlank
    @Length(max = 32)
    @Pattern(regexp = "iOS|android")
    private String platform;
}
