package com.dcep.supergw.dto.dc401;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

/**
 * 
 * 8.3.1 自由格式报文<dcep.401.001.01> FreeFormatInformation 自由格式信息
 * 
 * @author laimincai
 * @version $Id: FreeFrmtInf.java, v 0.1 2019年10月11日 上午10:18:43 laimincai Exp $
 *
 */
@JacksonXmlRootElement(localName = "FreeFrmtInf")
@Setter
@Getter
@ToString
public class FreeFrmtInf implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2304225960457767800L;
	
	public FreeFrmtInf() {}
	
	public FreeFrmtInf(String msgCnt) {
		this.setMsgCnt(msgCnt);
	}

	/**
	 * MessageContent 信息内容
	 */
	@JacksonXmlProperty(localName = "MsgCnt")
	@NotBlank
	@Length(min = 1, max = 1024)
	private String msgCnt;

}
