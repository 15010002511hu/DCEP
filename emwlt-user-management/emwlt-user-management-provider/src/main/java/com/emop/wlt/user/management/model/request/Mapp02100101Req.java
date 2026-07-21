package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@Data
@ToString(callSuper = true)
public class Mapp02100101Req extends FlowBaseReq {

    /**
     * 手机号
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;
}
