/**
 * DCEP.com.cn Inc.
 * Copyright (c) 2018-2019 All Rights Reserved.
 */
package com.dcep.dips.wholesalepayment.dto.dc20302;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 附言对象
 * 
 * @author liuqi
 * @version $Id: RmtInf.java, v 0.1 2019年8月23日 下午8:14:08 liuqi Exp $
 */
@JacksonXmlRootElement(localName = "RmtInf")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RmtInf implements Serializable {
    /**  */
    private static final long serialVersionUID = 7958024638349539188L;
    /*
     * Unstructured附言/Postscript/Value
     */
    @JacksonXmlProperty(localName = "Postscript")
    @Length(min = 1, max = 140)
    private String postscript;

    /*
     * Unstructured备注/Remark/Value
     */
    @JacksonXmlProperty(localName = "Remark")
    @Length(min = 1, max = 120)
    private String remark;

    /*
     * 申请理由
     */
    @JacksonXmlProperty(localName = "Reason")
    @Length(min = 1, max = 70)
    private String reason;

    /*
     * 发起方系统标识
     */
    @JacksonXmlProperty(localName = "SndSys")
    @Length(min = 1, max = 8)
    @Pattern(regexp = "DCEP||NMTBN||BCSP")
    private String sndChnlSys;

    /*
     * 接收方系统标识
     */
    @JacksonXmlProperty(localName = "RcvSys")
    @Length(min = 1, max = 8)
    @Pattern(regexp = "DCEP||NMTBN||BCSP")
    private String rcvChnlSys;

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
            }  else if (ustrd.contains("/Reason/")) {
                this.setReason(ustrd.substring("/Reason/".length()));
            } else if (ustrd.contains("/SndSys/")) {
                this.setSndChnlSys(ustrd.substring("/SndSys/".length()));
            } else if (ustrd.contains("/RcvSys/")) {
                this.setRcvChnlSys(ustrd.substring("/RcvSys/".length()));
            }
        }
    }

}
