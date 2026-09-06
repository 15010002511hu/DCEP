/*
 * pbcdci.cn Inc. Copyright © 2024 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.common.utils;

import java.lang.reflect.Method;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;
import org.springframework.util.ReflectionUtils;
import com.dcep.common.annotation.CheckAccountTag;
import com.dcep.common.annotation.CheckAccountTag.Type;
import com.dcep.common.annotation.CheckAccountTags;
import com.dcep.common.model.EnvelopeDTO;
import com.dcep.common.model.GwDTO;
import com.dcep.common.model.soap.SoapBody;
import com.dcep.common.model.soap.SoapHeader;
import com.dcep.supergw.common.exception.GwException;
import com.dcepex.trace.support.TraceContext;
import mockit.Expectations;
import mockit.Mocked;

public class ValidateUtilsAccountTagTest {

    @Mocked
    private TraceContext traceContext;

    @Test
    public void validateDtoWithoutAccountTag() {
        EnvelopeDTO<GwDTO> envelope = new EnvelopeDTO<>();

        DemoDto demoDto = new DemoDto();
        WrapperField wrapperField = new WrapperField();
        wrapperField.setValue("111");
        demoDto.setField(wrapperField);

        envelope.setSoapBody(new SoapBody(demoDto));

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Method checkAccountTagMethod = ReflectionUtils.findMethod(ValidateUtils.class,
                        "checkAccountTag", EnvelopeDTO.class);
                ReflectionUtils.makeAccessible(checkAccountTagMethod);
                ReflectionUtils.invokeMethod(checkAccountTagMethod, null, envelope);
            }
        });
    }

    @Test
    public void validateAccountTagIsProdTrue() {
        new Expectations() {
            {
                TraceContext.get(anyString);
                result = "FALSE";
            }
        };

        EnvelopeDTO<GwDTO> envelope = new EnvelopeDTO<>();

        DemoDto demoDto = new DemoDto();
        WrapperField wrapperField = new WrapperField();
        wrapperField.setValue("111");
        demoDto.setField(wrapperField);

        envelope.setSoapBody(new SoapBody(demoDto));

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Method checkAccountTagMethod = ReflectionUtils.findMethod(ValidateUtils.class,
                        "checkAccountTag", EnvelopeDTO.class);
                ReflectionUtils.makeAccessible(checkAccountTagMethod);
                ReflectionUtils.invokeMethod(checkAccountTagMethod, null, envelope);
            }
        });
    }

    @Test
    public void validateAccountTagIsPerfTrue() {
        new Expectations() {
            {
                TraceContext.get(anyString);
                result = "TRUE";
            }
        };

        EnvelopeDTO<GwDTO> envelope = new EnvelopeDTO<>();

        DemoDto demoDto = new DemoDto();
        WrapperField wrapperField = new WrapperField();
        wrapperField.setValue("111T");
        demoDto.setField(wrapperField);

        envelope.setSoapBody(new SoapBody(demoDto));

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Method checkAccountTagMethod = ReflectionUtils.findMethod(ValidateUtils.class,
                        "checkAccountTag", EnvelopeDTO.class);
                ReflectionUtils.makeAccessible(checkAccountTagMethod);
                ReflectionUtils.invokeMethod(checkAccountTagMethod, null, envelope);
            }
        });
    }

    @Test
    public void validateAccountTagIsPerfFalse() {
        new Expectations() {
            {
                TraceContext.get(anyString);
                result = "TRUE";
            }
        };

        EnvelopeDTO<GwDTO> envelope = new EnvelopeDTO<>();

        DemoDto demoDto = new DemoDto();
        WrapperField wrapperField = new WrapperField();
        wrapperField.setValue("111");
        demoDto.setField(wrapperField);

        envelope.setSoapBody(new SoapBody(demoDto));

        Assertions.assertThrows(GwException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Method checkAccountTagMethod = ReflectionUtils.findMethod(ValidateUtils.class,
                        "checkAccountTag", EnvelopeDTO.class);
                ReflectionUtils.makeAccessible(checkAccountTagMethod);
                ReflectionUtils.invokeMethod(checkAccountTagMethod, null, envelope);
            }
        });
    }

    @Test
    public void validateAccountTagIsProdFalse() {
        new Expectations() {
            {
                TraceContext.get(anyString);
                result = "FALSE";
            }
        };

        EnvelopeDTO<GwDTO> envelope = new EnvelopeDTO<>();

        DemoDto demoDto = new DemoDto();
        WrapperField wrapperField = new WrapperField();
        wrapperField.setValue("111T");
        demoDto.setField(wrapperField);

        envelope.setSoapBody(new SoapBody(demoDto));

        Assertions.assertThrows(GwException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                Method checkAccountTagMethod = ReflectionUtils.findMethod(ValidateUtils.class,
                        "checkAccountTag", EnvelopeDTO.class);
                ReflectionUtils.makeAccessible(checkAccountTagMethod);
                ReflectionUtils.invokeMethod(checkAccountTagMethod, null, envelope);
            }
        });
    }

    @Test
    public void validateAccountTagValueIsNull() {
        EnvelopeDTO<GwDTO> envelope = new EnvelopeDTO<>();

        DemoDto demoDto = new DemoDto();

        envelope.setSoapBody(new SoapBody(demoDto));

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Method checkAccountTagMethod = ReflectionUtils.findMethod(ValidateUtils.class,
                        "checkAccountTag", EnvelopeDTO.class);
                ReflectionUtils.makeAccessible(checkAccountTagMethod);
                ReflectionUtils.invokeMethod(checkAccountTagMethod, null, envelope);
            }
        });
    }

    @Test
    public void validateAccountTagValueWithAccountTags() {
        EnvelopeDTO<GwDTO> envelope = new EnvelopeDTO<>();

        DemoDtoWithAccountTags demoDto = new DemoDtoWithAccountTags();

        envelope.setSoapBody(new SoapBody(demoDto));

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Method checkAccountTagMethod = ReflectionUtils.findMethod(ValidateUtils.class,
                        "checkAccountTag", EnvelopeDTO.class);
                ReflectionUtils.makeAccessible(checkAccountTagMethod);
                ReflectionUtils.invokeMethod(checkAccountTagMethod, null, envelope);
            }
        });
    }

    public static class DemoDtoWithoutAccountTag extends GwDTO {

        @Override
        public void init() {
            // TODO Auto-generated method stub

        }

        @Override
        public String fetchMsgId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean check(SoapHeader header) {
            // TODO Auto-generated method stub
            return false;
        }

    }

    @CheckAccountTag(type = Type.UID, path = {"field.value"})
    public static class DemoDto extends GwDTO {

        private WrapperField field;

        public WrapperField getField() {
            return field;
        }

        public void setField(WrapperField field) {
            this.field = field;
        }

        @Override
        public void init() {
            // TODO Auto-generated method stub

        }

        @Override
        public String fetchMsgId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean check(SoapHeader header) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public String toString() {
            return "DemoDto [field=" + field + "]";
        }

    }

    public static class WrapperField {
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "WrapperField [value=" + value + "]";
        }
    }

    @CheckAccountTags(@CheckAccountTag(type = Type.UID, path = {"field.value"}))
    @CheckAccountTag(type = Type.WID, path = {"field.value"})
    public static class DemoDtoWithAccountTags extends GwDTO {

        private WrapperField field;

        public WrapperField getField() {
            return field;
        }

        public void setField(WrapperField field) {
            this.field = field;
        }

        @Override
        public void init() {
            // TODO Auto-generated method stub

        }

        @Override
        public String fetchMsgId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public boolean check(SoapHeader header) {
            // TODO Auto-generated method stub
            return false;
        }

    }
}

