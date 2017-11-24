package edu.upc.fib.meetnrun.models;

import java.io.Serializable;

/**
 * Created by eric on 21/11/17.
 */

public class Chat{

    private Integer id;
    private String chat;
    private User userName1;
    private String userName2;
    private Message message;

    public Chat() {
    }

    public Chat(Integer id, String chat, User userName1, String userName2, Message message) {
        this.id = id;
        this.chat = chat;
        this.userName1 = userName1;
        this.userName2 = userName2;
        this.message = message;
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

    public User getUserName1() {
        return userName1;
    }

    public void setUserName1(User userName1) {
        this.userName1 = userName1;
    }

    public String getUserName2() {
        return userName2;
    }

    public void setUserName2(String userName2) {
        this.userName2 = userName2;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
