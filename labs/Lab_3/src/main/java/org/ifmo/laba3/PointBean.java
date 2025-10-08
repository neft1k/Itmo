package org.ifmo.laba3;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
@Named("pointBean")
@SessionScoped
public class PointBean implements Serializable {

    private double x;
    private double y;
    private double r;
    private List<Point> points = new ArrayList<>();

    // Публичный геттер и сеттер для x, y, r
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    // Публичный метод для добавления точки
    public void addPoint() {
        Point point = new Point(x, y, r, calculateResult());
        points.add(point);
        x = 0;  // Очистка значений после добавления
        y = 0;
        r = 0;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    private String calculateResult() {
        // Логика определения, попадает ли точка в область
        if (x >= 0 && y >= 0 && x <= r && y <= r) {
            return "Попадает";
        } else {
            return "Не попадает";
        }
    }
}



