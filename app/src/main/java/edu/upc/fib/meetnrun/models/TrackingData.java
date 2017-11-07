package edu.upc.fib.meetnrun.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by guillemcastro on 03/11/2017.
 */

public class TrackingData {

    public TrackingData(float averageSpeed, float distance, int steps, List<LatLng> routePoints, long totalTimeMillis) {
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.steps = steps;
        this.routePoints = routePoints;
        this.totalTimeMillis = totalTimeMillis;
    }

    private float averageSpeed;
    private float distance;
    private int steps;
    private long totalTimeMillis;
    private List<LatLng> routePoints;

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public void setAverageSpeed(float averageSpeed) {
        this.averageSpeed = averageSpeed;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public List<LatLng> getRoutePoints() {
        return routePoints;
    }

    public void setRoutePoints(List<LatLng> routePoints) {
        this.routePoints = routePoints;
    }

    public long getTotalTimeMillis() {
        return totalTimeMillis;
    }

    public void setTotalTimeMillis(long totalTimeMillis) {
        this.totalTimeMillis = totalTimeMillis;
    }

    @Override
    public String toString() {
        return "avSpeed: " + averageSpeed + " distance: " + distance + " steps: " + steps + " time: " + totalTimeMillis;
    }

}
