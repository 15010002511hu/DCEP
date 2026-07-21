package com.emop.wlt.user.management.model.request;


import com.emop.wlt.common.model.flow.FlowBaseReq;
import com.emop.wlt.user.management.model.vo.Geolocation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 注册准入条件校验
 */

@Data
@ToString(callSuper = true)
public class Mapp10000101Req extends FlowBaseReq {

    /**
     * 手机号（带区号）
     */
    @Length(max = 35)
    @NotBlank
    private String mobileNumber;

    /**
     * 国家和地区代码
     */
    @Length(max = 20)
    @NotBlank
    private String countryAndRegionCode;

    /**
     * 地理位置
     */
//    @NotNull todo
    private Geolocation geolocation;

}
