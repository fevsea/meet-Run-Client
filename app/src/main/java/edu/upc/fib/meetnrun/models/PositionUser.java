package edu.upc.fib.meetnrun.models;

/**
 * Created by Javier on 05/01/2018.
 */

public class PositionUser extends Position {
    private String userID;//username
    private String firstName;
    private String lastName;

    public PositionUser(int zip, float distance, String userID, String firstName, String lastName) {
        super(zip, distance);
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "PositionUser{" +
                "userID='" + userID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}