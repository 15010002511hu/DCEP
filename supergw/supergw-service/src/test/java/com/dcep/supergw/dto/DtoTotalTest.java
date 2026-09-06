package com.dcep.supergw.dto;

import com.dcep.supergw.dto.dc401.Dcep40100101DTO;
import com.dcep.supergw.dto.dc415.Dcep41500101DTO;
import com.dcep.supergw.dto.dc416.Dcep41600101DTO;
import com.dcep.supergw.dto.dc419.Dcep41900101DTO;
import com.dcep.supergw.dto.dc420.Dcep42000101DTO;
import com.dcep.supergw.dto.dc433.Dcep43300101DTO;
import com.dcep.supergw.dto.dc434.Dcep43400101DTO;
import com.dcep.supergw.dto.dc441.Dcep44100101DTO;
import com.dcep.supergw.dto.dc442.Dcep44200101DTO;
import com.dcep.supergw.dto.dc443.Dcep44300101DTO;
import com.dcep.supergw.dto.dc721.Dcep72100101DTO;
import com.dcep.supergw.dto.dc722.Dcep72200101DTO;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JMockit.class)
public class DtoTotalTest {

    @Test
    public void DtoTest() {
        Dcep40100101DTO DTO401 = new Dcep40100101DTO();
        Dcep41500101DTO DTO415 = new Dcep41500101DTO();
        Dcep41600101DTO DTO416 = new Dcep41600101DTO();
        Dcep41900101DTO DTO419 = new Dcep41900101DTO();
        Dcep42000101DTO DTO420 = new Dcep42000101DTO();
        Dcep43300101DTO DTO433 = new Dcep43300101DTO();
        Dcep43400101DTO DTO434 = new Dcep43400101DTO();
        Dcep44100101DTO DTO441 = new Dcep44100101DTO();
        Dcep44200101DTO DTO442 = new Dcep44200101DTO();
        Dcep44300101DTO DTO443 = new Dcep44300101DTO();
        Dcep72100101DTO DTO721 = new Dcep72100101DTO();
        Dcep72200101DTO DTO722 = new Dcep72200101DTO();

    }

}
