package com.example.demo;

import com.example.demo.model.PointEntity;
import com.example.demo.repository.PointRepository;
import com.example.demo.service.PointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    @Test
    void testGetAllPoints() {
        List<PointEntity> points = Arrays.asList(
                new PointEntity(0.0, 0.0, 1.0, false),
                new PointEntity(1.0, 1.0, 2.0, true)
        );
        when(pointRepository.findAll()).thenReturn(points);

        List<PointEntity> result = pointService.getAllPoints();

        assertEquals(points, result, "Should return all points from repository");
        verify(pointRepository, times(1)).findAll();
    }

    @Test
    void testAddPoint_HitTrue() {
        double x = -1.0, y = 1.0, r = 2.0;
        PointEntity returned = new PointEntity(x, y, r, true);
        ArgumentCaptor<PointEntity> captor = ArgumentCaptor.forClass(PointEntity.class);
        when(pointRepository.save(any(PointEntity.class))).thenReturn(returned);

        PointEntity result = pointService.addPoint(x, y, r);

        verify(pointRepository).save(captor.capture());
        PointEntity captured = captor.getValue();
        assertEquals(x, captured.getX(), 0.0001);
        assertEquals(y, captured.getY(), 0.0001);
        assertEquals(r, captured.getR(), 0.0001);
        assertTrue(captured.isHit(), "Expected hit to be true");

        assertEquals(returned, result, "Should return the entity from repository");
    }

    @Test
    void testAddPoint_HitFalse() {
        double x = -2.0, y = -2.0, r = 1.0;
        PointEntity returned = new PointEntity(x, y, r, false);
        ArgumentCaptor<PointEntity> captor = ArgumentCaptor.forClass(PointEntity.class);
        when(pointRepository.save(any(PointEntity.class))).thenReturn(returned);

        PointEntity result = pointService.addPoint(x, y, r);

        verify(pointRepository).save(captor.capture());
        PointEntity captured = captor.getValue();
        assertEquals(x, captured.getX(), 0.0001);
        assertEquals(y, captured.getY(), 0.0001);
        assertEquals(r, captured.getR(), 0.0001);
        assertFalse(captured.isHit(), "Expected hit to be false");

        assertEquals(returned, result, "Should return the entity from repository");
    }
}
