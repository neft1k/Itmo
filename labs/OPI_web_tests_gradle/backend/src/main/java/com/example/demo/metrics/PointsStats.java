package com.example.demo.metrics;

import javax.management.Notification;
import javax.management.NotificationBroadcasterSupport;
import java.util.concurrent.atomic.AtomicLong;

public class PointsStats extends NotificationBroadcasterSupport implements PointsStatsMBean {
    private final AtomicLong total  = new AtomicLong();
    private final AtomicLong hits   = new AtomicLong();
    private final AtomicLong misses = new AtomicLong();
    private volatile int missStreak = 0;
    private long seq = 0L;

    @Override
    public synchronized void recordClick(boolean hit) {
        total.incrementAndGet();
        if (hit) {
            hits.incrementAndGet();
            missStreak = 0;
        } else {
            misses.incrementAndGet();
            missStreak++;
            if (missStreak == 4) {
                Notification n = new Notification("com.example.demo.metrics.fourMissesInRow", this, ++seq, System.currentTimeMillis(), "Пользователь совершил 4 промаха подряд");
                sendNotification(n);
            }
        }
    }

    @Override
    public long getTotalPoints(){
        return total.get();
    }
    @Override
    public long getHits(){
        return hits.get();
    }
    @Override
    public long getMisses(){
        return misses.get();
    }
    @Override
    public int  getCurrentMissStreak() {
        return missStreak;
    }

    @Override
    public synchronized void reset() {
        total.set(0); hits.set(0); misses.set(0); missStreak = 0;
    }
}
