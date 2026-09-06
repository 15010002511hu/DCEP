package com.dcep.supergw.service.impl;

import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.GrpHdr;
import com.dcep.common.model.soap.InstdPty;
import com.dcep.common.model.soap.InstgPty;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgSnUtil;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.LoggerUtils;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.dto.dc401.Dcep40100101DTO;
import com.dcep.supergw.dto.dc401.FreeFrmtInf;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.AgencyProcessChannel;
import mockit.Expectations;
import mockit.Mock;
import mockit.MockUp;
import mockit.Mocked;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

/**
 * @author chenkai
 * @version GwOutServiceTest.java, v 0.1, 2019/8/21 19:09
 * @description
 */
public class GwoutServiceImplTest {

    @Mocked
    AgencyProcessChannel channel;

    @Mocked
    ChannelContext context;

    @Mocked
    LoggerUtils loggerUtils;

    @Mocked
    InfoCacheUtils infoCacheUtils;

    @Mocked
    ValidateUtils validateUtils;

    @Test
    public void excecute_succ() {
        GwoutServiceImpl gwOutService = new GwoutServiceImpl();

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setVer("01");
        soapHeader.setSndDtTm(DcepDateUtils.getDcepDateStrNow());

        soapHeader.setMsgTp("dcep.401.001.01");
        soapHeader.setSender("G4001011000013");
        soapHeader.setReceiver("C1010311000014");
        soapHeader.setSignSN("4");
        soapHeader.setNcrptnSN("");
        soapHeader.setDgtlEnvlp("");
        soapHeader.setMsgSN(MsgSnUtil.randomMsgSn("401", "000", "0"));
        envelopeDTO.setSoapHeader(soapHeader);

        SoapBody<GwDTO> soapBody = new SoapBody<>();

        Dcep40100101DTO dcep40100101DTO = new Dcep40100101DTO();

        GrpHdr grpHdr = new GrpHdr();
        grpHdr.setMsgId(soapHeader.getMsgSN().substring(0, 32));
        grpHdr.setCreDtTm(DcepDateUtils.getDcepDateStrNow());

        InstgPty instgPty = new InstgPty();
        instgPty.setInstgDrctPty("G4001011000013");
        grpHdr.setInstgPty(instgPty);

        InstdPty instdPty = new InstdPty();
        instdPty.setInstdDrctPty("C1010311000014");
        grpHdr.setInstdPty(instdPty);

        dcep40100101DTO.setGrpHdr(grpHdr);

        FreeFrmtInf freeFrmtInf = new FreeFrmtInf();
        freeFrmtInf.setMsgCnt("unit test");
        dcep40100101DTO.setFreeFrmtInf(freeFrmtInf);

        soapBody.setT(dcep40100101DTO);
        envelopeDTO.setSoapBody(soapBody);

        new MockUp<ChannelContext>(ChannelContext.class) {
            @Mock
            public void fireInvokeAction() {

            }
        };

        gwOutService.execute(envelopeDTO);
    }

    @Test
    public void excecute_fail() {
        GwoutServiceImpl gwOutService = new GwoutServiceImpl();

        EnvelopeDTO<GwDTO> envelopeDTO = new EnvelopeDTO<>();

        SoapHeader soapHeader = new SoapHeader();
        soapHeader.setVer("01");
        soapHeader.setSndDtTm(DcepDateUtils.getDcepDateStrNow());

        soapHeader.setMsgTp("dcep.401.001.01");
        soapHeader.setSender("G4001011000013");
        soapHeader.setReceiver("C1010311000014");
        soapHeader.setSignSN("4");
        soapHeader.setNcrptnSN("");
        soapHeader.setDgtlEnvlp("");
        soapHeader.setMsgSN(MsgSnUtil.randomMsgSn("401", "000", "0"));
        envelopeDTO.setSoapHeader(soapHeader);

        SoapBody<GwDTO> soapBody = new SoapBody<>();

        Dcep40100101DTO dcep40100101DTO = new Dcep40100101DTO();

        GrpHdr grpHdr = new GrpHdr();
        grpHdr.setMsgId(soapHeader.getMsgSN().substring(0, 32));
        grpHdr.setCreDtTm(DcepDateUtils.getDcepDateStrNow());

        InstgPty instgPty = new InstgPty();
        instgPty.setInstgDrctPty("G4001011000013");
        grpHdr.setInstgPty(instgPty);

        InstdPty instdPty = new InstdPty();
        instdPty.setInstdDrctPty("C1010311000014");
        grpHdr.setInstdPty(instdPty);

        dcep40100101DTO.setGrpHdr(grpHdr);

        FreeFrmtInf freeFrmtInf = new FreeFrmtInf();
        freeFrmtInf.setMsgCnt("unit test");
        dcep40100101DTO.setFreeFrmtInf(freeFrmtInf);

        soapBody.setT(dcep40100101DTO);
        envelopeDTO.setSoapBody(soapBody);

        new Expectations() {
            {
                ValidateUtils.validateEnvFlag((SoapHeader) any);
                result = new GwException(GwErrorEnum.PERF_TEST_SWITCH_CLOSE_ERROR.getCode(),
                    GwErrorEnum.PERF_TEST_SWITCH_CLOSE_ERROR.getDescription());
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                gwOutService.execute(envelopeDTO);
            }
        });

        new Expectations() {
            {
                ValidateUtils.validateEnvFlag((SoapHeader) any);
                result = new RuntimeException();
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                gwOutService.execute(envelopeDTO);
            }
        });
    }

    @Test
    public void send_succ() {
        GwoutServiceImpl gwOutService = new GwoutServiceImpl();

        String dcep401 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" +
            "    <soap:Header>\r\n" +
            "        <Ver>01</Ver>\r\n" +
            "        <SndDtTm>2020-05-02T19:00:00</SndDtTm>\r\n" +
            "        <MsgTp>dcep.401.001.01</MsgTp>\r\n" +
            "        <MsgSN>202005020011401000000000000000020000</MsgSN>\r\n" +
            "        <Sender>G4001011000013</Sender>\r\n" +
            "        <Receiver>C1010211000012</Receiver>\r\n" +
            "        <SignSN>4</SignSN>\r\n" +
            "    </soap:Header>\r\n" +
            "    <soap:Body>\r\n" +
            "        <FreeFrmt>\r\n" +
            "            <GrpHdr>\r\n" +
            "                <MsgId>20200502001140100000000000000002</MsgId>\r\n" +
            "                <CreDtTm>2020-05-02T19:00:00</CreDtTm>\r\n" +
            "                <InstgPty>\r\n" +
            "                    <InstgDrctPty>G4001011000013</InstgDrctPty>\r\n" +
            "                </InstgPty>\r\n" +
            "                <InstdPty>\r\n" +
            "                    <InstdDrctPty>C1010211000012</InstdDrctPty>\r\n" +
            "                </InstdPty>\r\n" +
            "            </GrpHdr>\r\n" +
            "            <FreeFrmtInf>\r\n" +
            "                <MsgCnt>11</MsgCnt>\r\n" +
            "            </FreeFrmtInf>\r\n" +
            "        </FreeFrmt>\r\n" +
            "    </soap:Body>\r\n" +
            "</soap:Envelope>";

        new MockUp<SoapUtils>(SoapUtils.class) {
            @SuppressWarnings("unchecked")
            @Mock
            public <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, byte[] xml) {
                return (EnvelopeDTO<T>) ChannelContextMocker.toDto(header, xml);
            }
        };

        gwOutService.send(dcep401);


    }

    @Test(expected = GwException.class)
    public void send_head_parse_fail() {
        GwoutServiceImpl gwOutService = new GwoutServiceImpl();

        new MockUp<SoapUtils>(SoapUtils.class) {
            @SuppressWarnings("unchecked")
            @Mock
            public <T extends GwDTO> EnvelopeDTO<T> toDto(SoapHeader header, byte[] xml) {
                return (EnvelopeDTO<T>) ChannelContextMocker.toDto(header, xml);
            }
        };
        String dcep401 = "";

        gwOutService.send(dcep401);


    }


    @Test(expected = GwException.class)
    public void send_body_parse_fail() {
        GwoutServiceImpl gwOutService = new GwoutServiceImpl();

        String dcep401 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
            "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\r\n" +
            "    <soap:Header>\r\n" +
            "        <Ver>01</Ver>\r\n" +
            "        <SndDtTm>2020-05-02T19:00:00</SndDtTm>\r\n" +
            "        <MsgTp>dcep.401.001.01</MsgTp>\r\n" +
            "        <MsgSN>202005020011401000000000000000020000</MsgSN>\r\n" +
            "        <Sender>G4001011000013</Sender>\r\n" +
            "        <Receiver>C1010211000012</Receiver>\r\n" +
            "        <SignSN>4</SignSN>\r\n" +
            "    </soap:Header>\r\n";

        gwOutService.send(dcep401);
    }

}