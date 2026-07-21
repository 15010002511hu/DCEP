package com.emop.wlt.user.management.model.request;


import com.emop.wlt.common.model.flow.FlowBaseReq;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 注册并登录
 */

@Data
@ToString(callSuper = true)
public class Mapp10100101Req extends FlowBaseReq {

    /**
     * 手机号
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;
}
