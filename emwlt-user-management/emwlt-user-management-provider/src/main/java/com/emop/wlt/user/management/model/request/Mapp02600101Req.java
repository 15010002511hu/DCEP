package com.emop.wlt.user.management.model.request;

import com.emop.wlt.common.model.flow.FlowBaseReq;
import com.emop.wlt.model.emap.dto.ChineseNameInfo;
import com.emop.wlt.model.emap.dto.EnglishNameInfo;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 实名信息采集（非登录态） 请求报文
 */

@Data
@ToString(callSuper = true)
public class Mapp02600101Req extends FlowBaseReq {

    /**
     * 中文姓名信息
     */
    private ChineseNameInfo chineseNameInfo;

    /**
     * 英文姓名信息
     */
    private EnglishNameInfo englishNameInfo;

    /**
     * 证件号码
     */
    @NotBlank
    @Length(max = 32)
    private String idNumber;

    /**
     * 证件类型
     */
    @NotBlank
    @Pattern(regexp = "IT01|IT02|IT03|IT04")
    private String idType;

}
