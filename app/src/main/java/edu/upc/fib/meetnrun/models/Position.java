package edu.upc.fib.meetnrun.models;

/**
 * Created by Javier on 05/01/2018.
 */

public class Position {
    private int position;
    private int zip;
    private float distance;

    public Position(int zip, float distance) {
        this.zip = zip;
        this.distance = distance;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Position{" +
                "position=" + position +
                ", zip=" + zip +
                ", distance=" + distance +
                '}';
    }
}

