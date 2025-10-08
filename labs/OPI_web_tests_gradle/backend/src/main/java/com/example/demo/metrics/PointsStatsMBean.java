package com.example.demo.metrics;

public interface PointsStatsMBean {
    long getTotalPoints();
    long getHits();
    long getMisses();
    int  getCurrentMissStreak();
    void reset();

    void recordClick(boolean hit);
}
