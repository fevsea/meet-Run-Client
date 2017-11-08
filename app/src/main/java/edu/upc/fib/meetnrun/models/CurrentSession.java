package edu.upc.fib.meetnrun.models;

import edu.upc.fib.meetnrun.adapters.AdaptersContainer;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public class CurrentSession {

    private static final CurrentSession instance = new CurrentSession();
    private String token;
    private User currentUser;
    private AdaptersContainer adapterContainer;

    private CurrentSession() {
        adapterContainer = AdaptersContainer.getInstance();
    }

    public static CurrentSession getInstance() {
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

    public AdaptersContainer getAdapterContainer() {
        return adapterContainer;
    }

    public void setAdapterContainer(AdaptersContainer adapterContainer) {
        this.adapterContainer = adapterContainer;
    }

    public void get

}
