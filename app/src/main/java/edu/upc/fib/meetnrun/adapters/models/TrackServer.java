package edu.upc.fib.meetnrun.adapters.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Awais Iqbal on 10/11/2017.
 */

public class TrackServer {
    @SerializedName("id")
    @Expose
    private Integer id;
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
    private Integer totalTimeMillis;
    @SerializedName("calories")
    @Expose
    private Float calories;
    @SerializedName("routePoints")
    @Expose
    private List<LatLng> routePoints;
}
