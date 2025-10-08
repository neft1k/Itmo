package org.ifmo.laba3;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Named
@RequestScoped
public class CoordinateBean {
    private double x;
    private double y;
    private double r;
    private boolean r1;
    private boolean r1_5;
    private boolean r2;
    private boolean r2_5;
    private boolean r3;
    private String result;
    // Список координат
    private List<Coordinate> coordinates = new ArrayList<>();

    private CoordinateData coordinateData = new CoordinateData();

    public boolean isR1() {
        return r1;
    }

    public void setR1(boolean r1) {
        this.r1 = r1;
    }

    public boolean isR1_5() {
        return r1_5;
    }

    public void setR1_5(boolean r1_5) {
        this.r1_5 = r1_5;
    }

    public boolean isR2() {
        return r2;
    }

    public void setR2(boolean r2) {
        this.r2 = r2;
    }

    public boolean isR2_5() {
        return r2_5;
    }

    public void setR2_5(boolean r2_5) {
        this.r2_5 = r2_5;
    }

    public boolean isR3() {
        return r3;
    }

    public void setR3(boolean r3) {
        this.r3 = r3;
    }
    public double getX() {
        return x;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
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

    // Геттер для списка координат
    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    // Метод для сохранения координат
    public void saveCoordinates() {
        Coordinate coordinate = new Coordinate();
        coordinate.setX(x);
        coordinate.setY(y);
        coordinate.setR(r);
        if ((x <= 0 && y <= 0 && x * x + y * y <= r * r / 4) || (x >= 0 && y <= 0 && y >= -r/2 && x <= r) || (x <= 0 && y >= 0 && y <= x * 2 + r)){
            coordinate.setResult("Пробил");
        }else{
            coordinate.setResult("Не пробил");
        }


        // Сохранение координаты в базу данных
        coordinateData.saveCoordinate(coordinate);

        // Обновляем список координат
        loadCoordinates();
    }
    public void setXAndUpdate(int xValue) {
        if (r1) {
            this.r = 1;
        }
        if (r1_5) {
            this.r = 1.5;
        }
        if (r2) {
            this.r = 2;
        }
        if (r2_5) {
            this.r = 2.5;
        }
        if (r3) {
            this.r = 3;
        }
        setX(xValue);
        saveCoordinates();
    }
    // Метод для загрузки всех координат из базы данных
    @PostConstruct
    public void loadCoordinates() {
        coordinates = coordinateData.getAllCoordinates();
    }
}




