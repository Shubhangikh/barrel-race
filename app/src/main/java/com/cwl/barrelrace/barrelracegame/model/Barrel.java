package com.cwl.barrelrace.barrelracegame.model;

import android.widget.ImageView;

public class Barrel {
    ImageView barrel;
    private boolean point1;
    private boolean point2;
    private boolean point3;
    private boolean point4;

    public Barrel(ImageView barrel, boolean point1, boolean point2, boolean point3, boolean point4) {
        this.barrel = barrel;
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.point4 = point4;
    }

    public ImageView getBarrel() {
        return barrel;
    }

    public void setBarrel(ImageView barrel) {
        this.barrel = barrel;
    }

    public boolean isPoint1() {
        return point1;
    }

    public void setPoint1(boolean point1) {
        this.point1 = point1;
    }

    public boolean isPoint2() {
        return point2;
    }

    public void setPoint2(boolean point2) {
        this.point2 = point2;
    }

    public boolean isPoint3() {
        return point3;
    }

    public void setPoint3(boolean point3) {
        this.point3 = point3;
    }

    public boolean isPoint4() {
        return point4;
    }

    public void setPoint4(boolean point4) {
        this.point4 = point4;
    }

    public boolean isEncircled(){
        if(point1 && point2 && point3 && point4) {
            return true;
        } else {
            return false;
        }
    }
}
