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
        FeedMeeting fm = new FeedMeeting(this.meeting.toGenericModel(), this.type, this.friend.toGenericModel(), this.tracking.toGenericModel());
        return fm;
    }
}
