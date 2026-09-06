package com.dcep.supergw.dto.dc507;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 用户信息
 * @author qinchaoyong
 */
@Data
public class UserInf implements Serializable {

    private static final long serialVersionUID = -5616517796337085255L;

    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "UserUniqId")
    private String userUniqId;


}
