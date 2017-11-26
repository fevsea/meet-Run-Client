package edu.upc.fib.meetnrun.models;

import java.util.Date;

/**
 * Created by guillemcastro on 24/11/2017.
 */

public class Challenge {

    private Integer id;
    private User creator;
    private User challenged;
    private int distance;
    private String dateDeadline;
    private String creationDate;
    private Integer creatorDistance;
    private Integer challengedDistance;

    public Challenge() {

    }

    public Challenge(Integer id, User creator, User challenged, int distance, String dateDeadline, String creationDate, Integer creatorDistance, Integer challengedDistance) {
        this.id = id;
        this.creator = creator;
        this.challenged = challenged;
        this.distance = distance;
        this.dateDeadline = dateDeadline;
        this.creationDate = creationDate;
        this.creatorDistance = creatorDistance;
        this.challengedDistance = challengedDistance;
    }

    public Challenge(User creator, User challenged, int distance, String dateDeadline) {
        this.creator = creator;
        this.challenged = challenged;
        this.distance = distance;
        this.dateDeadline = dateDeadline;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDateDeadline() {
        return dateDeadline;
    }

    public void setDateDeadline(String dateDeadline) {
        this.dateDeadline = dateDeadline;
    }

    public Integer getCreatorDistance() {
        return creatorDistance;
    }

    public void setCreatorDistance(Integer creatorDistance) {
        this.creatorDistance = creatorDistance;
    }

    public Integer getChallengedDistance() {
        return challengedDistance;
    }

    public void setChallengedDistance(Integer challengedDistance) {
        this.challengedDistance = challengedDistance;
    }

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
