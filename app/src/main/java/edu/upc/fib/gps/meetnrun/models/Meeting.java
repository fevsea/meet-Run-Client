package edu.upc.fib.gps.meetnrun.models;

import java.util.Date;

/**
 * Created by Awais Iqbal on 17/10/2017.
 */

public class Meeting {
    private int id;
    private String title;
    private String description;
    private String creatorAuthor;
    private Date dateTime;
    private boolean isPublic;
    private int level;
    private float latitude;
    private float longitude;

    public Meeting(int id,String title, String description, String creatorAuthor, Date dateTime, boolean isPublic, int level, float latitude, float longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.creatorAuthor = creatorAuthor;
        this.dateTime = dateTime;
        this.isPublic = isPublic;
        this.level = level;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorAuthor() {
        return creatorAuthor;
    }

    public void setCreatorAuthor(String creatorAuthor) {
        this.creatorAuthor = creatorAuthor;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
