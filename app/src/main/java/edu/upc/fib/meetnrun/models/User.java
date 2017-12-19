package edu.upc.fib.meetnrun.models;

/**
 * Created by Awais Iqbal on 31/10/2017.
 */

public class User {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String postalCode;
    private String question;
    private Integer level;
    private boolean selected = false;
    private boolean isFriend = false;

    public User(Integer id, String username, String firstName, String lastName, String postalCode, String question,Integer level) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.postalCode = postalCode;
        this.question = question;
        this.level = level;
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

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isFriend() {
        return isFriend;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", question='" + question + '\'' +
                ", level=" + level +
                '}';
    }

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof User)) {
            return false;
        }
        User otherUser = (User)anObject;
        return otherUser.getId().equals(getId());
    }
}
