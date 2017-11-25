package edu.upc.fib.meetnrun.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by guillemcastro on 03/11/2017.
 */

public class TrackingData implements Parcelable, Cloneable {

    public TrackingData(float averageSpeed, float distance, int steps, float calories ,List<LatLng> routePoints, long totalTimeMillis) {
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.steps = steps;
        this.routePoints = routePoints;
        this.totalTimeMillis = totalTimeMillis;
        this.calories = calories;
    }
    private int userID;
    private  int meetingID;
    private float averageSpeed;
    private float distance;
    private int steps;
    private long totalTimeMillis;
    private float calories;
    private List<LatLng> routePoints;

    protected TrackingData(Parcel in) {
        averageSpeed = in.readFloat();
        distance = in.readFloat();
        steps = in.readInt();
        totalTimeMillis = in.readLong();
        calories = in.readFloat();
        routePoints = in.createTypedArrayList(LatLng.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(averageSpeed);
        dest.writeFloat(distance);
        dest.writeInt(steps);
        dest.writeLong(totalTimeMillis);
        dest.writeFloat(calories);
        dest.writeTypedList(routePoints);
    }

    public TrackingData(int userID, int meetingID, float averageSpeed, float distance, int steps, long totalTimeMillis, float calories, List<LatLng> routePoints) {
        this.userID = userID;
        this.meetingID = meetingID;
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.steps = steps;
        this.totalTimeMillis = totalTimeMillis;
        this.calories = calories;
        this.routePoints = routePoints;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TrackingData> CREATOR = new Creator<TrackingData>() {
        @Override
        public TrackingData createFromParcel(Parcel in) {
            return new TrackingData(in);
        }

        @Override
        public TrackingData[] newArray(int size) {
            return new TrackingData[size];
        }
    };

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

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return "avSpeed: " + averageSpeed + " distance: " + distance + " steps: " + steps + " time: " + totalTimeMillis;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getMeetingID() {
        return meetingID;
    }

    public void setMeetingID(int meetingID) {
        this.meetingID = meetingID;
    }
}
