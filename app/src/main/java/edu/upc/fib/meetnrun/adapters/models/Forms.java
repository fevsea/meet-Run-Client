package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;

/**
 * Created by Awais Iqbal on 26/10/2017.
 */

public class Forms {

    public static class ChangePassword {
        @SerializedName("old")
        @Expose
        private String oldPasword;
        @SerializedName("new")
        @Expose
        private String newPassword;

        public ChangePassword(String oldPasword, String newPassword) {
            this.oldPasword = oldPasword;
            this.newPassword = newPassword;
        }
    }

    public static class LoginUser {
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;

        public LoginUser(String username, String password) {
            this.username = username;
            this.password = password;
        }
    }

    public static class Token {
        @SerializedName("token")
        @Expose
        private String token;

        public Token(String token) {
            this.token = token;
        }

      public String getToken() {
        return token;
      }
    }

    public static class UserRegistration {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("first_name")
        @Expose
        private String firstName;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("postal_code")
        @Expose
        private String postalCode;
        @SerializedName("question")
        @Expose
        private String question;
        @SerializedName("answer")
        @Expose
        private String answer;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("level")
        @Expose
        private Integer level;

        public UserRegistration(Integer id, String username, String firstName, String lastName, String postalCode, String question, String answer, String password, Integer level) {
            this.id = id;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.postalCode = postalCode;
            this.question = question;
            this.answer = answer;
            this.password = password;
            this.level = level;
        }

        public UserRegistration(Integer id, String username, String firstName, String lastName, String postalCode, String question, String answer, String password) {
            this.id = id;
            this.username = username;
            this.firstName = firstName;
            this.lastName = lastName;
            this.postalCode = postalCode;
            this.question = question;
            this.answer = answer;
            this.password = password;
        }
    }

    public static class CreateMeeting {
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("public")
        @Expose
        private Boolean _public;
        @SerializedName("level")
        @Expose
        private Integer level;
        @SerializedName("date")
        @Expose
        private String date;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("chat")
        @Expose
        private Integer chatID;

        public CreateMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude, Integer chatID) {
            this.title = title;
            this.description = description;
            this._public = _public;
            this.level = level;
            this.date = date;
            this.latitude = latitude;
            this.longitude = longitude;
            this.chatID = chatID;
        }
    }

    public static class ChatCreateUpdate {

        @SerializedName("chatName")
        @Expose
        private String chatName;

        @SerializedName("listUsersChat")
        @Expose
        private List<Integer> listUsersChat;

        @SerializedName("type")
        @Expose
        private int type;

        @SerializedName("meeting")
        @Expose
        private Integer meetingToRelate;

        @SerializedName("lastMessage")
        @Expose
        private String lastMessage;

        @SerializedName("lastMessageUserName")
        @Expose
        private String lastMessageUsername;

        @SerializedName("lastDateTime")
        @Expose
        private String lastMessageDateTime;

        public ChatCreateUpdate(String chatName, List<Integer> listUsersChat, int type,
                                Integer meetingToRelate, String lastMessage, String lastMessageUsername,
                                Date lastMessageDateTime) {
            this.chatName = chatName;
            this.listUsersChat = listUsersChat;
            this.type = type;
            this.meetingToRelate = meetingToRelate;
            this.lastMessage = lastMessage;
            this.lastMessageUsername = lastMessageUsername;
            this.lastMessageDateTime = UtilsGlobal.formatDate(lastMessageDateTime);
        }

        public ChatCreateUpdate(Chat c) {
            this.chatName = c.getChatName();
            List<Integer> lic = new ArrayList<>();
            for (int i = 0; i < c.getListUsersChat().size(); i++) {
                lic.add(c.getListUsersChat().get(i).getId());
            }
            this. lastMessageUsername = c.getMessage().getName();
            this.listUsersChat = lic;
            this.type = c.getType();
            this.meetingToRelate = (c.getMeeting() != null) ? c.getMeeting().getId() : null;
            this.lastMessage = c.getMessage().getMessage();
            this.lastMessageDateTime = UtilsGlobal.formatDate(c.getMessage().getDateTime());
        }


    }

    public static class ChallengeCreator {
        @SerializedName("id")
        @Expose
        private Integer id;

        @SerializedName("creator")
        @Expose
        private Integer creator;

        @SerializedName("challenged")
        @Expose
        private Integer challenged;

        @SerializedName("distance")
        @Expose
        private Integer distance;

        @SerializedName("deadline")
        @Expose
        private String dateDeadline;

        public ChallengeCreator(Integer id, Integer creator, Integer challenged, Integer distance,
                                String dateDeadline) {
            this.id = id;
            this.creator = creator;
            this.challenged = challenged;
            this.distance = distance;
            this.dateDeadline = dateDeadline;
        }
    }
}

