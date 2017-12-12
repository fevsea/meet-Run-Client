package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;

public class MeetingServer implements IServerModel {

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
    @SerializedName("owner")
    @Expose
    private UserServer owner;
    @SerializedName("participants")
    @Expose
    private List<UserServer> participants;
    @SerializedName("chat")
    @Expose
    private Integer chatID;

    /**
     * No args constructor for use in serialization
     */
    public MeetingServer() {
    }

    public MeetingServer(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude, Integer chatID) {
        this.title = title;
        this.description = description;
        this._public = _public;
        this.level = level;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.chatID = chatID;
    }

    public MeetingServer(Integer id, String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude, Integer chatID, UserServer owner, List<UserServer> participants) {
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

    public MeetingServer(Meeting m) {
        super();
        this.id = m.getId();
        this.title = m.getTitle();
        this.description = m.getDescription();
        this._public = m.getPublic();
        this.level = m.getLevel();
        this.date = m.getDate();
        this.latitude = m.getLatitude();
        this.longitude = m.getLongitude();
        this.chatID = m.getChatID();
        this.owner = new UserServer(m.getOwner());
        List<UserServer> us = new ArrayList<>();
        List<User> u = m.getParticipants();
        for (int i = 0; i < u.size(); i++) {
            us.add(new UserServer(u.get(i)));
        }
        this.participants = us;
    }

    public Meeting toGenericModel() {
        List<User> ul = new ArrayList<>();
        if (participants != null) {
            List<UserServer> sl = participants;
            for (int i = 0; i < sl.size(); i++) {
                ul.add(sl.get(i).toGenericModel());
            }
        }
        return new Meeting(id, title, description, _public, level, date, latitude, longitude,chatID ,owner.toGenericModel(), ul);
    }
}
