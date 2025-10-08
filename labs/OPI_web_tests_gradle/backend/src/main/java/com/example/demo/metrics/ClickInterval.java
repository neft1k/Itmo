package com.example.demo.metrics;

public class ClickInterval implements ClickIntervalMBean {
    private long   clickCount = 0;
    private long   lastClickNanos = -1;
    private long   intervalsCount = 0;
    private double totalIntervalMillis = 0.0;
    private double lastIntervalMillis  = 0.0;

    @Override
    public synchronized void recordClickNow() {
        long now = System.nanoTime();
        clickCount++;
        if (lastClickNanos != -1) {
            long diffNs = now - lastClickNanos;
            lastIntervalMillis = diffNs / 1_000_000.0;
            totalIntervalMillis += lastIntervalMillis;
            intervalsCount++;
        }
        lastClickNanos = now;
    }

    @Override
    public synchronized long getClickCount() {
        return clickCount;
    }
    @Override
    public synchronized double getAverageIntervalMillis() {
        return intervalsCount == 0 ? 0.0 : totalIntervalMillis / intervalsCount;
    }
    @Override
    public synchronized double getLastIntervalMillis() {
        return lastIntervalMillis;
    }

    @Override
    public synchronized void reset() {
        clickCount = 0;
        lastClickNanos = -1;
        intervalsCount = 0;
        totalIntervalMillis = 0.0;
        lastIntervalMillis = 0.0;
    }
}
