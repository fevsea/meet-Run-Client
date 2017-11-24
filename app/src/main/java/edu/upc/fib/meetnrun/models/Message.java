package edu.upc.fib.meetnrun.models;

import java.util.Date;

/**
 * Created by eric on 21/11/17.
 */

public class Message {

    private String message;
    private String name;
    private String hour;
    private Date time;

    public Message() {
    }

    public Message(String message, String name, String hour, Date time) {
        this.message = message;
        this.name = name;
        this.hour = hour;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isSender() {
        if (name.equals(CurrentSession.getInstance().getCurrentUser().getUsername())) return true;
        return false;
    }

}
