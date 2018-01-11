package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.upc.fib.meetnrun.models.FeedMeeting;

public class FeedMeetingServer implements IServerModel {

    @SerializedName("meeting")
    @Expose
    private MeetingServer meeting;

    @SerializedName("type")
    @Expose
    private Integer type;

    @SerializedName("friend")
    @Expose
    private UserServer friend;

    @SerializedName("tracking")
    @Expose
    private TrackServer tracking;


    /**
     * No args constructor for use in serialization
     */
    public FeedMeetingServer() {
    }


    public FeedMeeting toGenericModel() {
        FeedMeeting fm = new FeedMeeting();
        if (this.meeting != null) {
            fm.setMeeting(this.meeting.toGenericModel());
        }
        if (this.type != null) {
            fm.setType(this.type);
        }
        if (this.friend != null) {
            fm.setFriend(this.friend.toGenericModel());
        }
        if (this.tracking != null) {
            fm.setTracking(this.tracking.toGenericModel());
        }
        return fm;
    }
}
