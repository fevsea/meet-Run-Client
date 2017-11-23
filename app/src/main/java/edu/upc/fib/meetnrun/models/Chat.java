package edu.upc.fib.meetnrun.models;

import java.io.Serializable;

/**
 * Created by eric on 21/11/17.
 */

public class Chat{

    private Integer id;
    private String chat;
    private String userName;
    private String friendUsername;
    private String last_converse;
    private String last_hour;
    private String last_second;
    private String last_date;

    public Chat() {
    }

    public Chat(Integer id, String chat, String userName, String friendUsername, String last_converse, String last_hour, String last_second, String last_date) {
        this.id = id;
        this.chat = chat;
        this.userName = userName;
        this.friendUsername = friendUsername;
        this.last_converse = last_converse;
        this.last_hour = last_hour;
        this.last_second = last_second;
        this.last_date = last_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFriendUsername() {
        return friendUsername;
    }

    public void setFriendUsername(String friendUsername) {
        this.friendUsername = friendUsername;
    }

    public String getLast_converse() {
        return last_converse;
    }

    public void setLast_converse(String last_converse) {
        this.last_converse = last_converse;
    }

    public String getLast_hour() {
        return last_hour;
    }

    public void setLast_hour(String last_hour) {
        this.last_hour = last_hour;
    }

    public String getLast_second() {
        return last_second;
    }

    public void setLast_second(String last_second) {
        this.last_second = last_second;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }
}
