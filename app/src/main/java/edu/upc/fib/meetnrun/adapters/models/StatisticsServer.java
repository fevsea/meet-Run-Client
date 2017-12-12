package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.TrackingData;

/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public class StatisticsServer implements IServerModel {
  @SerializedName("averagespeed")
  @Expose
  private float avgSpeed;

  @SerializedName("steps")
  @Expose
  private int totalSteps;

  @SerializedName("distance")
  @Expose
  private float totalKm;

  @SerializedName("totalTimeMillis")
  @Expose
  private float totalTimeMillis;

  @SerializedName("meetingsCompletats")
  @Expose
  private int numberMeetings;

  @SerializedName("calories")
  @Expose
  private float totalCalories;

  @SerializedName("maxAverageSpeed")
  @Expose
  private float maxSpeed;

  @SerializedName("minAverageSpeed")
  @Expose
  private float minSpeed;

  @SerializedName("maxDistance")
  @Expose
  private float maxLength;

  @SerializedName("minDistance")
  @Expose
  private float minLength;

  @SerializedName("maxDuration")
  @Expose
  private float maxTime;

  @SerializedName("minDuration")
  @Expose
  private float minTime;

  @SerializedName("lastTracking")
  @Expose
  private TrackingData tracking;

  public StatisticsServer(Statistics st) {
    this.avgSpeed = st.getAvgSpeed();
    this.totalSteps = st.getTotalSteps();
    this.totalKm = st.getTotalKm();
    this.totalTimeMillis = st.getTotalTimeMillis();
    this.numberMeetings = st.getNumberMeetings();
    this.totalCalories = st.getTotalCalories();
    this.maxSpeed = st.getMaxSpeed();
    this.minSpeed = st.getMinSpeed();
    this.maxLength = st.getMaxLength();
    this.minLength = st.getMinLength();
    this.maxTime = st.getMaxTime();
    this.minTime = st.getMinTime();
    this.tracking = st.getTracking();
  }

  public StatisticsServer(float avgSpeed, int totalSteps, float totalKm, float totalTimeMillis, int numberMeetings, float totalCalories, float maxSpeed, float minSpeed, float maxLength, float minLength, float maxTime, float minTime, TrackingData tracking) {
    this.avgSpeed = avgSpeed;
    this.totalSteps = totalSteps;
    this.totalKm = totalKm;
    this.totalTimeMillis = totalTimeMillis;
    this.numberMeetings = numberMeetings;
    this.totalCalories = totalCalories;
    this.maxSpeed = maxSpeed;
    this.minSpeed = minSpeed;
    this.maxLength = maxLength;
    this.minLength = minLength;
    this.maxTime = maxTime;
    this.minTime = minTime;
    this.tracking = tracking;
  }


  @Override
  public Statistics toGenericModel() {
    Statistics s = new Statistics(this.avgSpeed, this.totalSteps, this.totalKm, this.totalTimeMillis, this.numberMeetings,
      this.totalCalories, this.maxSpeed, this.minSpeed, this.maxLength, this.minLength, this.maxTime,
      this.minTime, this.tracking);
    return s;
  }
}
