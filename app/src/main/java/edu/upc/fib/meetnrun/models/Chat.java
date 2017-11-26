package edu.upc.fib.meetnrun.models;

import java.io.Serializable;

/**
 * Created by eric on 21/11/17.
 */

public class Chat{

    private Integer id;
    private String chatName;
    private User user1;
    private String user2;
    private Message message;

    public Chat() {
    }

    public Chat(Integer id, String chatName, User user1, String user2, Message message) {
        this.id = id;
        this.chatName = chatName;
        this.user1 = user1;
        this.user2 = user2;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
