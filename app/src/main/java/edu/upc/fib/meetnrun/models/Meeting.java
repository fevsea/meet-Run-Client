package edu.upc.fib.meetnrun.models;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Meeting {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("public")
    @Expose
    private Boolean _public;
    @SerializedName("level")
    @Expose
    private Integer level;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;

    /**
     * No args constructor for use in serialization
     *
     */
    public Meeting() {
    }

    /**
     *
     * @param id
     * @param title
     * @param level
     * @param _public
     * @param description
     * @param longitude
     * @param latitude
     * @param date
     */
    public Meeting(Integer id, String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this._public = _public;
        this.level = level;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Boolean getPublic() {
        return _public;
    }

    public void setPublic(Boolean _public) {
        this._public = _public;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public Date getDateTime() {
        return new Date();
    }

    public void setDateTime(Date d){

    }
}