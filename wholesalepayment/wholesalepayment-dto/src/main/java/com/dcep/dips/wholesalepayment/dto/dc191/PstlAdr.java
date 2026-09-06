package com.dcep.dips.wholesalepayment.dto.dc191;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 组件------PostalAddress
 * @Author qiaopengyu
 * @date 2025-10-30 21:17:01
 */
@Data
public class PstlAdr implements Serializable {
    private static final long serialVersionUID = 8558416577022961977L;
    /**
     * StreetName
     */
    @JacksonXmlProperty(
            localName = "StrtNm"
    )
    @Length(min = 1, max = 70)
    private String strtNm;

    /**
     * BuildingNumber
     */
    @JacksonXmlProperty(
            localName = "BldgNb"
    )
    @Length(min = 1, max = 16)
    private String bldgNb;

    /**
     * BuildingName
     */
    @JacksonXmlProperty(
            localName = "BldgNm"
    )
    @Length(min = 1, max = 35)
    private String bldgNm;

    /**
     * Floor
     */
    @JacksonXmlProperty(
            localName = "Flr"
    )
    @Length(min = 1, max = 70)
    private String flr;

    /**
     * PostBox
     */
    @JacksonXmlProperty(
            localName = "PstBx"
    )
    @Length(min = 1, max = 16)
    private String pstBx;

    /**
     * Room
     */
    @JacksonXmlProperty(
            localName = "Room"
    )
    @Length(min = 1, max = 70)
    private String room;

    /**
     * PostCode
     */
    @JacksonXmlProperty(
            localName = "PstCd"
    )
    @Length(min = 1, max = 16)
    private String pstCd;

    /**
     * TownName
     */
    @JacksonXmlProperty(
            localName = "TwnNm"
    )
    @Length(min = 1, max = 35)
    private String twnNm;

    /**
     * TownLocationName
     */
    @JacksonXmlProperty(
            localName = "TwnLctnNm"
    )
    @Length(min = 1, max = 35)
    private String twnLctnNm;

    /**
     * DistrictName
     */
    @JacksonXmlProperty(
            localName = "DstrctNm"
    )
    @Length(min = 1, max = 35)
    private String dstrctNm;

    /**
     * CountrySubDivision
     */
    @JacksonXmlProperty(
            localName = "CtrySubDvsn"
    )
    @Length(min = 1, max = 35)
    private String ctrySubDvsn;

    /**
     * 付款方所在国家
     */
    @JacksonXmlProperty(
            localName = "Ctry"
    )
    @Length(min = 1, max = 35)
    private String ctry;
}
