package edu.upc.fib.meetnrun.models;

/**
 * Created by eric on 21/11/17.
 */

public class Chat {

    private Integer id;
    private String chat;
    private User friend;
    private String last_converse;
    private String last_hour;

    public Chat() {
    }

    public Chat(Integer id, String chat, User friend, String last_converse, String last_hour) {
        this.id = id;
        this.chat = chat;
        this.friend = friend;
        this.last_converse = last_converse;
        this.last_hour = last_hour;
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

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
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
}
