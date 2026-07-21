package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 重置登录密码预校验（非登录态） 请求
 */
@Data
@ToString(callSuper = true)
public class Mapp11100101Req extends FlowBaseReq {

    /**
     * 手机号（带区号）
     */
    @NotBlank
    @Length(max = 35)
    private String mobileNumber;
}
