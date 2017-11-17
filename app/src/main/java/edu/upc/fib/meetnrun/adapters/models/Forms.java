package edu.upc.fib.meetnrun.adapters.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

        public String getOldPasword() {
            return oldPasword;
        }

        public void setOldPasword(String oldPasword) {
            this.oldPasword = oldPasword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
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

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
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

        public void setToken(String token) {
            this.token = token;
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

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
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

        public CreateMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) {
            this.title = title;
            this.description = description;
            this._public = _public;
            this.level = level;
            this.date = date;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean get_public() {
            return _public;
        }

        public void set_public(Boolean _public) {
            this._public = _public;
        }

        public Integer getLevel() {
            return level;
        }

        public void setLevel(Integer level) {
            this.level = level;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }
    }

}

