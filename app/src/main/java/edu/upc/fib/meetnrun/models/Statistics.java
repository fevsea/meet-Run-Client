package edu.upc.fib.meetnrun.models;

/**
 * Created by Javier on 26/11/2017.
 */

public class Statistics {

    private float  avgSpeed;
    private int    totalSteps;
    private float  totalKm;
    private float  totalTimeMillis;
    private int    numberMeetings;
    private float  totalCalories;
    private float  maxSpeed;
    private float  minSpeed;
    private float  maxLength;
    private float  minLength;
    private float  maxTime;
    private float  minTime;
    private TrackingData tracking;

    public Statistics(){
        this.avgSpeed = 0;
        this.totalSteps = 0;
        this.totalKm = 0;
        this.totalTimeMillis = 0;
        this.numberMeetings = 0;
        this.totalCalories = 0;
        this.maxSpeed = 0;
        this.minSpeed = 0;
        this.maxLength = 0;
        this.minLength = 0;
        this.maxTime = 0;
        this.minTime = 0;
        this.tracking = null;
    }

    public Statistics(float avgSpeed, int totalSteps, float totalKm, float totalTimeMillis, int numberMeetings, float totalCalories, float maxSpeed, float minSpeed, float maxLength, float minLength, float maxTime, float minTime, TrackingData tracking) {
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

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public float getAvgTimePerKm(){
        return totalTimeMillis/totalKm;
    }

    public String getAvgTimePerKmInString(){
        return String.format("%sh %sm %ss", this.totalTimeMillis/(this.totalKm*1000));
    }

    public int getTotalSteps() {
        return totalSteps;
    }

    public float getTotalKm() {
        return totalKm;
    }

    public float getTotalTimeMillis(){
        return totalTimeMillis;
    }
    public String getTimeInString(float time) {
        float hours=time/3600000;
        float mins=(time%3600000)/60000;
        float secs=(time%60000)/1000;
        return String.format("%sh %sm %ss", (int) hours, (int) mins, (int) secs);
    }

    public int getNumberMeetings() {
        return numberMeetings;
    }

    public float getTotalCalories() {
        return totalCalories;
    }

    public float getRhythm(){
        return totalTimeMillis/(1000*totalKm);
    }

    public String getRhythmInString(){
        float secstotals=getRhythm();
        float hours=secstotals/3600;
        float mins=(secstotals%3600)/60;
        float secs=secstotals%60;
        return String.format("%sh %sm %ss", (int) hours, (int) mins, (int) secs);
    }

    public String getSpeedInString(float speed){
        return String.valueOf(speed) + " km/h";
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public float getMinSpeed() {
        return minSpeed;
    }

    public float getMaxLength() {
        return maxLength;
    }

    public float getMinLength() {
        return minLength;
    }

    public float getMaxTime() {
        return maxTime;
    }

    public float getMinTime() {
        return minTime;
    }

    public void setMaxSpeed(float maxSpeed) {
        if (maxSpeed > this.maxSpeed) this.maxSpeed = maxSpeed;
    }

    public void setMinSpeed(float minSpeed) {
        if (minSpeed > this.minSpeed) this.minSpeed = minSpeed;
    }

    public void setMaxLength(float maxLength) {
        if (maxLength > this.maxLength) this.maxLength = maxLength;
    }

    public void setMinLength(float minLength) {
        if (minLength > this.minLength) this.minLength = minLength;
    }

    public void setMaxTime(float maxTime) {
        if (maxTime > this.maxTime) this.maxTime = maxTime;
    }

    public void setMinTime(float minTime) {
        if (minTime < this.minTime) this.minTime = minTime;
    }


    public TrackingData getTracking() {
        return tracking;
    }

    public void setTracking(TrackingData tracking) {
        this.tracking = tracking;
    }



}
