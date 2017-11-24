package edu.upc.fib.meetnrun.models;

import java.util.Date;

/**
 * Created by guillemcastro on 24/11/2017.
 */

public class Challenge {

    private User creator;
    private User challenged;
    private int distance;
    private int hours;
    private int minutes;
    private String dateDeadline;
    private String creationDate;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getChallenged() {
        return challenged;
    }

    public void setChallenged(User challenged) {
        this.challenged = challenged;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getDeadline() {
        return dateDeadline;
    }

    public void setDeadline(String deadline) {
        this.dateDeadline = deadline;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

}
