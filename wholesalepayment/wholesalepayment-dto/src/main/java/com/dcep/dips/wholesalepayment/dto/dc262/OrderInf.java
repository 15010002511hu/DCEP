package com.dcep.dips.wholesalepayment.dto.dc262;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 订单信息
 * 
 * @author chenxf
 *
 */
@JacksonXmlRootElement(localName = "OrderInf")
@Setter
@Getter
@ToString
public class OrderInf implements Serializable {

	private static final long serialVersionUID = 1754171405056340692L;

	/**
	 * 订单号
	 */
	@JacksonXmlProperty(localName = "OrdrNo")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 64)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String ordrNo;

	/**
	 * 订单时间
	 */
	@JacksonXmlProperty(localName = "OrdrTm")
	@NotBlank(groups = Priority.Highest.class)
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
	private String ordrTm;

	/**
	 * 商品名称
	 */
	@JacksonXmlProperty(localName = "GdNm")
	@Length(min = 1, max = 200)
	private String gdNm;

	/**
	 * 订单详情
	 */
	@JacksonXmlProperty(localName = "OrdrDtls")
	@Length(min = 1, max = 4096)
	private String ordrDtls;

	/**
	 * 订单扩展信息
	 */
	@JacksonXmlProperty(localName = "OrdrExtInf")
	@Length(min = 1, max = 1024)
	private String ordrExtInf;
	
	/**
     * 订单有效期
     */
    @JacksonXmlProperty(localName = "OrdrExp")
    @Pattern(regexp = "^\\d{1,15}$", message = "订单有效期请输入有效数字")
    private String            ordrExp;
    
    /**
     * 居民类型
     */
    @JacksonXmlProperty(localName = "ResdtTp")
    @Pattern(regexp = "^[R][T][0-9]{2}||REST[0-9]{2}")
    private String resdtTp;
    
    /**
     * 常驻国家/地区代码
     */
    @JacksonXmlProperty(localName = "ResdtCtryCd")
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String resdtCtryCd;

	/**
	 * BLOCK：拒绝 RESTRICTED：加强验证 PASS：通过
	 */
	@JacksonXmlProperty(localName = "OrdrRiskLvl")
	@NotBlank(groups = Priority.Highest.class)
	@Pattern(regexp = "BLOCK||RESTRICTED||PASS")
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String ordrRiskLvl;

}
