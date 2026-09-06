package com.dcep.supergw.common.utils;

import com.dcep.clearing.dto.dc201.Dcep20100101DTO;
import com.dcep.common.model.soap.SoapHeader;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author : maxinyu
 * @version : XmlUtilsTest.java v 0.1 2019-11-13
 * @description :
 */
@SpringBootTest
@RunWith(JMockit.class)
public class XmlUtilsTest {

    /**
     * 测试构造方法
     */
    @Test
    public void testConstruct() {
        XmlUtils xmlUtils = new XmlUtils();
    }

    /**
     * 测试xml字符串转换为Object对象
     */
    @Test
    public void testxmlToObject() {
        String xml = "<SoapHeader>\n" +
            "        <Ver>01</Ver>\n" +
            "        <SndDtTm>2019-08-19T09:25:43</SndDtTm>\n" +
            "        <MsgTp>dcep.461.001.01</MsgTp>\n" +
            "        <MsgSN>201908090041252000000000000900010001</MsgSN>\n" +
            "        <Sender>C1010411000013</Sender>\n" +
            "        <Receiver>C1010211000012</Receiver>\n" +
            "        <SignSN>3</SignSN>\n" +
            "        <NcrptnSN>3</NcrptnSN>\n" +
            "        <DgtlEnvlp>3</DgtlEnvlp>\n" +
            "    </SoapHeader>";

        Object obj = null;
        try {
            obj = XmlUtils.xmlToObject(xml, SoapHeader.class);
        } catch (Exception e) {

        }
    }

    /**
     * 测试xml转obj，异常分支
     */
    @Test(expected = Exception.class)
    public void testmlToObjectThrowException() throws Exception {
        new MockUp<ObjectMapper>(ObjectMapper.class) {
            @Mock
            public <T> T readValue(String content, Class<T> valueType)
                throws IOException, JsonParseException, JsonMappingException {
                throw new JsonMappingException("");
            }
        };
        String xml = "<SoapHeader>\n" +
            "        <Ver>01</Ver>\n" +
            "        <SndDtTm>2019-08-19T09:25:43</SndDtTm>\n" +
            "        <MsgTp>dcep.461.001.01</MsgTp>\n" +
            "        <MsgSN>201908090041252000000000000900010001</MsgSN>\n" +
            "        <Sender>C1010411000013</Sender>\n" +
            "        <Receiver>C1010211000012</Receiver>\n" +
            "        <SignSN>3</SignSN>\n" +
            "        <NcrptnSN>3</NcrptnSN>\n" +
            "        <DgtlEnvlp>3</DgtlEnvlp>\n" +
            "    </SoapHeader>";

        Object obj = XmlUtils.xmlToObject(xml, SoapHeader.class);

    }

    /**
     * 给一个具体对象，转换成一个xml字符串
     */
    @Test
    public void testObjectToXml() throws Exception {
        SoapHeader header = new SoapHeader();
        header.setVer("01");
        header.setSndDtTm("2019-11-12T09:25:43");
        header.setMsgTp("dcep.461.001.01");
        header.setMsgSN("201911120041252000000000000900010001");
        header.setSender("C1010411000013");
        header.setReceiver("C1010211000012");
        header.setSignSN("3");
        header.setNcrptnSN("3");
        header.setDgtlEnvlp("3");

        String headerStr = XmlUtils.objectToXml(header);
        Assert.assertNotNull(headerStr);
        System.out.println(headerStr);

    }

    /**
     * 测试201,202,203dto处理
     */
    @Test
    public void testProcessPaymentDTO1() {
        String messageType = "dcep201.001.01";
        String tStr = "<FIToFICstmrCdtTrf>\n" +
            "            <GrpHdr>\n" +
            "                <MsgId>20190809004120100000000000090081</MsgId>\n" +
            "                <!--报文标识号、最后一位必须是0、1、2、3-->\n" +
            "                <CreDtTm>2019-11-12T09:25:43</CreDtTm>\n" +
            "                <NbOfTxs>1</NbOfTxs>\n" +
            "                <SttlmInf>\n" +
            "                    <SttlmMtd>CLRG</SttlmMtd>\n" +
            "                </SttlmInf>\n" +
            "            </GrpHdr>\n" +
            "            <CdtTrfTxInf>\n" +
            "                <PmtId>\n" +
            "                    <InstrId></InstrId>\n" +
            "                    <EndToEndId>20190809004120100000000000090081</EndToEndId>\n" +
            "                    <TxId>20190809004120100000000000090081</TxId>\n" +
            "                    <!--明细标识号、同报文标识号-->\n" +
            "\t\t\t\n" +
            "                </PmtId>\n" +
            "                <PmtTpInf>\n" +
            "                    <SvcLvl>\n" +
            "                        <Prtry>TT01</Prtry>\n" +
            "                        <!--支付方式TT00:普通汇款；TT01:扫码支付；TT02:碰一碰支付 禁止中文-->\n" +
            "\t\t\t\t\n" +
            "                    </SvcLvl>\n" +
            "                    <CtgyPurp>\n" +
            "                        <Prtry>C200</Prtry>\n" +
            "                        <!--业务类型编码 汇兑C200、投资理财C201、网络购物C202、商旅服务C203、缴费C204、慈善捐款C205、贷款还款C206、交易退款C208、实时代付C209、其他C210-->\n"
            +
            "\t\t\t\t\n" +
            "                    </CtgyPurp>\n" +
            "                </PmtTpInf>\n" +
            "                <IntrBkSttlmAmt Ccy=\"CNY\">1111.11</IntrBkSttlmAmt>\n" +
            "                <!--货币符号、结算金额-->\n" +
            "                <ChrgBr>DEBT</ChrgBr>\n" +
            "                <!--固定填写DEBT-->\n" +
            "                <InstgAgt>\n" +
            "                    <FinInstnId>\n" +
            "                        <ClrSysMmbId>\n" +
            "                            <MmbId>C1010211000012</MmbId>\n" +
            "                            <!--付款运营机构-->\n" +
            "\t\t\t\t\t\n" +
            "                        </ClrSysMmbId>\n" +
            "                    </FinInstnId>\n" +
            "                </InstgAgt>\n" +
            "                <InstdAgt>\n" +
            "                    <FinInstnId>\n" +
            "                        <ClrSysMmbId>\n" +
            "                            <MmbId>C1010311000014</MmbId>\n" +
            "                            <!--收款运营机构-->\n" +
            "\t\t\t\t\t\n" +
            "                        </ClrSysMmbId>\n" +
            "                    </FinInstnId>\n" +
            "                </InstdAgt>\n" +
            "                <Dbtr>\n" +
            "                    <Nm>谢三哥</Nm><!--付款姓名-->\n" +
            "                </Dbtr>\n" +
            "                <DbtrAcct>\n" +
            "                    <Id>\n" +
            "                        <Othr>\n" +
            "                            <Id>0021000000122282</Id>\n" +
            "                            <!--付款人钱包ID-->\n" +
            "                            <SchmeNm>\n" +
            "                                <Prtry>WL03</Prtry>\n" +
            "                                <!--付款人钱包等级-->\n" +
            "\t\t\t\t\t    \n" +
            "                            </SchmeNm>\n" +
            "                        </Othr>\n" +
            "                    </Id>\n" +
            "                    <Tp>\n" +
            "                        <Prtry>WT01</Prtry>\n" +
            "                        <!--付款人钱包类型-->\n" +
            "\t\t\t    \n" +
            "                    </Tp>\n" +
            "                    <Nm>谢三哥</Nm><!--付款钱包昵称-->\n" +
            "                    <DC>\n" +
            "                        <Id>DC</Id>\n" +
            "                        <!--数字货币-->\n" +
            "\t\t\t\t\n" +
            "                    </DC>\n" +
            "                </DbtrAcct>\n" +
            "                <Cdtr>\n" +
            "                    <Nm>张师弟</Nm><!--收款姓名-->\n" +
            "                </Cdtr>\n" +
            "                <CdtrAcct>\n" +
            "                    <Id>\n" +
            "                        <Othr>\n" +
            "                            <Id>0021000000122281</Id>\n" +
            "                            <!--收款人钱包ID-->\n" +
            "                            <SchmeNm>\n" +
            "                                <Prtry>WL03</Prtry>\n" +
            "                                <!--收款人钱包等级-->\n" +
            "\t\t\t\t\t    \n" +
            "                            </SchmeNm>\n" +
            "                        </Othr>\n" +
            "                    </Id>\n" +
            "                    <Tp>\n" +
            "                        <Prtry>WT01</Prtry>\n" +
            "                        <!--收款人钱包类型-->\n" +
            "\t\t\t    \n" +
            "                    </Tp>\n" +
            "                    <Nm>张师弟</Nm><!--收款钱包昵称-->\n" +
            "                </CdtrAcct>\n" +
            "                <Purp>\n" +
            "                    <Prtry>00600</Prtry>\n" +
            "                    <!--业务种类编码 00600 – 保险费-->  \n" +
            "\t\t\t\n" +
            "                </Purp>\n" +
            "                <RmtInf>\n" +
            "                    <Ustrd>/Postscript/11</Ustrd>\n" +
            "                    <Ustrd>/Remark/22</Ustrd>\n" +
            "                    <Ustrd>/MaskedEmail/123****@163.com</Ustrd>\n" +
            "                </RmtInf>\n" +
            "            </CdtTrfTxInf>\n" +
            "        </FIToFICstmrCdtTrf>";

    }

    public void testProcessPaymentDTO2() throws Exception {
        String messageType = "dcep201.001.01";
        String tStr = "<FIToFICstmrCdtTrf>\n" +
            "            <GrpHdr>\n" +
            "                <MsgId>20190809004120100000000000090081</MsgId>\n" +
            "                <!--报文标识号、最后一位必须是0、1、2、3-->\n" +
            "                <CreDtTm>2019-11-12T09:25:43</CreDtTm>\n" +
            "                <NbOfTxs>1</NbOfTxs>\n" +
            "                <SttlmInf>\n" +
            "                    <SttlmMtd>CLRG</SttlmMtd>\n" +
            "                </SttlmInf>\n" +
            "            </GrpHdr>\n" +
            "            <CdtTrfTxInf>\n" +
            "                <PmtId>\n" +
            "                    <InstrId></InstrId>\n" +
            "                    <EndToEndId>20190809004120100000000000090081</EndToEndId>\n" +
            "                    <TxId>20190809004120100000000000090081</TxId>\n" +
            "                    <!--明细标识号、同报文标识号-->\n" +
            "\t\t\t\n" +
            "                </PmtId>\n" +
            "                <PmtTpInf>\n" +
            "                    <SvcLvl>\n" +
            "                        <Prtry>TT01</Prtry>\n" +
            "                        <!--支付方式TT00:普通汇款；TT01:扫码支付；TT02:碰一碰支付 禁止中文-->\n" +
            "\t\t\t\t\n" +
            "                    </SvcLvl>\n" +
            "                    <CtgyPurp>\n" +
            "                        <Prtry>C200</Prtry>\n" +
            "                        <!--业务类型编码 汇兑C200、投资理财C201、网络购物C202、商旅服务C203、缴费C204、慈善捐款C205、贷款还款C206、交易退款C208、实时代付C209、其他C210-->\n"
            +
            "\t\t\t\t\n" +
            "                    </CtgyPurp>\n" +
            "                </PmtTpInf>\n" +
            "                <IntrBkSttlmAmt Ccy=\"CNY\">1111.11</IntrBkSttlmAmt>\n" +
            "                <!--货币符号、结算金额-->\n" +
            "                <ChrgBr>DEBT</ChrgBr>\n" +
            "                <!--固定填写DEBT-->\n" +
            "                <InstgAgt>\n" +
            "                    <FinInstnId>\n" +
            "                        <ClrSysMmbId>\n" +
            "                            <MmbId>C1010211000012</MmbId>\n" +
            "                            <!--付款运营机构-->\n" +
            "\t\t\t\t\t\n" +
            "                        </ClrSysMmbId>\n" +
            "                    </FinInstnId>\n" +
            "                </InstgAgt>\n" +
            "                <InstdAgt>\n" +
            "                    <FinInstnId>\n" +
            "                        <ClrSysMmbId>\n" +
            "                            <MmbId>C1010311000014</MmbId>\n" +
            "                            <!--收款运营机构-->\n" +
            "\t\t\t\t\t\n" +
            "                        </ClrSysMmbId>\n" +
            "                    </FinInstnId>\n" +
            "                </InstdAgt>\n" +
            "                <Dbtr>\n" +
            "                    <Nm>谢三哥</Nm><!--付款姓名-->\n" +
            "                </Dbtr>\n" +
            "                <DbtrAcct>\n" +
            "                    <Id>\n" +
            "                        <Othr>\n" +
            "                            <Id>0021000000122282</Id>\n" +
            "                            <!--付款人钱包ID-->\n" +
            "                            <SchmeNm>\n" +
            "                                <Prtry>WL03</Prtry>\n" +
            "                                <!--付款人钱包等级-->\n" +
            "\t\t\t\t\t    \n" +
            "                            </SchmeNm>\n" +
            "                        </Othr>\n" +
            "                    </Id>\n" +
            "                    <Tp>\n" +
            "                        <Prtry>WT01</Prtry>\n" +
            "                        <!--付款人钱包类型-->\n" +
            "\t\t\t    \n" +
            "                    </Tp>\n" +
            "                    <Nm>谢三哥</Nm><!--付款钱包昵称-->\n" +
            "                    <DC>\n" +
            "                        <Id>DC</Id>\n" +
            "                        <!--数字货币-->\n" +
            "\t\t\t\t\n" +
            "                    </DC>\n" +
            "                </DbtrAcct>\n" +
            "                <Cdtr>\n" +
            "                    <Nm>张师弟</Nm><!--收款姓名-->\n" +
            "                </Cdtr>\n" +
            "                <CdtrAcct>\n" +
            "                    <Id>\n" +
            "                        <Othr>\n" +
            "                            <Id>0021000000122281</Id>\n" +
            "                            <!--收款人钱包ID-->\n" +
            "                            <SchmeNm>\n" +
            "                                <Prtry>WL03</Prtry>\n" +
            "                                <!--收款人钱包等级-->\n" +
            "\t\t\t\t\t    \n" +
            "                            </SchmeNm>\n" +
            "                        </Othr>\n" +
            "                    </Id>\n" +
            "                    <Tp>\n" +
            "                        <Prtry>WT01</Prtry>\n" +
            "                        <!--收款人钱包类型-->\n" +
            "\t\t\t    \n" +
            "                    </Tp>\n" +
            "                    <Nm>张师弟</Nm><!--收款钱包昵称-->\n" +
            "                </CdtrAcct>\n" +
            "                <Purp>\n" +
            "                    <Prtry>00600</Prtry>\n" +
            "                    <!--业务种类编码 00600 – 保险费-->  \n" +
            "\t\t\t\n" +
            "                </Purp>\n" +
            "                <RmtInf>\n" +
            "                    <Ustrd>/Postscript/11</Ustrd>\n" +
            "                    <Ustrd>/Remark/22</Ustrd>\n" +
            "                    <Ustrd>/MaskedEmail/123****@163.com</Ustrd>\n" +
            "                </RmtInf>\n" +
            "            </CdtTrfTxInf>\n" +
            "        </FIToFICstmrCdtTrf>";

        Dcep20100101DTO t = (Dcep20100101DTO) XmlUtils.xmlToObject(tStr, Dcep20100101DTO.class);

    }

}
