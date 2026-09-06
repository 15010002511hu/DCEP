/*
 * pbcdci.cn Inc.
 * Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.common.utils;

import com.dcep.common.model.soap.SoapHeader;
import com.dcep.common.utils.MsgSnUtil;
import com.dcep.supergw.common.config.NacosConfigClient;
import com.dcep.supergw.common.exception.GwException;
import com.dcepex.trace.support.TraceContext;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class ValidateUtilsEnvFlagTest {

    @Mocked
    private TraceContext traceContext;

    @Mocked
    private NacosConfigClient nacosConfigClient;

    @Test
    public void validateLoadTraffic() {
        SoapHeader header = new SoapHeader();
        header.setMsgSN(MsgSnUtil.randomMsgSn("401", "000", "0"));

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = "FALSE";
            }
        };

        Assertions.assertThrows(GwException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ValidateUtils.validateEnvFlag(header);
            }
        });
    }

    @Test
    public void validateProdEnvFlagSucc() {
        SoapHeader header = new SoapHeader();
        header.setMsgSN(MsgSnUtil.randomMsgSn("401", "000", "1"));

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = null;
            }
        };

        new Expectations() {
            {
                NacosConfigClient.getInstance();
                result = nacosConfigClient;

                nacosConfigClient.getEnvFlag();
                result = "1";
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                ValidateUtils.validateEnvFlag(header);
            }
        });
    }

    @Test
    public void validateProdEnvFlagFail() {
        SoapHeader header = new SoapHeader();
        header.setMsgTp("dcep.401.001.01");
        header.setSender("G4001011000013");
        header.setReceiver("C1010311000014");
        header.setMsgSN(MsgSnUtil.randomMsgSn("401", "000", "1"));

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = null;
            }
        };

        new Expectations() {
            {
                NacosConfigClient.getInstance();
                result = nacosConfigClient;

                nacosConfigClient.getEnvFlag();
                result = "3";
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                ValidateUtils.validateEnvFlag(header);
            }
        });

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = "true";
            }
        };

        Assertions.assertThrows(GwException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ValidateUtils.validateEnvFlag(header);
            }
        });
    }

    @Test
    public void validatePerfEnvFlagSucc() {
        SoapHeader header = new SoapHeader();
        header.setMsgTp("dcep.401.001.01");
        header.setSender("G4001011000013");
        header.setReceiver("C1010311000014");
        header.setMsgSN(MsgSnUtil.randomMsgSn("401", "000", "2"));

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = "true";
            }
        };

        new Expectations() {
            {
                NacosConfigClient.getInstance();
                result = nacosConfigClient;

                nacosConfigClient.getPressFlag();
                result = true;
            }
        };

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                ValidateUtils.validateEnvFlag(header);
            }
        });
    }

    @Test
    public void validatePerfEnvFlagFail() {
        SoapHeader header = new SoapHeader();
        header.setMsgTp("dcep.401.001.01");
        header.setSender("G4001011000013");
        header.setReceiver("C1010311000014");
        header.setMsgSN(MsgSnUtil.randomMsgSn("401", "000", "2"));

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = "true";
            }
        };

        new Expectations() {
            {
                NacosConfigClient.getInstance();
                result = nacosConfigClient;

                nacosConfigClient.getPressFlag();
                result = false;
            }
        };

        Assertions.assertThrows(GwException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ValidateUtils.validateEnvFlag(header);
            }
        });

        new Expectations() {
            {
                TraceContext.get(anyString);
                result = null;
            }
        };

        new Expectations() {
            {
                NacosConfigClient.getInstance();
                result = nacosConfigClient;

                nacosConfigClient.getPressFlag();
                result = true;
            }
        };

        Assertions.assertThrows(GwException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                ValidateUtils.validateEnvFlag(header);
            }
        });
    }
}
