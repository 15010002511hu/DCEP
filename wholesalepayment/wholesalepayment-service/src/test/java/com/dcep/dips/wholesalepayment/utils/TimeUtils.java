package com.dcep.dips.wholesalepayment.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeUtils {
    public static void sleepSeconds(int seconds) {
        try {
            log.info("sleeping------------------------------------------------------------------------------------------------------------------");
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ignored) {
        }
    }
}
