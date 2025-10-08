package com.example.demo.service;

import com.example.demo.metrics.ClickInterval;
import com.example.demo.metrics.PointsStats;
import com.example.demo.model.PointEntity;
import com.example.demo.repository.PointRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PointService {

    private final PointRepository pointRepository;

    private final PointsStats pointsStats;
    private final ClickInterval clickInterval;

    public PointService(PointRepository pointRepository,
                        PointsStats pointsStats,
                        ClickInterval clickInterval) {
        this.pointRepository = pointRepository;
        this.pointsStats = pointsStats;
        this.clickInterval = clickInterval;
    }

    public List<PointEntity> getAllPoints() {
        return pointRepository.findAll();
    }

    public PointEntity addPoint(double x, double y, double r) {
        boolean hit = checkHit(x, y, r);

        clickInterval.recordClickNow();

        pointsStats.recordClick(hit);

        PointEntity p = new PointEntity(x, y, r, hit);
        return pointRepository.save(p);
    }

    private boolean checkHit(double x, double y, double r) {
        return ((x * x + y * y <= r * r) && (x <= 0) && (y >= 0)) ||
                ((x >= 0) && (y >= 0) && (x <= r) && (y <= r / 2)) ||
                ((y <= 0) && (x >= 0) && (y >= x - r));
    }
}




