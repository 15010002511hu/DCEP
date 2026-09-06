package com.dcep.supergw.dto.dc536;

import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
@Getter
@Setter
@ToString
/**
 * 修改记录2021-09-08:修改手续费费率为非必输
 * 修改记录2021-09-24：修改“信用卡取现手续费”改为必输,修改字段类型
 */
public class CardInf implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 603373046592776952L;

	/**
     *信用卡取现手续费费率
     */
    @JacksonXmlProperty(localName = "CdtCardWdrRate")
    @Length(min = 1, max = 8)
    private String cdtCardWdrRate;
    
	/**
     * 信用卡取现手续费
     */
    @Valid
    @JacksonXmlProperty(localName = "CdtCardWdrChrg")
    @NotNull
    private ActiveCurrencyAndAmount cdtCardWdrChrg;
}
