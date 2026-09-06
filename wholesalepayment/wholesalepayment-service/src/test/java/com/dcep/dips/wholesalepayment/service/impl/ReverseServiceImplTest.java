package com.dcep.dips.wholesalepayment.service.impl;

import cn.hutool.core.date.DateUtil;
import com.dcep.common.model.ActiveCurrencyAndAmount;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.soap.FinInstnId;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.soap.XmlUtils;
import com.dcep.common.utils.MsgIdUtil;
import com.dcep.dips.wholesalepayment.api.ReverseService;
import com.dcep.dips.wholesalepayment.common.utils.DtoUtil;
import com.dcep.dips.wholesalepayment.dto.dc427.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ReferenceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.Date;

@Slf4j
public class ReverseServiceImplTest {

    @InjectMocks
    private ReverseServiceImpl reverseService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

//    @Test
    public void procesReal() {
        ReferenceConfig<ReverseService> referenceConfig = new ReferenceConfig<>();
        referenceConfig.setInterface(ReverseService.class);
        referenceConfig.setValidation("false");
        referenceConfig.setUrl("dubbo://127.0.0.1:20883");
        referenceConfig.setApplication(new ApplicationConfig("wholesalepayment"));
        referenceConfig.setRetries(0);
        referenceConfig.setCheck(true);
        ReverseService reverseService = referenceConfig.get();

        EnvelopeDTO<GwDTO> envelopeDTO = createDcep42700101DTO();
        try {
            String xml = XmlUtils.objectToXml(envelopeDTO);
            log.info("request:"+ xml);
            Response<EnvelopeDTO> response = reverseService.process(envelopeDTO);
            xml = XmlUtils.objectToXml(response.getResult());
            log.info("response:"+ xml);
        }catch (Exception e){
            e.printStackTrace();
            log.debug("异常信息：",e.getMessage());
        }




    }

    private EnvelopeDTO<GwDTO> createDcep42700101DTO() {

        Assgnmt assgnmt = new Assgnmt();
        String msgId = (DateUtil.format(new Date(),"yyyyMMdd")+ "427" + "1" + "427" + MsgIdUtil.getRandomNum(14)) + "00" + "0";
//        String msgId = MsgIdUtil.randomMsgId("427","C1040311005293","6");
        assgnmt.setId( msgId);
        assgnmt.setAssgnr(new Assgnr(new Agt(createFinInstnId(null,"C1040311005293"))));
        assgnmt.setAssgne(new Assgne(new Agt(createFinInstnId(null,"C1030935001347"))));
        assgnmt.setCreDtTm(DateUtil.format(new Date(),"yyyy-MM-dd'T'HH:mm:ss"));



        TxInf txInf = new TxInf();
        txInf.setCas(new Cas("20251021008122700000000176238890",new Cretr(new Agt(createFinInstnId(null,"C1040311005293")))));
        OrgnlTxRef orgnlTxRef = new OrgnlTxRef();
        orgnlTxRef.setPurp(new Purp("prtry"));
        orgnlTxRef.setCdtrAgt(new CdtrAgt(createFinInstnId(null,"C1030935001347")));
        orgnlTxRef.setDbtrAgt(new DbtrAgt(createFinInstnId(null,"C1040311005293")));
        orgnlTxRef.setPmtTpInf(new PmtTpInf( new CtgyPurp("")));
        orgnlTxRef.setIntrBkSttlmAmt(new ActiveCurrencyAndAmount("CNY","10"));
        txInf.setOrgnlTxRef(orgnlTxRef);

        CxlRsnInf cxlRsnInf = new CxlRsnInf();
        cxlRsnInf.setAddtlInf("addtlInf");
        cxlRsnInf.setRsn(new Rsn("2222"));
        txInf.setCxlRsnInf( cxlRsnInf);

        OrgnlGrpInf orgnlGrpInf = new OrgnlGrpInf();
        orgnlGrpInf.setOrgnlMsgId("20251021008122700000000176238890");
        orgnlGrpInf.setOrgnlMsgNmId("dcep.227.010.01");
        txInf.setOrgnlGrpInf(orgnlGrpInf);
        txInf.setOrgnlUETR("20251021008122700000000176238890");

        Undrlyg undrlyg = new Undrlyg(txInf);

        // 创建 Dcep42700101DTO 对象
        Dcep42700101DTO dcep42700101DTO = new Dcep42700101DTO();
        dcep42700101DTO.setAssgnmt(assgnmt);
        dcep42700101DTO.setUndrlyg(undrlyg);
        // 初始化 Dcep20301002DTO
        dcep42700101DTO.init();

        // 打印初始化后的对象
        System.out.println(dcep42700101DTO);


        // 模拟输入
        EnvelopeDTO<GwDTO> req = new EnvelopeDTO<>();
        req.setSoapBody(new SoapBody<>(dcep42700101DTO));
        req.setSoapHeader(new SoapHeader());

        return req;


    }
    private static FinInstnId createFinInstnId(String code, String mmbId) {
        FinInstnId finInstnId = new FinInstnId();
        FinInstnId.ClrSysMmbId clrSysMmbId = new FinInstnId.ClrSysMmbId();
        FinInstnId.ClrSysId clrSysId = new FinInstnId.ClrSysId();
        clrSysId.setCd(code);
        clrSysMmbId.setClrSysId(clrSysId);
        clrSysMmbId.setMmbId(mmbId);
        finInstnId.setClrSysMmbId(clrSysMmbId);
        return finInstnId;
    }
}