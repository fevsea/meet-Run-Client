package edu.upc.fib.meetnrun.models;

/**
 * Created by eric on 4/12/17.
 */

public class Friend {

    private User user;
    private User friend;
    private boolean accepted;

    public Friend() {
    }

    public Friend(User user, User friend, boolean accepted) {
        this.user = user;
        this.friend = friend;
        this.accepted = accepted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    @Override
    public String toString() {
        return "Friend{" +
                "user=" + user +
                ", friend=" + friend +
                ", accepted=" + accepted +
                '}';
    }
}
