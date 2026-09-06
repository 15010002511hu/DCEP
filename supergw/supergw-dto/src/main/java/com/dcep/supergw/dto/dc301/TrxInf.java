package com.dcep.supergw.dto.dc301;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : TrxInf.java v 0.1 2021-04-13
 * @description :
 */
public class TrxInf implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5980148994950460146L;

    /**
     * 交易类型
     * TT00-TT04
     * 禁止中文
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Pattern(regexp = "TT[0-9]{2}")
    @Length(min = 4, max = 4)
    @JacksonXmlProperty(localName = "TrxTp")
    private String trxTp;

    /**
     * 下单类型
     * 禁止中文
     */
    @Pattern(regexp = "COT01||COT02||COT03")
    @JacksonXmlProperty(localName = "CreOrdrTp")
    private String creOrdrTp;

    /**
     * 订单金额
     */
    @Valid
    @NotNull(groups = Priority.Highest.class)
    @JacksonXmlProperty(localName = "TrxAmt")
    private ActiveCurrencyAndAmount trxAmt;

    /**
     * 业务类型编码
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1,max = 4)
    @JacksonXmlProperty(localName = "TrxBizTp")
    private String trxBizTp;

    /**
     * 业务种类编码
     */
    @NotBlank(groups = Priority.Highest.class)
    @Pattern(regexp = "[^\u4e00-\u9fa5]*", message = "禁止中文")
    @Length(min = 1,max = 8)
    @JacksonXmlProperty(localName = "TrxCtgyCd")
    private String trxCtgyCd;

    /**
     * H5订单页类型
     */
    @Pattern(regexp = "QET[0-9]{2}")
    @Length(min = 1,max = 5)
    @JacksonXmlProperty(localName = "H5OrdrPgTp")
    private String h5OrdrPgTp;

    @Length(min = 1, max = 128)
    @JacksonXmlProperty(localName = "TrxRmk")
    private String trxRmk;

    @Pattern(regexp = "CT[0-9]{2}")
    @Length(min = 1, max = 4)
    @JacksonXmlProperty(localName = "ChnlTp")
    private String chnlTp;

    public String getTrxTp() {
        return trxTp;
    }

    public void setTrxTp(String trxTp) {
        this.trxTp = trxTp;
    }

    public String getCreOrdrTp() {
        return creOrdrTp;
    }

    public void setCreOrdrTp(String creOrdrTp) {
        this.creOrdrTp = creOrdrTp;
    }

    public ActiveCurrencyAndAmount getTrxAmt() {
        return trxAmt;
    }

    public void setTrxAmt(ActiveCurrencyAndAmount trxAmt) {
        this.trxAmt = trxAmt;
    }

    public String getTrxBizTp() {
        return trxBizTp;
    }

    public void setTrxBizTp(String trxBizTp) {
        this.trxBizTp = trxBizTp;
    }

    public String getTrxCtgyCd() {
        return trxCtgyCd;
    }

    public void setTrxCtgyCd(String trxCtgyCd) {
        this.trxCtgyCd = trxCtgyCd;
    }

    public String getH5OrdrPgTp() {
        return h5OrdrPgTp;
    }

    public void setH5OrdrPgTp(String h5OrdrPgTp) {
        this.h5OrdrPgTp = h5OrdrPgTp;
    }

    public String getTrxRmk() {
        return trxRmk;
    }

    public void setTrxRmk(String trxRmk) {
        this.trxRmk = trxRmk;
    }

    public String getChnlTp() {
        return chnlTp;
    }

    public void setChnlTp(String chnlTp) {
        this.chnlTp = chnlTp;
    }

    @Override
    public String toString() {
        return "TrxInf{" +
            "trxTp='" + trxTp + '\'' +
            ", creOrdrTp='" + creOrdrTp + '\'' +
            ", trxAmt=" + trxAmt +
            ", trxBizTp='" + trxBizTp + '\'' +
            ", trxCtgyCd='" + trxCtgyCd + '\'' +
            ", h5OrdrPgTp='" + h5OrdrPgTp + '\'' +
            ", trxRmk='" + trxRmk + '\'' +
            ", chnlTp='" + chnlTp + '\'' +
            '}';
    }
}
