package edu.upc.fib.meetnrun.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 21/11/17.
 */

public class Chat{

    private Integer id;
    private String chatName;
    private List<User> listUsersChat;
    private int type;
    private Message message;
    private Meeting meeting;
    private int numbNewMessages = 0;

    public Chat() {
        listUsersChat = new ArrayList<>();
    }

    public Chat(Integer id, String chatName, List<User> listUsersChat, int type, Message message) {
        this.id = id;
        this.chatName = chatName;
        this.listUsersChat = listUsersChat;
        this.type = type;
        this.message = message;
        this.meeting = null;
    }

    public Chat(Integer id, String chatName, List<User> listUsersChat, int type, Message message, Meeting meeting) {
        this.id = id;
        this.chatName = chatName;
        this.listUsersChat = listUsersChat;
        this.type = type;
        this.message = message;
        this.meeting = meeting;
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

    public List<User> getListUsersChat() {
        return listUsersChat;
    }

    public void setListUsersChat(List<User> listUsersChat) {
        this.listUsersChat = listUsersChat;
    }

    public User getUser1() {
        return listUsersChat.get(0);
    }

    public void setUser1(User user1) {
        listUsersChat.remove(0);
        listUsersChat.add(0, user1);
    }

    public User getUser2() {
        return listUsersChat.get(1);
    }

    public void setUser2(User user2) {
        listUsersChat.remove(1);
        listUsersChat.add(1, user2);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public int getNumbNewMessages() {
        return numbNewMessages;
    }

    public void setNumbNewMessages(int numbNewMessages) {
        this.numbNewMessages = numbNewMessages;
    }
}
