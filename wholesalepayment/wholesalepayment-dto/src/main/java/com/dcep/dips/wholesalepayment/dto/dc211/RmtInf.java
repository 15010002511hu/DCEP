package com.dcep.dips.wholesalepayment.dto.dc211;

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
import java.util.List;

/**
 * 
 * @author sunxiaofeng
 *
 */
@JacksonXmlRootElement(localName = "RmtInf")
@Setter
@Getter
@ToString
public class RmtInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8080193621631960466L;

	@JacksonXmlProperty(localName = "Postscript")
	@Length(min = 1, max = 120)
	private String postscript;

	@JacksonXmlProperty(localName = "Remark")
	@Length(min = 1, max = 120)
	private String remark;

	/**
	 * 认证方式 /AuthCode/Value AC00：协议方式 AC01：在线认证方式 AC02：动态密码方式 AC03：短信认证方式
	 */
	@JacksonXmlProperty(localName = "AuthCode")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 4, max = 4)
	@Pattern(regexp = "^[A][C][0-9]{2}||VM00")
	private String authCode;

	@JacksonXmlProperty(localName = "AuthInfo")
	@NotBlank(groups = Priority.Highest.class)
	@Length(min = 1, max = 130)
	private String authInfo;
	
	/**
     * 商户属性
     */
    @JacksonXmlProperty(localName = "MrchntPrprty")
    @Pattern(regexp = "^[M][P][0-9]{2}")
    private String mrchntPrprty;

	/**
	 * 商户号
	 */
	@JacksonXmlProperty(localName = "MrchntNo")
	@Length(min = 1, max = 35)
	private String mrchntNo;

	/**
	 * 商户名称
	 */
	@JacksonXmlProperty(localName = "MrchntNm")
	@Length(min = 1, max = 60)
	private String mrchntNm;

	/**
	 * 商户简称
	 */
	@JacksonXmlProperty(localName = "MrchntAbbrNm")
	@Length(min = 1, max = 30)
	private String mrchntAbbrNm;

	/**
	 * 商品名称
	 */
	@JacksonXmlProperty(localName = "GdNm")
	@Length(min = 1, max = 200)
	private String gdNm;

	/**
	 * 订单号
	 */
	@JacksonXmlProperty(localName = "OrdrNo")
	@Length(min = 1, max = 64)
	private String ordrNo;

	/**
	 * 订单时间
	 */
	@JacksonXmlProperty(localName = "OrdrTm")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd'T'HH:mm:ss")
	@Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
	private String ordrTm;

	/**
	 * 商户证件类型
	 */
	@JacksonXmlProperty(localName = "MrchntIdTp")
	@Pattern(regexp = "^[I][T][0-9]{2}")
	private String mrchntIdTp;

	/**
	 * 商户证件编码
	 */
	@JacksonXmlProperty(localName = "MrchntIdNo")
	@Length(min = 1, max = 32)
	private String mrchntIdNo;

	/**
	 * 场景ID
	 */
	@JacksonXmlProperty(localName = "SceneId")
	@Length(min = 1, max = 10)
	private String sceneId;
	
	/**
     * 商户类别代码
     */
    @JacksonXmlProperty(localName = "MCC")
    @Length(min = 1, max = 4)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mcc;

	/**
	 * 受理服务机构金融编码
	 */
	@JacksonXmlProperty(localName = "AcqAgtInstnId")
	@Length(min = 1, max = 14)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String acqAgtInstnId;

	/**
	 * 受理服务机构名称
	 */
	@JacksonXmlProperty(localName = "AcqAgtNm")
	@Length(min = 1, max = 60)
	private String acqAgtNm;
	
	/**
     * 受理订单号
     */
    @JacksonXmlProperty(localName = "OutOrdrNo")
    @Length(min = 1, max = 64)
    private String outOrdrNo;
    
    /**
     * 商户订单号
     */
    @JacksonXmlProperty(localName = "MrchntOrdrNo")
    @Length(min = 1, max = 64)
    private String mrchntOrdrNo;

	/**
	 * 二级商户编码
	 */
	@JacksonXmlProperty(localName = "SubMrchntNo")
	@Length(min = 1, max = 35)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String subMrchntNo;

	/**
	 * 二级商户钱包id
	 */
	@JacksonXmlProperty(localName = "SubMrchntWltId")
	@Length(min = 16, max = 16)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String subMrchntWltId;

	/**
	 * 二级商户名称
	 */
	@JacksonXmlProperty(localName = "SubMrchntNm")
	@Length(min = 1, max = 60)
	private String subMrchntNm;

	/**
	 * 二级商户简称
	 */
	@JacksonXmlProperty(localName = "SubMrchntAbbrNm")
	@Length(min = 1, max = 30)
	private String subMrchntAbbrNm;

	/**
	 * 二级商户类别代码
	 */
	@JacksonXmlProperty(localName = "SubMCC")
	@Length(min = 1, max = 4)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String subMCC;

	/**
	 * 二级商户证件类型
	 */
	@JacksonXmlProperty(localName = "SubMrchntIdTp")
	@Pattern(regexp = "^[I][T][0-9]{2}")
	private String subMrchntIdTp;

	/**
	 * 二级商户证件编码
	 */
	@JacksonXmlProperty(localName = "SubMrchntIdNo")
	@Length(min = 1, max = 32)
	@Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
	private String subMrchntIdNo;

	/**
	 * 二级商户订单详情
	 */
	@JacksonXmlProperty(localName = "OrdrDtls")
	@Length(min = 1, max = 4096)
	private String ordrDtls;

	/**
     * 受理终端编号
     */
    @JacksonXmlProperty(localName = "TerNo")
    @Length(min = 1, max = 32)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            terNo;

    /**
     * 受理终端地址位置
     */
    @JacksonXmlProperty(localName = "TerLctnInf")
    @Length(min = 1, max = 128)
    private String            terLctnInf;

    /**
     * 网络交易平台名称(商户属性为MP02或MP03时必填)
     */
    @JacksonXmlProperty(localName = "PltfrmNm")
    @Length(min = 1, max = 40)
    private String            pltfrmNm;

    /**
     * 商户经营地址(商户属性为MP01或MP03时必填)
     */
    @JacksonXmlProperty(localName = "MrchntBizAddr")
    @Length(min = 1, max = 128)
    private String            mrchntBizAddr;

    /**
     * 受理终端设备信息
     */
    @JacksonXmlProperty(localName = "TerDevcInf")
    @Length(min = 1, max = 149)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String            terDevcInf;

	/**
     * 居民类型
     */
    @JacksonXmlProperty(localName = "ResdtTp")
	@NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "^[R][T][0-9]{2}||REST[0-9]{2}")
    private String resdtTp;
    
    /**
     * 常驻国家/地区代码
     */
    @JacksonXmlProperty(localName = "ResdtCtryCd")
	@NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 3)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String resdtCtryCd;

	public RmtInf() {

	}

	/**
	 * 通过List构造RmtInf对象，不含标签
	 * 
	 * @param ustrds
	 */
	public RmtInf(List<String> ustrds) {
		for (String ustrd : ustrds) {
			if (ustrd.contains("/Postscript/")) {
				this.setPostscript(ustrd.substring("/Postscript/".length()));
			} else if (ustrd.contains("/Remark/")) {
				this.setRemark(ustrd.substring("/Remark/".length()));
			} else if (ustrd.contains("/AuthCode/")) {
				this.setAuthCode(ustrd.substring("/AuthCode/".length()));
			} else if (ustrd.contains("/AuthInfo/")) {
				this.setAuthInfo(ustrd.substring("/AuthInfo/".length()));
			} else if (ustrd.contains("/MrchntPrprty/")) {
                this.setMrchntPrprty(ustrd.substring("/MrchntPrprty/".length()));
            } else if (ustrd.contains("/MrchntNo/")) {
				this.setMrchntNo(ustrd.substring("/MrchntNo/".length()));
			} else if (ustrd.contains("/MrchntNm/")) {
				this.setMrchntNm(ustrd.substring("/MrchntNm/".length()));
			} else if (ustrd.contains("/GdNm/")) {
				this.setGdNm(ustrd.substring("/GdNm/".length()));
			} else if (ustrd.contains("/OrdrNo/")) {
				this.setOrdrNo(ustrd.substring("/OrdrNo/".length()));
			} else if (ustrd.contains("/OrdrTm/")) {
				this.setOrdrTm(ustrd.substring("/OrdrTm/".length()));
			} else if (ustrd.contains("/MrchntIdTp/")) {
				this.setMrchntIdTp(ustrd.substring("/MrchntIdTp/".length()));
			} else if (ustrd.contains("/MrchntIdNo/")) {
				this.setMrchntIdNo(ustrd.substring("/MrchntIdNo/".length()));
			} else if (ustrd.contains("/SceneId/")) {
				this.setSceneId(ustrd.substring("/SceneId/".length()));
			} else if (ustrd.contains("/MCC/")) {
                this.setMcc(ustrd.substring("/MCC/".length()));
            } else if (ustrd.contains("/MrchntAbbrNm/")) {
				this.setMrchntAbbrNm(ustrd.substring("/MrchntAbbrNm/".length()));
			} else if (ustrd.contains("/AcqAgtInstnId/")) {
				this.setAcqAgtInstnId(ustrd.substring("/AcqAgtInstnId/".length()));
			} else if (ustrd.contains("/AcqAgtNm/")) {
				this.setAcqAgtNm(ustrd.substring("/AcqAgtNm/".length()));
			} else if (ustrd.contains("/OutOrdrNo/")) {
                this.setOutOrdrNo(ustrd.substring("/OutOrdrNo/".length()));
            } else if (ustrd.contains("/MrchntOrdrNo/")) {
                this.setMrchntOrdrNo(ustrd.substring("/MrchntOrdrNo/".length()));
            } else if (ustrd.contains("/SubMrchntNo/")) {
				this.setSubMrchntNo(ustrd.substring("/SubMrchntNo/".length()));
			} else if (ustrd.contains("/SubMrchntWltId/")) {
				this.setSubMrchntWltId(ustrd.substring("/SubMrchntWltId/".length()));
			} else if (ustrd.contains("/SubMrchntNm/")) {
				this.setSubMrchntNm(ustrd.substring("/SubMrchntNm/".length()));
			} else if (ustrd.contains("/SubMrchntAbbrNm/")) {
				this.setSubMrchntAbbrNm(ustrd.substring("/SubMrchntAbbrNm/".length()));
			} else if (ustrd.contains("/SubMCC/")) {
				this.setSubMCC(ustrd.substring("/SubMCC/".length()));
			} else if (ustrd.contains("/SubMrchntIdTp/")) {
				this.setSubMrchntIdTp(ustrd.substring("/SubMrchntIdTp/".length()));
			} else if (ustrd.contains("/SubMrchntIdNo/")) {
				this.setSubMrchntIdNo(ustrd.substring("/SubMrchntIdNo/".length()));
			} else if (ustrd.contains("/OrdrDtls/")) {
				this.setOrdrDtls(ustrd.substring("/OrdrDtls/".length()));
			} else if (ustrd.contains("/TerNo/")) {
                this.setTerNo(ustrd.substring("/TerNo/".length()));
            } else if (ustrd.contains("/TerLctnInf/")) {
                this.setTerLctnInf(ustrd.substring("/TerLctnInf/".length()));
            } else if (ustrd.contains("/PltfrmNm/")) {
                this.setPltfrmNm(ustrd.substring("/PltfrmNm/".length()));
            } else if (ustrd.contains("/MrchntBizAddr/")) {
                this.setMrchntBizAddr(ustrd.substring("/MrchntBizAddr/".length()));
            } else if (ustrd.contains("/TerDevcInf/")) {
                this.setTerDevcInf(ustrd.substring("/TerDevcInf/".length()));
            } else if (ustrd.contains("/ResdtTp/")) {
                this.setResdtTp(ustrd.substring("/ResdtTp/".length()));
            } else if (ustrd.contains("/ResdtCtryCd/")) {
                this.setResdtCtryCd(ustrd.substring("/ResdtCtryCd/".length()));
            }
		}
	}

}
