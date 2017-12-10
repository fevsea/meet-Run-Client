package edu.upc.fib.meetnrun.models;

import java.util.Date;
import java.util.List;

public class Meeting {
    private Integer id;
    private String title;
    private String description;
    private Boolean _public;
    private Integer level;
    private String date;
    private String latitude;
    private String longitude;
    private User owner;
    private List<User> participants;
    private Integer chatID;

    /**
     * No args constructor for use in serialization
     *
     */
    public Meeting() {
    }

    public Meeting(Integer id, String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude,Integer chatID) {
        this.id = id;
        this.title = title;
        this.description = description;
        this._public = _public;
        this.level = level;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chatID = chatID;
    }

    public Meeting(Integer id, String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude, Integer chatID, User owner, List<User> participants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this._public = _public;
        this.level = level;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chatID = chatID;
        this.owner = owner;
        this.participants = participants;
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

    public Boolean get_public() {
        return _public;
    }

    public void set_public(Boolean _public) {
        this._public = _public;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Integer getChatID() {
        return chatID;
    }

    public void setChatID(Integer chatID) {
        this.chatID = chatID;
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", _public=" + _public +
                ", level=" + level +
                ", date='" + date + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", owner=" + owner +
                ", participants=" + participants +
                '}';
    }
}