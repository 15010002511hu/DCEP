package com.emop.wlt.user.management.model.request;


import com.emop.wlt.common.model.flow.FlowBaseReq;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 钱包解挂预校验
 */
@Data
@ToString(callSuper = true)
public class Mapp31700101Req extends FlowBaseReq {

    /**
     * 手机号（带区号）
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;

}
