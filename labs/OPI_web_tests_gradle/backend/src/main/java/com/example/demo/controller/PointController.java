package com.example.demo.controller;

import com.example.demo.model.PointEntity;
import com.example.demo.service.PointService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/points")
@CrossOrigin(origins = "http://localhost:8081", allowCredentials = "true")
public class PointController {

    private final PointService pointService;

    public PointController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping
    public ResponseEntity<?> getAllPoints(HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        List<PointEntity> points = pointService.getAllPoints();
        return ResponseEntity.ok(points);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPoint(@RequestParam double x, @RequestParam double y, @RequestParam double r, HttpSession session) {
        if (session.getAttribute("USER") == null) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
        PointEntity p = pointService.addPoint(x, y, r);
        return ResponseEntity.ok(p);
    }
}

