package edu.upc.fib.meetnrun.models;

/**
 * Created by eric on 21/11/17.
 */

public class Message {

    private String message;
    private String name;
    private String hour;

    public Message() {
    }

    public Message(String message, String name, String hour) {
        this.message = message;
        this.name = name;
        this.hour = hour;
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
}
