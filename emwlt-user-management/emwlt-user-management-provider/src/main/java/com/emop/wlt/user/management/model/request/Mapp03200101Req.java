package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@ToString(callSuper = true)
public class Mapp03200101Req extends FlowBaseReq {

    /**
     * 手机号
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;

    /**
     * 生物识别请求信息
     */
    @NotNull
    private BioAuthReqInfoVO bioAuthReqInfo;

}
