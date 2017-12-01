package edu.upc.fib.meetnrun.models;

import android.util.Log;

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
    private List<Integer> listNumbMessages;

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
        listNumbMessages = new ArrayList<>();
        for (int i = 0; i < listUsersChat.size(); i++) listNumbMessages.add(0);
    }

    public Chat(Integer id, String chatName, List<User> listUsersChat, int type, Message message, Meeting meeting) {
        this.id = id;
        this.chatName = chatName;
        this.listUsersChat = listUsersChat;
        this.type = type;
        this.message = message;
        this.meeting = meeting;
        listNumbMessages = new ArrayList<>();
        for (int i = 0; i < listUsersChat.size(); i++) listNumbMessages.add(0);
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

    public int getListUsersChatSize() {
        return listUsersChat.size();
    }

    public void setListUsersChat(List<User> listUsersChat) {
        this.listUsersChat = listUsersChat;
    }

    public User getUserAtPosition(int i) {
        return listUsersChat.get(i);
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

    public Integer getNumbMessagesAtPosition(int i) {
        return listNumbMessages.get(i);
    }

    public void sumNumbMessagesAtPosition(int i) {
        Integer aux = listNumbMessages.get(i);
        this.listNumbMessages.remove(i);
        this.listNumbMessages.add(i, aux++);
    }

    public void setNumbMessagesAtPosition(int i, Integer x) {
        this.listNumbMessages.remove(i);
        this.listNumbMessages.add(i, x);
    }
}
