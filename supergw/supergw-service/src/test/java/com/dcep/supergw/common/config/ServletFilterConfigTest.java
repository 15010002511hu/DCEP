/*
 * pbcdci.cn Inc. Copyright © 2023 PBCDCI All Rights Reserved.
 */
package com.dcep.supergw.common.config;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

public class ServletFilterConfigTest {

    @Test
    public void test_servlet_filter() {
        ServletFilterConfig configurtion = new ServletFilterConfig();

        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                configurtion.registSoapFilter();
                configurtion.registPathPatternFilter();
                configurtion.registEnvFlagFilter();
                configurtion.registFlowFilter();
            }
        });
    }
}
