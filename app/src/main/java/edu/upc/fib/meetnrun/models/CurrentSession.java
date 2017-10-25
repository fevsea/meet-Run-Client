package edu.upc.fib.meetnrun.models;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public class CurrentSession {

    private static CurrentSession instance = null;
    private String token = null;
    private User currentUser;


    public static CurrentSession getInstance() {
        if(instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    private CurrentSession() {}
}
