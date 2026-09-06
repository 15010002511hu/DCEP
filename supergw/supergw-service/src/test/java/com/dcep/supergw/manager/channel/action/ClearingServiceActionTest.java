package com.dcep.supergw.manager.channel.action;

import com.dcep.clearing.dto.ClearingStatus;
import com.dcep.clearing.dto.dc202.Dcep20200101DTO;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.Response;
import com.dcep.common.model.msg.Dcep90000101DTO;
import com.dcep.common.model.msg.Dcep90200101DTO;
import com.dcep.common.model.msg.Dcep91100101DTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.config.DtoMappingConfig;
import com.dcep.supergw.common.enums.GwErrorEnum;
import com.dcep.supergw.common.exception.GwException;
import com.dcep.supergw.common.utils.ChannelContextMocker;
import com.dcep.supergw.common.utils.SoapUtils;
import com.dcep.supergw.common.utils.TestMsgUtils;
import com.dcep.supergw.common.utils.XmlUtils;
import com.dcep.supergw.manager.bo.ChannelContext;
import com.dcep.supergw.manager.channel.AbstractChannel;
import com.dcep.supergw.manager.channel.ChannelBuilder;
import com.dcep.supergw.manager.dubbo.DynamicInvoker;
import javax.validation.ConstraintViolationException;
import mockit.Mock;
import mockit.MockUp;
import mockit.integration.junit4.JMockit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RunWith(JMockit.class)
public class ClearingServiceActionTest {

    ClearingServiceAction clearingServiceAction;

    @Before
    public void mockInfoCache() {
        clearingServiceAction = new ClearingServiceAction();
//		new MockUp<InfoCacheUtils>(InfoCacheUtils.class) {
//			@Mock
//			public String getPbocInf() {
//				return "0000000000000";
//			}
//		};
//
//		new MockUp<ValidateUtils>(ValidateUtils.class){
//			@Mock
//			public void check(EnvelopeDTO envelopeDTO) {
//			}
//		};
    }

    @Test
    public void test_clearingServiceAction_doInvoke_201_return_900() {
//		clearingServiceAction = new ClearingServiceAction();
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());
        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new Dcep90000101DTO("", "", "", "", "", "", "", "", "", "", "", ""));
            }
        };

        clearingServiceAction.doInvoke(context);

//		new MockUp<DynamicInvoker>(DynamicInvoker.class) {
//			@Mock
//			public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
//				return null;
//			}
//		};
//
//		clearingServiceAction.doInvoke(context);
    }

    @Test(expected = GwException.class)
    public void test_clearingServiceAction_doInvoke_201_dubbo_throwException() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>("DCEPXXXX");
            }
        };

        clearingServiceAction.doInvoke(context);
    }

    @Test
    public void test_clearingServiceAction_doInvoke_201_dubbo_unkownResult() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new Dcep20200101DTO());
            }
        };

        clearingServiceAction.doInvoke(context);
    }

    @Test
    public void test_clearingServiceAction_doInvoke_201_return_clearStatus() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new ClearingStatus());
            }
        };

        clearingServiceAction.doInvoke(context);
    }

    @Test
    public void test_clearingServiceAction_doCallBack_202_return_900() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new Dcep90000101DTO("", "", "", "", "", "", "", "", "", "", "", ""));
            }
        };

        clearingServiceAction.doCallBack(context);
    }

    @Test
    public void test_clearingServiceAction_doCallBack_202_return_902() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new Dcep90200101DTO("", "", "", "", "", ""));
            }
        };

        clearingServiceAction.doCallBack(context);
    }

    @Test
    public void test_clearingServiceAction_doCallBack_202_return_911() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new Dcep91100101DTO("", "", "", ""));
            }
        };

        clearingServiceAction.doCallBack(context);
    }

    @Test(expected = GwException.class)
    public void test_clearingServiceAction_doCallBack_202_dubbo_throwException() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>("DCEPXXXX");
            }
        };

        clearingServiceAction.doCallBack(context);
    }

    @Test
    public void test_clearingServiceAction_doCallBack_202_dubbo_unkownResult() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());

        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new Dcep20200101DTO());
            }
        };

        clearingServiceAction.doCallBack(context);
    }

    @Test
    public void test_clearingServiceAction_doCallBack_202_return_clearStatus() {
        ChannelContext context = newMockChannelContext(TestMsgUtils.dcep_202());
        new MockUp<DynamicInvoker>(DynamicInvoker.class) {
            @Mock
            public Object invokeDubbo(String service, String method, EnvelopeDTO dto) {
                return new Response<>(new ClearingStatus());
            }
        };

        clearingServiceAction.doCallBack(context);
        context.setExceptionCatched(true);

        new MockUp<ChannelContext>(ChannelContext.class) {
            @Mock
            public void fireInvokeCallBack() {

            }
        };
        clearingServiceAction.doCallBack(context);
    }

    private ChannelContext newMockChannelContext(String msg) {
//		soapUtilsMock();
        ChannelContext channelContext = ChannelContextMocker.newChannelContext(msg);
        channelContext.setChannel(new AbstractChannel("clearingServiceAction unit test") {
            @Override
            public AbstractAction getChannel() {
                return ChannelBuilder.getInstance().addAction(clearingServiceAction)
                    .addAction(new AbstractAction("clearingServiceAction unit test") {
                        @Override
                        public void doInvoke(ChannelContext context) {
                        }
                    }).build();
            }
        });
        channelContext.setAction(clearingServiceAction);

        return channelContext;
    }


    private void soapUtilsMock() {
        new MockUp<SoapUtils>(SoapUtils.class) {
            @Mock
            public EnvelopeDTO toDto(SoapHeader header, String xml) {
                System.out.println("mock soapUtils toDto....");
                Class clz = DtoMappingConfig.getClzByMsgTp(header.getMsgTp());
                EnvelopeDTO envelopeDTO = new EnvelopeDTO();
                if (clz == null) {
                    throw new GwException(GwErrorEnum.MSGTP_ERROR,
                        "ParseXml2DTOUtils.parseXml2DTO，报文编号异常----messageType=" + header.getMsgTp());
                }
                try {
                    envelopeDTO.setSoapHeader(header);
                    envelopeDTO.setSoapBody(new SoapBody(getSoapBodyBean(xml, clz)));

                } catch (ConstraintViolationException e) {

                } catch (GwException e) {

                } catch (Exception e) {

                }
                return envelopeDTO;
            }
        };
    }

    private <T extends GwDTO> T getSoapBodyBean(String xml, Class<T> clz) {
        try {
            return XmlUtils.xmlToObject(getBodyStr(xml), clz);
        } catch (Exception e) {
            throw new GwException(GwErrorEnum.SOAPHEADER_ERROR, e);
        }
    }

    /**
     * @return 返回<:   Body></:Body>的字符串 不包括<:Body></:Body>标签
     */
    private String getBodyStr(String xml) {
        if (xml == null || "".equals(xml)) {
            return null;
        }
        String result = xml.substring(xml.indexOf(":Body>") + 6, xml.lastIndexOf(":Body>"));
        result = result.substring(0, result.lastIndexOf("</"));
        return result;
    }
}
