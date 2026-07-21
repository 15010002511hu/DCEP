package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import javax.validation.constraints.NotBlank;

import com.emop.wlt.user.management.model.vo.BioAuthReqInfoVO;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@ToString(callSuper = true)
public class Mapp03000101Req extends FlowBaseReq {

    /**
     * 密码密文
     */
    @Length(max = 512)
    @NotBlank
    private String pwdEnc;

    /**
     * 手机号
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;

    /**
     * 生物识别请求信息
     */
    private BioAuthReqInfoVO bioAuthReqInfo;
}
