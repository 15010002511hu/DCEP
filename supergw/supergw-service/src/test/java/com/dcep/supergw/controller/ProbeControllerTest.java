package com.dcep.supergw.controller;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.DcepDateUtils;
import com.dcep.common.utils.MsgSnUtil;
import com.dcep.supergw.common.config.DtoMappingConfig;
import com.dcep.supergw.common.utils.EnviromentUtils;
import com.dcep.supergw.common.utils.GwMsgUtils;
import com.dcep.supergw.common.utils.InfoCacheUtils;
import com.dcep.supergw.common.utils.SoapHeaderUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.common.utils.ValidateUtils;
import com.dcep.supergw.dto.dc991.Dcep99100101DTO;
import com.dubbo.ldc.ZoneClient;
import com.dubbo.ldc.ZoneRouter;
import com.dubbo.ldc.model.Zone;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import mockit.Expectations;
import mockit.Mocked;
import mockit.Tested;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@SpringBootTest
@RunWith(JMockit.class)
public class ProbeControllerTest {

    @Tested
    ProbeController controller;
    @Mocked
    SoapHeaderUtils soapHeaderUtils;
    @Mocked
    Environment environment;
    @Mocked
    EnviromentUtils enviromentUtils;
    @Mocked
    DtoMappingConfig dtoMappingConfig;
    @Mocked
    ValidateUtils validateUtils;
    @Mocked
    ZoneClient zoneClient;
    @Mocked
    GwMsgUtils gwMsgUtils;
    @Mocked
    InfoCacheUtils infoCacheUtils;
    @Mocked
    Field field;

    @Test
    public void probeControllerdoPostsucc() throws ServletException, IOException, IllegalAccessException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        request.setAsyncSupported(true);
        request.setAttribute("header", new SoapHeader("01", DcepDateUtils.getDcepDateStrNow(), "dcep.991.001.01",
            MsgSnUtil.randomMsgSn("991", "000", "0"), "G4001011000013", "C1010511003703", "4", "", ""));
        request.setAttribute("xml", TestMsgUtils.dcep_991().getBytes());
        new Expectations(ZoneClient.class) {
            {
                zoneClient.getAllShardingInfo(anyString);
                Map<String, String> tmp = new HashMap<String, String>();
                tmp.put("00", "RZ00");
                tmp.put("01", "RZ01");
                tmp.put("02", "RZ02");
                tmp.put("03", "RZ03");
                result = tmp;
            }
        };
        new Expectations(DtoMappingConfig.class) {
            {
                dtoMappingConfig.getClzByMsgTp(anyString);
                result = Dcep99100101DTO.class;
            }
        };

        new Expectations(Field.class) {
            {
                field.get(any);
                Map<String, Zone> msz = new HashMap<String, Zone>();
                Zone tmp00 = new Zone();
                tmp00.setCity("beij");
                tmp00.setIdc("idc1234");
                tmp00.setZoneName("RZ00");
                tmp00.setZoneType(ZoneRouter.Type.RZ);
                msz.put("RZ00", tmp00);

                Zone tmp01 = new Zone();
                tmp01.setCity("beij");
                tmp01.setIdc("idc5678");
                tmp01.setZoneName("RZ01");
                tmp01.setZoneType(ZoneRouter.Type.RZ);
                msz.put("RZ01", tmp01);

                Zone tmp02 = new Zone();
                tmp02.setCity("SU01");
                tmp02.setIdc("idc9000");
                tmp02.setZoneName("RZ02");
                tmp02.setZoneType(ZoneRouter.Type.RZ);
                msz.put("RZ02", tmp01);

                Zone tmp03 = new Zone();
                tmp03.setCity("SU02");
                tmp03.setIdc("idc9111");
                tmp03.setZoneName("RZ03");
                tmp03.setZoneType(ZoneRouter.Type.RZ);
                msz.put("RZ03", tmp01);

                result = msz;
            }
        };

        controller.doPost(request, response);
    }

    @Test
    public void probeControllerdoPostfailE3() throws ServletException, IOException, IllegalAccessException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        request.setAsyncSupported(true);
        request.setAttribute("header", new SoapHeader("01", DcepDateUtils.getDcepDateStrNow(), "dcep.991.001.01",
            MsgSnUtil.randomMsgSn("991", "000", "0"), "G4001011000013", "C1010511003703", "4", "", ""));
        request.setAttribute("xml", TestMsgUtils.dcep_991().getBytes());
        new Expectations(ZoneClient.class) {
            {
                zoneClient.getAllShardingInfo(anyString);
                Map<String, String> tmp = new HashMap<String, String>();
                tmp.put("00", "R00");
                result = tmp;
            }
        };
        //        new Expectations(DtoMappingConfig.class) {
        //            {
        //                dtoMappingConfig.getClzByMsgTp(anyString);
        //                result = Dcep99100101DTO.class;
        //            }
        //        };

        new Expectations(Field.class) {
            {
                field.get(any);
                Map<String, Zone> msz = new HashMap<String, Zone>();
                Zone tmp = new Zone();
                tmp.setCity("beij");
                tmp.setIdc("idc1234");
                msz.put("R00", tmp);
                result = msz;
            }
        };

        controller.doPost(request, response);
    }

    @Test
    public void probeControllerdoPostfailE1() throws ServletException, IOException, IllegalAccessException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        request.setAsyncSupported(true);
        request.setAttribute("header", new SoapHeader("01", DcepDateUtils.getDcepDateStrNow(), "dcep.991.001.01",
            MsgSnUtil.randomMsgSn("991", "000", "0"), "G4001011000013", "C1010511003703", "4", "", ""));
        request.setAttribute("xml", TestMsgUtils.dcep_991().getBytes());
        new Expectations(ZoneClient.class) {
            {
                zoneClient.getAllShardingInfo(anyString);
                Map<String, String> tmp = new HashMap<String, String>();
                tmp.put("00", "R00");
                result = tmp;
            }
        };
        new Expectations(DtoMappingConfig.class) {
            {
                dtoMappingConfig.getClzByMsgTp(anyString);
                result = new IOException();
            }
        };

        new Expectations(Field.class) {
            {
                field.get(any);
                Map<String, Zone> msz = new HashMap<String, Zone>();
                Zone tmp = new Zone();
                tmp.setCity("beij");
                tmp.setIdc("idc1234");
                msz.put("R00", tmp);
                result = msz;
            }
        };

        controller.doPost(request, response);
    }

    @Test
    public void probeControllerdoPostfailE2() throws ServletException, IOException, IllegalAccessException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        request.setAsyncSupported(true);
        request.setAttribute("header", new SoapHeader("01", DcepDateUtils.getDcepDateStrNow(), "dcep.991.001.01",
            MsgSnUtil.randomMsgSn("991", "000", "0"), "G4001011000013", "C1010511003703", "4", "", ""));
        request.setAttribute("xml", TestMsgUtils.dcep_991().getBytes());

        new Expectations(DtoMappingConfig.class) {
            {
                dtoMappingConfig.getClzByMsgTp(anyString);
                result = new NoSuchFieldException();
            }
        };

        controller.doPost(request, response);
    }

}
