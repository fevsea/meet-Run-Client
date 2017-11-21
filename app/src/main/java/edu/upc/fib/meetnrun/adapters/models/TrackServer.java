package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.models.TrackingData;

/**
 * Created by Awais Iqbal on 10/11/2017.
 */

public class TrackServer implements IServerModel {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user")
    @Expose
    private Integer user;
    @SerializedName("meeting")
    @Expose
    private Integer meeting;
    @SerializedName("averagespeed")
    @Expose
    private Float averageSpeed;
    @SerializedName("distance")
    @Expose
    private Float distance;
    @SerializedName("steps")
    @Expose
    private Integer steps;
    @SerializedName("totalTimeMillis")
    @Expose
    private Long totalTimeMillis;
    @SerializedName("calories")
    @Expose
    private Float calories;
    @SerializedName("routePoints")
    @Expose
    private List<LatLng> routePoints;


    public TrackServer(TrackingData td) {
        this.user = td.getUserID();
        this.meeting = td.getMeetingID();
        this.averageSpeed = td.getAverageSpeed();
        this.calories = td.getCalories();
        this.distance = td.getDistance();
        this.steps = td.getSteps();
        this.totalTimeMillis = td.getTotalTimeMillis();
        this.routePoints = new ArrayList<>();

        List<com.google.android.gms.maps.model.LatLng> l = td.getRoutePoints();
        LatLng ln = null;
        for (int i = 0; i < l.size(); i++) {
            ln = new LatLng(l.get(i).latitude, l.get(i).longitude);
            this.routePoints.add(ln);
        }
    }

    public TrackServer(Integer user, Integer meeting, Float averageSpeed, Float distance, Integer steps, Long totalTimeMillis, Float calories, List<com.google.android.gms.maps.model.LatLng> routePoints) {
        this.user = user;
        this.meeting = meeting;
        this.averageSpeed = averageSpeed;
        this.distance = distance;
        this.steps = steps;
        this.totalTimeMillis = totalTimeMillis;
        this.calories = calories;
        this.routePoints = new ArrayList<>();
        List<com.google.android.gms.maps.model.LatLng> l = routePoints;
        LatLng ln = null;
        for (int i = 0; i < l.size(); i++) {
            ln = new LatLng(l.get(i).latitude, l.get(i).longitude);
            this.routePoints.add(ln);
        }

    }

    public TrackingData toGenericModel() {

        List<com.google.android.gms.maps.model.LatLng> lrp = new ArrayList<>();
        for (int i = 0; i < this.routePoints.size(); i++) {
            LatLng ownLatLng = this.routePoints.get(i);
            com.google.android.gms.maps.model.LatLng googleLatLng = new com.google.android.gms.maps.model.LatLng(ownLatLng.getLatitude(), ownLatLng.getLongitude());
            lrp.add(googleLatLng);
        }
        TrackingData ts = new TrackingData(this.user, this.meeting, this.averageSpeed, this.distance, this.steps, this.totalTimeMillis, this.calories, lrp);
        return ts;
    }


    public static class LatLng {
        @SerializedName("latitude")
        @Expose
        private Double latitude;
        @SerializedName("longitude")
        @Expose
        private Double longitude;

        public LatLng(Double latitude, Double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Double getLatitude() {
            return latitude;
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public Double getLongitude() {
            return longitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }
    }
}
