package com.lrptest.daemon;

interface ILrpBoundService {
    void measure(long nanoTime);

    void sendInMs(long nanoTime, long ms);

    void reduceDozeAndSchedule(long nanoTime, long ms);

    void startParInf();
}
