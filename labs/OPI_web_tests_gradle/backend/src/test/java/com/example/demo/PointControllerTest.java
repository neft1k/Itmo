package com.example.demo;

import com.example.demo.controller.PointController;
import com.example.demo.model.PointEntity;
import com.example.demo.service.PointService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointControllerTest {

    @Mock
    private PointService pointService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private PointController pointController;

    @Test
    void notLoggedIn_getPoints() {
        when(session.getAttribute("USER")).thenReturn(null);

        ResponseEntity<?> response = pointController.getAllPoints(session);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Unauthorized", response.getBody());
        verify(session).getAttribute("USER");
        verifyNoInteractions(pointService);
    }

    @Test
    void loggedIn_getPoints() {
        when(session.getAttribute("USER")).thenReturn("karim");
        PointEntity p1 = new PointEntity();
        PointEntity p2 = new PointEntity();
        List<PointEntity> stubList = List.of(p1, p2);
        when(pointService.getAllPoints()).thenReturn(stubList);

        ResponseEntity<?> response = pointController.getAllPoints(session);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(stubList, response.getBody());
        verify(session).getAttribute("USER");
        verify(pointService).getAllPoints();
    }

    @Test
    void notLoggedIn_addPoint() {
        when(session.getAttribute("USER")).thenReturn(null);

        ResponseEntity<?> response = pointController.addPoint(1.0, 2.0, 3.0, session);

        assertEquals(401, response.getStatusCodeValue());
        assertEquals("Unauthorized", response.getBody());
        verify(session).getAttribute("USER");
        verifyNoInteractions(pointService);
    }

    @Test
    void loggedIn_addPoint() {
        when(session.getAttribute("USER")).thenReturn("john");
        PointEntity newPoint = new PointEntity();
        when(pointService.addPoint(1.0, 2.0, 3.0)).thenReturn(newPoint);

        ResponseEntity<?> response = pointController.addPoint(1.0, 2.0, 3.0, session);

        assertEquals(200, response.getStatusCodeValue());
        assertSame(newPoint, response.getBody());
        verify(session).getAttribute("USER");
        verify(pointService).addPoint(1.0, 2.0, 3.0);
    }
}
