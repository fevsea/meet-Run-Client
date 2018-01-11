package edu.upc.fib.meetnrun.models;


import android.support.annotation.Nullable;

public class FeedMeeting {

    public final static int PAST_FRIEND = 0;
    public final static int FUTURE_FRIEND_CREATED = 1;
    public final static int FUTURE_FRIEND_JOINED = 2;
    public final static int FUTURE_NEAR = 3;

    private Meeting meeting;
    private int type;

    @Nullable
    private User friend;

    @Nullable
    private TrackingData tracking;

    public FeedMeeting() {

    }


    public FeedMeeting(Meeting meeting, int type, User friend, TrackingData tracking) {
        this.meeting = meeting;
        this.type = type;
        this.friend = friend;
        this.tracking = tracking;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Nullable
    public User getFriend() {
        return friend;
    }

    public void setFriend(@Nullable User friend) {
        this.friend = friend;
    }

    @Nullable
    public TrackingData getTracking() {
        return tracking;
    }

    public void setTracking(@Nullable TrackingData tracking) {
        this.tracking = tracking;
    }
}
