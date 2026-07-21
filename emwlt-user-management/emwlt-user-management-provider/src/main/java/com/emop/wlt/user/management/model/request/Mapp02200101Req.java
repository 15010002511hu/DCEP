package com.emop.wlt.user.management.model.request;


import com.emop.wlt.common.model.flow.FlowBaseReq;
import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
import lombok.Data;
import lombok.ToString;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;


@Data
@ToString(callSuper = true)
public class Mapp02200101Req extends FlowBaseReq {

    /**
     * 手机号
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;

    /**
     * 验证码
     */
    @Length(max = 6)
    @NotBlank
    private String verifyCode;

    /**
     * 生物识别请求信息
     */
    private BioAuthReqInfoVO bioAuthReqInfo;
}
