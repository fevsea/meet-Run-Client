package edu.upc.fib.meetnrun.models;

import java.util.Date;

/**
 * Created by eric on 21/11/17.
 */

public class Message {

    private String message;
    private String name;
    private String hour;
    private Date dateTime;

    public Message() {
    }

    public Message(String message, String name, String hour, Date dateTime) {
        this.message = message;
        this.name = name;
        this.hour = hour;
        this.dateTime = dateTime;
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

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isSender() {
        return name.equals(CurrentSession.getInstance().getCurrentUser().getUsername());
    }

}
