package com.example.demo.metrics;

public interface ClickIntervalMBean {
    long   getClickCount();
    double getAverageIntervalMillis();
    double getLastIntervalMillis();
    void   reset();

    void recordClickNow();
}
