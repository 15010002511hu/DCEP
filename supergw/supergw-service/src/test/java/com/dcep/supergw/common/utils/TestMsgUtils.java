package com.dcep.supergw.common.utils;

import com.dcep.common.utils.DcepDateUtils;
import java.util.Date;

public class TestMsgUtils {

    private static String dcep_401 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "    <soap:Header>\n" +
        "        <Ver>01</Ver>\n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm>\n" +
        "        <MsgTp>dcep.401.001.01</MsgTp>\n" +
        "        <MsgSN>#msgId0001</MsgSN>\n" +
        "        <Sender>C1010511003703</Sender>\n" +
        "        <Receiver>C1010511003703</Receiver>\n" +
        "        <SignSN>4</SignSN>\n" +
        "        <NcrptnSN>123456789</NcrptnSN>\n" +
        "        <DgtlEnvlp>451200215</DgtlEnvlp>\n" +
        "    </soap:Header>\n" +
        "    <soap:Body>\n" +
        "        <FreeFrmt>\n" +
        "            <GrpHdr>\n" +
        "                <MsgId>#msgId</MsgId>\n" +
        "                <CreDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</CreDtTm>\n" +
        "                <InstgPty>\n" +
        "                    <InstgDrctPty>C1010511003703</InstgDrctPty>\n" +
        "                </InstgPty>\n" +
        "                <InstdPty>\n" +
        "                    <InstdDrctPty>C1010511003703</InstdDrctPty>\n" +
        "                </InstdPty>\n" +
        "                <Rmk>Rmk</Rmk>\n" +
        "            </GrpHdr>\n" +
        "            <FreeFrmtInf>\n" +
        "                <MsgCnt>MsgCnt</MsgCnt>\n" +
        "            </FreeFrmtInf>\n" +
        "        </FreeFrmt>\n" +
        "    </soap:Body></soap:Envelope>";

    private static String dcep_415 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "    <soap:Header>\n" +
        "        <Ver>01</Ver>\n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm>\n" +
        "        <MsgTp>dcep.415.001.01</MsgTp>\n" +
        "        <MsgSN>#msgId0001</MsgSN>\n" +
        "        <Sender>C1010511003703</Sender>\n" +
        "        <Receiver>C1010511003703</Receiver>\n" +
        "        <SignSN>4</SignSN>\n" +
        "        <NcrptnSN>123456789</NcrptnSN>\n" +
        "        <DgtlEnvlp>451200215</DgtlEnvlp>\n" +
        "    </soap:Header>\n" +
        "    <soap:Body>\n" +
        "        <OrdrQryReq>\n" +
        "            <GrpHdr>\n" +
        "                <MsgId>#msgId</MsgId>\n" +
        "                <CreDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</CreDtTm>\n" +
        "                <InstgPty>\n" +
        "                    <InstgDrctPty>C1010511003703</InstgDrctPty>\n" +
        "                </InstgPty>\n" +
        "                <InstdPty>\n" +
        "                    <InstdDrctPty>C1010511003703</InstdDrctPty>\n" +
        "                </InstdPty>\n" +
        "                <Rmk>Rmk</Rmk>\n" +
        "            </GrpHdr>\n" +
        "            <Dbtr>\n" +
        "                <DbtrWltId>ARSK9mhi0KXQPoSW2RG1UJ5MqHbpqFQVMr0gtnwhOn8=</DbtrWltId>\n" +
        "            </Dbtr>\n" +
        "        </OrdrQryReq>\n" +
        "    </soap:Body></soap:Envelope>";


    private static String dcep_902 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:head=\"http://www.dcep.com/dcep/header/\">\n"
        +
        "    <soap:Header>\n" +
        "        <head:Ver>01</head:Ver>\n" +
        "        <head:SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</head:SndDtTm>\n" +
        "        <head:MsgTp>dcep.902.001.01</head:MsgTp>\n" +
        "        <head:MsgSN>#msgId0001</head:MsgSN>\n" +
        "        <head:Sender>C1010511003703</head:Sender>\n" +
        "        <head:Receiver>C1010511003703</head:Receiver>\n" +
        "        <head:SignSN>4</head:SignSN>\n" +
        "        <head:NcrptnSN>4512</head:NcrptnSN>\n" +
        "        <head:DgtlEnvlp>45100</head:DgtlEnvlp>\n" +
        "    </soap:Header>\n" +
        "    <soap:Body>\n" +
        "        <wstxns1:ComConf xmlns:wstxns1=\"http://www.dcep.com/dcep/90200101/\">\n" +
        "            <ConfInf>\n" +
        "                <OrigSndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</OrigSndDtTm>\n" +
        "                <OrgnlMsgId>#msgId</OrgnlMsgId>\n" +
        "                <OrgnlInstgPty>Z2007933000010</OrgnlInstgPty>\n" +
        "                <OrgnlMT>20150214120141</OrgnlMT>\n" +
        "                <PrcSts>PR00</PrcSts>\n" +
        "                <Remark>451230205</Remark>\n" +
        "            </ConfInf>\n" +
        "        </wstxns1:ComConf>\n" +
        "    </soap:Body>\n" +
        "</soap:Envelope>";

    private static String dcep_201 = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">   \n" +
        "  <soap:Header>  \n" +
        "    <Ver>01</Ver>   \n" +
        "    <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm>   \n" +
        "    <MsgTp>dcep.201.001.01</MsgTp>   \n" +
        "    <MsgSN>#msgId0001</MsgSN>   \n" +
        "    <Sender>C1010511003703</Sender>   \n" +
        "    <Receiver>C1010211000012</Receiver>   \n" +
        "    <SignSN>1254210.32</SignSN>   \n" +
        "    <NcrptnSN>451201</NcrptnSN>   \n" +
        "    <DgtlEnvlp>451200215</DgtlEnvlp>  \n" +
        "  </soap:Header>   \n" +
        "  <soap:Body>  \n" +
        "    <FIToFICstmrCdtTrf>  \n" +
        "      <GrpHdr>  \n" +
        "        <MsgId>#msgId</MsgId>   \n" +
        "        <CreDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</CreDtTm>   \n" +
        "        <NbOfTxs>1</NbOfTxs>   \n" +
        "        <SttlmInf>  \n" +
        "          <SttlmMtd>CLRG</SttlmMtd>  \n" +
        "        </SttlmInf>  \n" +
        "      </GrpHdr>   \n" +
        "      <CdtTrfTxInf>  \n" +
        "        <PmtId>  \n" +
        "          <InstrId>B454120121451</InstrId>   \n" +
        "          <EndToEndId>2019072901112</EndToEndId>   \n" +
        "          <TxId>20190729011120100000000759889370</TxId>  \n" +
        "        </PmtId>   \n" +
        "        <PmtTpInf>  \n" +
        "          <SvcLvl>  \n" +
        "            <Prtry>TT00</Prtry>  \n" +
        "          </SvcLvl>   \n" +
        "          <CtgyPurp>  \n" +
        "            <Prtry>C200</Prtry>  \n" +
        "          </CtgyPurp>  \n" +
        "        </PmtTpInf>   \n" +
        "        <IntrBkSttlmAmt Ccy=\"CNY\">4512012.00</IntrBkSttlmAmt>   \n" +
        "        <ChrgBr>DEBT</ChrgBr>   \n" +
        "        <InstgAgt>  \n" +
        "          <FinInstnId>  \n" +
        "            <ClrSysMmbId>  \n" +
        "              <MmbId>C1010511003703</MmbId>  \n" +
        "            </ClrSysMmbId>  \n" +
        "          </FinInstnId>  \n" +
        "        </InstgAgt>   \n" +
        "        <InstdAgt>  \n" +
        "          <FinInstnId>  \n" +
        "            <ClrSysMmbId>  \n" +
        "              <MmbId>C1010211000012</MmbId>  \n" +
        "            </ClrSysMmbId>  \n" +
        "          </FinInstnId>  \n" +
        "        </InstdAgt>   \n" +
        "        <Dbtr>  \n" +
        "          <Nm>付款姓名</Nm>  \n" +
        "        </Dbtr>   \n" +
        "        <DbtrAcct>  \n" +
        "          <Id>  \n" +
        "            <Othr>  \n" +
        "              <Id>0011000000112341</Id>   \n" +
        "              <SchmeNm>  \n" +
        "                <Prtry>WL04</Prtry>  \n" +
        "              </SchmeNm>  \n" +
        "            </Othr>  \n" +
        "          </Id>   \n" +
        "          <Tp>  \n" +
        "            <Prtry>WT01</Prtry>  \n" +
        "          </Tp>   \n" +
        "          <Nm>付款钱包昵称</Nm>   \n" +
        "          <DC>  \n" +
        "            <Id>DC-xxx-biaodashi</Id>  \n" +
        "          </DC>  \n" +
        "        </DbtrAcct>   \n" +
        "        <Cdtr>  \n" +
        "          <Nm>收款姓名</Nm>  \n" +
        "        </Cdtr>   \n" +
        "        <CdtrAcct>  \n" +
        "          <Id>  \n" +
        "            <Othr>  \n" +
        "              <Id>0021000000122281</Id>   \n" +
        "              <SchmeNm>  \n" +
        "                <Prtry>WL01</Prtry>  \n" +
        "              </SchmeNm>  \n" +
        "            </Othr>  \n" +
        "          </Id>   \n" +
        "          <Tp>  \n" +
        "            <Prtry>WT01</Prtry>  \n" +
        "          </Tp>   \n" +
        "          <Nm>hahaha</Nm>  \n" +
        "        </CdtrAcct>   \n" +
        "        <Purp>  \n" +
        "          <Prtry>22011</Prtry>  \n" +
        "        </Purp>   \n" +
        "        <RmtInf>  \n" +
        "          <Ustrd>/Postscript/附言</Ustrd>   \n" +
        "          <Ustrd>/Remark/备注，前缀不能少</Ustrd>  \n" +
        "          <Ustrd>/MaskedTel/137****2314</Ustrd>   \n" +
        "          <Ustrd>/MaskedEmail/123****@126.com</Ustrd> \n" +
        "        </RmtInf>  \n" +
        "      </CdtTrfTxInf>  \n" +
        "    </FIToFICstmrCdtTrf>  \n" +
        "  </soap:Body>  \n" +
        "</soap:Envelope>";

    private static String dcep_202 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
        "    <soap:Header>\n" +
        "        <Ver>01</Ver>\n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm>\n" +
        "        <MsgTp>dcep.202.001.01</MsgTp>\n" +
        "        <MsgSN>#msgId00001</MsgSN>\n" +
        "        <Sender>C1010311000014</Sender>\n" +
        "        <Receiver>C1010511003703</Receiver>\n" +
        "        <SignSN>4</SignSN>\n" +
        "        <NcrptnSN>451201</NcrptnSN>\n" +
        "        <DgtlEnvlp>451200215</DgtlEnvlp>\n" +
        "    </soap:Header>\n" +
        "    <soap:Body>\n" +
        "        <FIToFIPmtStsRpt>\n" +
        "            <GrpHdr>\n" +
        "                <MsgId>#msgId</MsgId>\n" +
        "                <CreDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</CreDtTm>\n" +
        "            </GrpHdr>\n" +
        "            <OrgnlGrpInfAndSts>\n" +
        "                <OrgnlMsgId>#msgId</OrgnlMsgId>\n" +
        "                <OrgnlMsgNmId>dcep.201.001.01</OrgnlMsgNmId>\n" +
        "                <StsRsnInf>\n" +
        "                    <AddtlInf>PR00</AddtlInf>\n" +
        "                </StsRsnInf>\n" +
        "            </OrgnlGrpInfAndSts>\n" +
        "            <TxInfAndSts>\n" +
        "                <StsId>PR00</StsId>\n" +
        "                <OrnlInstrId>B123456789123</OrnlInstrId>\n" +
        "                <OrgnlTxId>20190821001120100000000759888021</OrgnlTxId>\n" +
        "                <StsRsnInf>\n" +
        "                    <Rsn>\n" +
        "                        <Prtry></Prtry>\n" +
        "                    </Rsn>\n" +
        "                    <AddtlInf></AddtlInf>\n" +
        "                </StsRsnInf>\n" +
        "                <OrgnlTxRef>\n" +
        "                    <IntrBkSttlmAmt Ccy=\"CNY\">45120.01</IntrBkSttlmAmt>\n" +
        "                    <PmtTpInf>\n" +
        "                        <CtgyPurp>\n" +
        "                            <Prtry>C201</Prtry>\n" +
        "                        </CtgyPurp>\n" +
        "                    </PmtTpInf>\n" +
        "                    <MndtRltdInf>\n" +
        "                        <AmdmntInfDtls>\n" +
        "                            <OrgnlDbtrAgt>\n" +
        "                                <FinInstnId>\n" +
        "                                    <ClrSysMmbId>\n" +
        "                                        <MmbId>C1010511003703</MmbId>\n" +
        "                                    </ClrSysMmbId>\n" +
        "                                </FinInstnId>\n" +
        "                            </OrgnlDbtrAgt>\n" +
        "                        </AmdmntInfDtls>\n" +
        "                    </MndtRltdInf>\n" +
        "                    <RmtInf>\n" +
        "                        <Ustrd>/CdtrBankId/00</Ustrd>\n" +
        "                    </RmtInf>\n" +
        "                    <DbtrAgt>\n" +
        "                        <FinInstnId>\n" +
        "                            <ClrSysMmbId>\n" +
        "                                <MmbId>C1010511003703</MmbId>\n" +
        "                            </ClrSysMmbId>\n" +
        "                        </FinInstnId>\n" +
        "                    </DbtrAgt>\n" +
        "                    <CdtrAgt>\n" +
        "                        <FinInstnId>\n" +
        "                            <ClrSysMmbId>\n" +
        "                                <MmbId>C1010511003703</MmbId>\n" +
        "                            </ClrSysMmbId>\n" +
        "                        </FinInstnId>\n" +
        "                    </CdtrAgt>\n" +
        "                </OrgnlTxRef>\n" +
        "            </TxInfAndSts>\n" +
        "        </FIToFIPmtStsRpt>\n" +
        "    </soap:Body>\n" +
        "</soap:Envelope>";

    private static String dcep_911 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"> \n" +
        "    <soap:Header> \n" +
        "        <Ver>01</Ver> \n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm> \n" +
        "        <MsgTp>dcep.911.001.01</MsgTp> \n" +
        "        <MsgSN>#msgId0001</MsgSN> \n" +
        "        <Sender>C1010211000012</Sender> \n" +
        "        <Receiver>C1010211000012</Receiver> \n" +
        "        <SignSN>4</SignSN> \n" +
        "    </soap:Header> \n" +
        "    <soap:Body> \n" +
        "        <Fault> \n" +
        "            <faultcode>DCEPO1012</faultcode> \n" +
        "            <faultstring>参数校验异常</faultstring> \n" +
        "            <faultactor>C1010211000012</faultactor> \n" +
        "            <detail>参数校验失败--soapBody.t.cdtr.cdtrWltId:不能为空</detail> \n" +
        "        </Fault> \n" +
        "    </soap:Body> \n" +
        "</soap:Envelope>";

    private static String dcep_900 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" +
        "    <soap:Header>\r\n" +
        "        <Ver>01</Ver>\r\n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm> \n" +
        "        <MsgTp>dcep.900.001.01</MsgTp>\r\n" +
        "        <MsgSN>#msgId0001</MsgSN> \n" +
        "        <Sender>G4001011000013</Sender>\r\n" +
        "        <Receiver>C1010511003703</Receiver>\r\n" +
        "        <SignSN>4</SignSN>\r\n" +
        "    </soap:Header>\r\n" +
        "    <soap:Body>\r\n" +
        "        <dcep:CmonConf xmlns:dcep=\"http://www.dcep.com/ws/\">\r\n" +
        "            <GrpHdr>\r\n" +
        "                <MsgId>20191205005190000000003417030001</MsgId>\r\n" +
        "                <CreDtTm>2019-12-05T02:04:26</CreDtTm>\r\n" +
        "                <InstgPty>\r\n" +
        "                    <InstgDrctPty>G4001011000013</InstgDrctPty>\r\n" +
        "                </InstgPty>\r\n" +
        "                <InstdPty>\r\n" +
        "                    <InstdDrctPty>C1010511003703</InstdDrctPty>\r\n" +
        "                </InstdPty>\r\n" +
        "            </GrpHdr>\r\n" +
        "            <OrgnlGrpHdr>\r\n" +
        "                <OrgnlMsgId>20191205005191900000003417030001</OrgnlMsgId>\r\n" +
        "                <OrgnlInstgPty>C1010511003703</OrgnlInstgPty>\r\n" +
        "                <OrgnlMT>dcep.919.001.01</OrgnlMT>\r\n" +
        "            </OrgnlGrpHdr>\r\n" +
        "            <CmonConfInf>\r\n" +
        "                <PrcSts>PR00</PrcSts>\r\n" +
        "                <PrcCd>DCEPI0000</PrcCd>\r\n" +
        "                <RjctInf>成功</RjctInf>\r\n" +
        "            </CmonConfInf>\r\n" +
        "        </dcep:CmonConf>\r\n" +
        "    </soap:Body>\r\n" +
        "</soap:Envelope>";

    private static String dcep_991 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" +
        "    <soap:Header>\r\n" +
        "        <Ver>01</Ver>\r\n" +
        "        <SndDtTm>" + DcepDateUtils.getDcepDateStrNow() + "</SndDtTm> \n" +
        "        <MsgTp>dcep.991.001.01</MsgTp>\r\n" +
        "        <MsgSN>#msgId0001</MsgSN> \n" +
        "        <Sender>G4001011000013</Sender>\r\n" +
        "        <Receiver>C1010511003703</Receiver>\r\n" +
        "        <SignSN>4</SignSN>\r\n" +
        "    </soap:Header>\r\n" +
        "    <soap:Body>\r\n" +
        "        <dcep:ProbeReq xmlns:dcep=\"http://www.dcep.com/dcep/99100101/\">\r\n" +
        "			<ProbeInf>\r\n" +
        "				<InstgId>C1010411000013</InstgId>\r\n" +
        "			</ProbeInf>\r\n" +
        "        </dcep:ProbeReq>\r\n" +
        "    </soap:Body>\r\n" +
        "</soap:Envelope>";

    public static String dcep_401() {
        return dcep_401.replaceAll("#msgId", getMsgId("401"));
    }

    public static String dcep_415() {
        return dcep_415.replaceAll("#msgId", getMsgId("415"));
    }

    public static String dcep_201() {
        return dcep_201.replaceAll("#msgId", getMsgId("201"));
    }

    public static String dcep_911() {
        return dcep_911.replaceAll("#msgId", getMsgId("911"));
    }

    public static String dcep_202() {
        return dcep_202.replaceAll("#msgId", getMsgId("202"));
    }

    public static String dcep_902() {
        return dcep_902.replaceAll("#msgId", getMsgId("902"));
    }

    public static String dcep_900() {
        return dcep_900.replaceAll("#msgId", getMsgId("902"));
    }

    public static String dcep_991() {
        return dcep_991.replaceAll("#msgId", getMsgId("991"));
    }

    private static String CHAR_LIST = "0123456789";

    private static char getChar() {
        return CHAR_LIST.charAt((int) (Math.random() % CHAR_LIST.length()));
    }

    public static String getMsgId(String msgType) {
        String first = DcepDateUtils.formateDate(new Date(), DcepDateUtils.DATE_PATTERN);
        String second = "0901";
        String third = msgType;
        String forth = "";
        while (forth.length() < 12) {
            forth += getChar();
        }
        String five = "00002";
        return first + second + third + forth + five;
    }

    public static void main(String[] args) {
        System.out.println(dcep_401());
    }

}
