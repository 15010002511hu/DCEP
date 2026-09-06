package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.List;

/**
 * 组件--RemittanceInformation
 * @Author qiaopengyu
 * @date 2025-10-30 21:16:50
 */
@JacksonXmlRootElement(localName = "RmtInf")
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RmtInf implements Serializable {

    /*
     * Unstructured附言/PstScrpt/Value
     */
    @JacksonXmlProperty(localName = "PstScrpt")
    @Length(max = 105)
    private String pstScrpt;

    /*
     * Unstructured批次号/BtchId/Value
     */
    @NotBlank
    @JacksonXmlProperty(localName = "BtchId")
    @Length(min = 1, max = 120)
    @Pattern(
            regexp = "[^\u4e00-\u9fa5]*",
            message = "禁止中文"
    )
    private String btchId;

    /**
     * 通过List构造RmtInfo对象不含标签
     * @param ustrds
     */
    public RmtInf(List<String> ustrds){
        for (String ustrd : ustrds){
            if (ustrd.contains("/PstScrpt/")){
                this.setPstScrpt(ustrd.substring("/PstScrpt/".length()));
            }else if (ustrd.contains("/BtchId/")){
                this.setBtchId(ustrd.substring("/BtchId/".length()));
            }
        }
    }
}
