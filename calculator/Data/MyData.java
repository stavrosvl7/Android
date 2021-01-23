package com.unipi.stavrosvl7.calculator.Data;

public class MyData {
    private final double x;
    private final double lastX;

    public MyData(double x, double lastx) {
        this.x = x;
        this.lastX = lastx;
    }

    public double getX() {
        return x;
    }

    public double getLastX() {
        return lastX;
    }

}
