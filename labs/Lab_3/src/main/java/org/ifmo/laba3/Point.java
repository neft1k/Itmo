package org.ifmo.laba3;



public class Point {
    private double x;
    private double y;
    private double r;
    private String result;

    public Point(double x, double y, double r, String result) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.result = result;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getR() {
        return r;
    }

    public String getResult() {
        return result;
    }
}

