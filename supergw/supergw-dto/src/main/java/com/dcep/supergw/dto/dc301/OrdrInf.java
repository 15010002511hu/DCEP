package com.dcep.supergw.dto.dc301;

import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : OrdrInf.java v 0.1 2021-04-13
 * @description : 订单信息
 * 修改说明2021-09-24：商品名称长度由40改为200
 */
public class OrdrInf implements Serializable {
    /**
     * 受理订单生成时间
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    @JacksonXmlProperty(localName = "OutOrdrTm")
    private String outOrdrTm;

    /**
     * 受理订单号
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 64)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OutOrdrNo")
    private String outOrdrNo;

    /**
     * 商户订单号
     */
    @Length(min = 1, max = 64)
    @JacksonXmlProperty(localName = "MrchntOrdrNo")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    private String mrchntOrdrNo;

    /**
     * 商品名称
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 200)
    @JacksonXmlProperty(localName = "GdNm")
    private String gdNm;

    /**
     * 订单详情
     */
    @Length(min = 1, max = 4096)
    @JacksonXmlProperty(localName = "OrdrDtls")
    private String ordrDtls;

    /**
     * 网络交易平台简称
     * 修改说明2021-09-29：长度改为40
     */
    @Length(min = 1, max = 40)
    @JacksonXmlProperty(localName = "PltfrmNm")
    private String pltfrmNm;

    /**
     * 交易地点
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "TrxPlce")
    private String trxPlce;

    /**
     * 订单失效时间
     */
    @NotBlank(groups = Priority.Highest.class)
    @Length(min = 1, max = 35)
    @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}(T)\\d{2}:\\d{2}:\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd'T'HH:mm:ss")
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @JacksonXmlProperty(localName = "OrdrTmExp")
    private String ordrTmExp;

    public String getOutOrdrTm() {
        return outOrdrTm;
    }

    public void setOutOrdrTm(String outOrdrTm) {
        this.outOrdrTm = outOrdrTm;
    }

    public String getOutOrdrNo() {
        return outOrdrNo;
    }

    public void setOutOrdrNo(String outOrdrNo) {
        this.outOrdrNo = outOrdrNo;
    }

    public String getGdNm() {
        return gdNm;
    }

    public void setGdNm(String gdNm) {
        this.gdNm = gdNm;
    }

    public String getOrdrDtls() {
        return ordrDtls;
    }

    public void setOrdrDtls(String ordrDtls) {
        this.ordrDtls = ordrDtls;
    }

    public String getPltfrmNm() {
        return pltfrmNm;
    }

    public void setPltfrmNm(String pltfrmNm) {
        this.pltfrmNm = pltfrmNm;
    }

    public String getTrxPlce() {
        return trxPlce;
    }

    public void setTrxPlce(String trxPlce) {
        this.trxPlce = trxPlce;
    }

    public String getOrdrTmExp() {
        return ordrTmExp;
    }

    public void setOrdrTmExp(String ordrTmExp) {
        this.ordrTmExp = ordrTmExp;
    }

    public String getMrchntOrdrNo() {
        return mrchntOrdrNo;
    }

    public void setMrchntOrdrNo(String mrchntOrdrNo) {
        this.mrchntOrdrNo = mrchntOrdrNo;
    }

    @Override
    public String toString() {
        return "OrdrInf{" +
                "outOrdrTm='" + outOrdrTm + '\'' +
                ", outOrdrNo='" + outOrdrNo + '\'' +
                ", mrchntOrdrNo='" + mrchntOrdrNo + '\'' +
                ", gdNm='" + gdNm + '\'' +
                ", ordrDtls='" + ordrDtls + '\'' +
                ", pltfrmNm='" + pltfrmNm + '\'' +
                ", trxPlce='" + trxPlce + '\'' +
                ", ordrTmExp='" + ordrTmExp + '\'' +
                '}';
    }
}
