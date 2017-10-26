package edu.upc.fib.meetnrun.utils;

import java.util.Date;

/**
 * Created by Awais Iqbal on 25/10/2017.
 */

public class FormContainers {

    public static class CreateMeetingForm{
        public Integer id;
        public String title;
        public String description;
        public Boolean _public;
        public Integer level;
        public String date;
        public String latitude;
        public String longitude;

        public CreateMeetingForm(Integer id, String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) {
            this.id = id;
            this.title = title;
            this.description = description;
            this._public = _public;
            this.level = level;
            this.date = date;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    public static class RegisterUser{
        private String username;
        private String name;
        private String surname;
        private String email;
        private int postalCode;
        private String password;
        private String question;
        private String answer;

        public RegisterUser(String username, String name, String surname, String email, int postalCode, String password, String question, String answer) {
            this.username = username;
            this.name = name;
            this.surname = surname;
            this.email = email;
            this.postalCode = postalCode;
            this.password = password;
            this.question = question;
            this.answer = answer;
        }

        public String getUsername() {
            return username;
        }

        public String getName() {
            return name;
        }

        public String getSurname() {
            return surname;
        }

        public String getEmail() {
            return email;
        }

        public int getPostalCode() {
            return postalCode;
        }

        public String getPassword() {
            return password;
        }

        public String getQuestion() {
            return question;
        }

        public String getAnswer() {
            return answer;
        }
    }

    public static class LoginUser{
        private String username;
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
    }

}
