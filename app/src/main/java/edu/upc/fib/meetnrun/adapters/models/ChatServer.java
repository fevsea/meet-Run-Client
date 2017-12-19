package edu.upc.fib.meetnrun.adapters.models;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;

/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public class ChatServer implements IServerModel {

    @SerializedName("pk")
    @Expose
    private int id;

    @SerializedName("chatName")
    @Expose
    private String chatName;

    @SerializedName("listUsersChat")
    @Expose
    private List<UserServer> participantUsers;

    @SerializedName("type")
    @Expose
    private int type;

    @SerializedName("meeting")
    @Expose
    private MeetingServer meetingToRelate;

    @SerializedName("lastMessage")
    @Expose
    private String lastMessage;

    @SerializedName("lastMessageUserName")
    @Expose
    private int lastMessageUsernamePosition;

    @SerializedName("lastDateTime")
    @Expose
    private String lastMessageDateTime;

    public ChatServer(int id, String chatName, List<UserServer> participantUsers, int type,
                      MeetingServer meetingToRelate, String lastMessage, int lastMessageUsernamePosition,
                      Date lastMessageDateTime) {
        this.id = id;
        this.chatName = chatName;
        this.participantUsers = participantUsers;
        this.type = type;
        this.meetingToRelate = meetingToRelate;
        this.lastMessage = lastMessage;
        this.lastMessageUsernamePosition = lastMessageUsernamePosition;
        this.lastMessageDateTime = UtilsGlobal.formatDate(lastMessageDateTime);
    }

    public ChatServer(Chat c) {
        List<UserServer> lus = new ArrayList<>();
        for (int i = 0; i < c.getListUsersChat().size(); i++) {

            lus.add(new UserServer(c.getListUsersChat().get(i)));

            if (c.getMessage().getName().equals(c.getListUsersChat().get(i).getUsername())) {
                this.lastMessageUsernamePosition = i;
            }
        }
        this.participantUsers = lus;
        this.id = c.getId();
        this.chatName = c.getChatName();
        this.type = c.getType();
        this.meetingToRelate = new MeetingServer(c.getMeeting());
        this.lastMessage = c.getMessage().getMessage();
        this.lastMessageDateTime = UtilsGlobal.formatDate(c.getMessage().getDateTime());

    }

    @Override
    public Chat toGenericModel() {
        List<User> lu = new ArrayList<>();
        for (int i = 0; i < participantUsers.size(); i++) {
            lu.add(participantUsers.get(i).toGenericModel());
        }
        Log.e("UTILS","lastMessageDateTime = " + lastMessageDateTime);
        Date d = UtilsGlobal.parseDate(this.lastMessageDateTime);
        Message m = new Message(this.lastMessage, participantUsers.get(lastMessageUsernamePosition).getUsername(), d);
        Chat c = null;
        if (meetingToRelate == null) {
            c = new Chat(this.id, this.chatName, lu, this.type, m, null);
        }
        else {
            c = new Chat(this.id, this.chatName, lu, this.type, m, this.meetingToRelate.toGenericModel());
        }

        return c;
    }
}
