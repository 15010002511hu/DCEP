package com.dcep.supergw.common.record;

import lombok.Getter;

@Getter
public class Timer {

    private long timePoint = System.currentTimeMillis();
    private long begin = timePoint;

    private Timer() {
    }

    public static Timer getTimer() {
        return new Timer();
    }

    public static long currentTime() {
        return System.currentTimeMillis();
    }

    public long get() {
        return timePoint;
    }

    public long set(long time) {
        timePoint = time;
        return get();
    }

    public long timeing() {
        long begin = timePoint;
        return set(System.currentTimeMillis()) - begin;
    }

    public long cost() {
        return System.currentTimeMillis() - begin;
    }
}
