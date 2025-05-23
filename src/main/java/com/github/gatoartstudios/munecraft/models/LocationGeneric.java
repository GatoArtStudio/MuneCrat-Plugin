package com.github.gatoartstudios.munecraft.models;

public class LocationGeneric {
    private double x;
    private double y;
    private double z;

    public LocationGeneric(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

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

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "LocationGeneric{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
