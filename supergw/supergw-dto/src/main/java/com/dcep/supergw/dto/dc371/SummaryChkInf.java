package com.dcep.supergw.dto.dc371;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.validator.Priority;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

/**
 * @author : maxinyu
 * @version : SummaryChkInf.java v 0.1 2021-04-14
 * @description : 对账汇总核对信息
 */
public class SummaryChkInf implements Serializable {

    @Valid
    @NotNull
    @JacksonXmlProperty(localName = "SummaryHdr")
    private SummaryHdr summaryHdr;

    @Valid
    @JacksonXmlProperty(localName = "DtlFileInf")
    private DtlFileInf dtlFileInf;

    public SummaryHdr getSummaryHdr() {
        return summaryHdr;
    }

    public void setSummaryHdr(SummaryHdr summaryHdr) {
        this.summaryHdr = summaryHdr;
    }

    public DtlFileInf getDtlFileInf() {
        return dtlFileInf;
    }

    public void setDtlFileInf(DtlFileInf dtlFileInf) {
        this.dtlFileInf = dtlFileInf;
    }

    @Override
    public String toString() {
        return "SummaryChkInf{" +
                "summaryHdr=" + summaryHdr +
                ", dtlFileInf=" + dtlFileInf +
                '}';
    }

    public class SummaryHdr {
        /**
         * 对账日期
         */
        @NotBlank(groups = Priority.Highest.class)
        @Pattern(regexp = "(\\d{4})-\\d{2}-\\d{2}", message = "日期格式错误，正确格式是：yyyy-MM-dd")
        @JacksonXmlProperty(localName = "ChkDt")
        private String chkDt;

        /**
         * 总笔数
         */
        @NotBlank(groups = Priority.Highest.class)
        @Length(min = 1, max = 15)
        @Pattern(regexp = "^[1-9]\\d*|0$", message = "总笔数应为非负整数")
        @JacksonXmlProperty(localName = "CntNb")
        private String cntNb;

        /**
         * 总金额
         */
        @Valid
        @NotNull
        @JacksonXmlProperty(localName = "CntAmt")
        private ActiveCurrencyAndAmount cntAmt;

        /**
         * 支付总笔数
         */
        @Length(min = 1, max = 15)
        @Pattern(regexp = "^[1-9]\\d*|0$", message = "支付总笔数应为非负整数")
        @JacksonXmlProperty(localName = "PmtCntNb")
        private String pmtCntNb;

        /**
         * 支付总金额
         */
        @Valid
        @JacksonXmlProperty(localName = "PmtCntAmt")
        private ActiveCurrencyAndAmount pmtCntAmt;

        /**
         * 退款总笔数
         */

        @Length(min = 1, max = 15)
        @Pattern(regexp = "^[1-9]\\d*|0$", message = "退款总笔数应非负正整数")
        @JacksonXmlProperty(localName = "RefCntNb")
        private String refCntNb;

        /**
         * 总金额
         */
        @Valid
        @JacksonXmlProperty(localName = "RefCntAmt")
        private ActiveCurrencyAndAmount refCntAmt;


        public String getChkDt() {
            return chkDt;
        }

        public void setChkDt(String chkDt) {
            this.chkDt = chkDt;
        }

        public String getCntNb() {
            return cntNb;
        }

        public void setCntNb(String cntNb) {
            this.cntNb = cntNb;
        }

        public ActiveCurrencyAndAmount getCntAmt() {
            return cntAmt;
        }

        public void setCntAmt(ActiveCurrencyAndAmount cntAmt) {
            this.cntAmt = cntAmt;
        }

        public String getPmtCntNb() {
            return pmtCntNb;
        }

        public void setPmtCntNb(String pmtCntNb) {
            this.pmtCntNb = pmtCntNb;
        }

        public ActiveCurrencyAndAmount getPmtCntAmt() {
            return pmtCntAmt;
        }

        public void setPmtCntAmt(ActiveCurrencyAndAmount pmtCntAmt) {
            this.pmtCntAmt = pmtCntAmt;
        }

        public String getRefCntNb() {
            return refCntNb;
        }

        public void setRefCntNb(String refCntNb) {
            this.refCntNb = refCntNb;
        }

        public ActiveCurrencyAndAmount getRefCntAmt() {
            return refCntAmt;
        }

        public void setRefCntAmt(ActiveCurrencyAndAmount refCntAmt) {
            this.refCntAmt = refCntAmt;
        }

        @Override
        public String toString() {
            return "SummaryHdr{" +
                    "chkDt='" + chkDt + '\'' +
                    ", cntNb='" + cntNb + '\'' +
                    ", cntAmt=" + cntAmt +
                    ", pmtCntNb='" + pmtCntNb + '\'' +
                    ", pmtCntAmt=" + pmtCntAmt +
                    ", refCntNb='" + refCntNb + '\'' +
                    ", refCntAmt=" + refCntAmt +
                    '}';
        }
    }


    public class DtlFileInf {

        /**
         * 总文件数
         */
        @NotBlank(groups = Priority.Highest.class)
        @Length(min = 1,max = 10)
        @Pattern(regexp = "^[1-9]\\d*|0$", message = "总文件数应为非负整数")
        @JacksonXmlProperty(localName = "FileInfNb")
        private String fileInfNb;

        /**
         * 文件列表
         */
        @Valid
        @NotNull
        @JacksonXmlElementWrapper(localName = "FileInfList")
        @JacksonXmlProperty(localName = "FileInf")
        private List<FileInf> fileInfList;

        public String getFileInfNb() {
            return fileInfNb;
        }

        public void setFileInfNb(String fileInfNb) {
            this.fileInfNb = fileInfNb;
        }

        public List<FileInf> getFileInfList() {
            return fileInfList;
        }

        public void setFileInfList(List<FileInf> fileInfList) {
            this.fileInfList = fileInfList;
        }

        @Override
        public String toString() {
            return "DtFileInf{" +
                    "fileInfNb='" + fileInfNb + '\'' +
                    ", fileInfList=" + fileInfList +
                    '}';
        }
    }

    public static class FileInf {
        /**
         * 文件路径
         */
        @NotBlank(groups = Priority.Highest.class)
        @Length(min = 1,max = 64)
        @JacksonXmlProperty(localName = "FilePath")
        private String filePath;

        /**
         * 文件名称列表
         */
        @JacksonXmlElementWrapper(localName = "FileNameList")
        @JacksonXmlProperty(localName = "FileName")
        @Valid
        private List<String> fileNameList;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public List<String> getFileNameList() {
            return fileNameList;
        }

        public void setFileNameList(List<String> fileNameList) {
            this.fileNameList = fileNameList;
        }

        @Override
        public String toString() {
            return "FileInf{" +
                    "filePath='" + filePath + '\'' +
                    ", fileNameList=" + fileNameList +
                    '}';
        }
    }
}
