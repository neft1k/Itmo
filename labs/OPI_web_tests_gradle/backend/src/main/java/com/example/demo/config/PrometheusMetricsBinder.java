package com.example.demo.config;

import com.example.demo.metrics.ClickInterval;
import com.example.demo.metrics.PointsStats;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrometheusMetricsBinder {
    private final MeterRegistry registry;
    private final PointsStats points;
    private final ClickInterval clicks;

    public PrometheusMetricsBinder(MeterRegistry registry, PointsStats points, ClickInterval clicks) {
        this.registry = registry;
        this.points = points;
        this.clicks = clicks;
    }

    @PostConstruct
    void bind() {
        FunctionCounter.builder("lab_points_total", points, p -> (double) p.getTotalPoints()).register(registry);
        FunctionCounter.builder("lab_points_hits_total", points, p -> (double) p.getHits()).register(registry);
        FunctionCounter.builder("lab_points_misses_total", points, p -> (double) p.getMisses()).register(registry);
        Gauge.builder("lab_points_miss_streak", points, PointsStats::getCurrentMissStreak).register(registry);

        FunctionCounter.builder("lab_clicks_total", clicks, c -> (double) c.getClickCount()).register(registry);
        Gauge.builder("lab_click_interval_avg_seconds", clicks, c -> c.getAverageIntervalMillis() / 1000.0).register(registry);
        Gauge.builder("lab_click_interval_last_seconds", clicks, c -> c.getLastIntervalMillis() / 1000.0).register(registry);
    }
}
