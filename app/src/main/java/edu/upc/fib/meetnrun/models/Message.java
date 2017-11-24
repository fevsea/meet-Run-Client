package edu.upc.fib.meetnrun.models;

/**
 * Created by eric on 21/11/17.
 */

public class Message {

    private String message;
    private String name;
    private String hour;
    private String date;
    private String seconds;

    public Message() {
    }

    public Message(String message, String name, String hour, String date, String seconds) {
        this.message = message;
        this.name = name;
        this.hour = hour;
        this.date = date;
        this.seconds = seconds;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public boolean isSender() {
        if (name.equals(CurrentSession.getInstance().getCurrentUser().getUsername())) return true;
        return false;
    }

}
