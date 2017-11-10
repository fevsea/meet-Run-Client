package edu.upc.fib.meetnrun.models;

import edu.upc.fib.meetnrun.adapters.AdaptersContainer;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;

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
    public IFriendsAdapter getFriendsAdapter(){
        return adapterContainer.getFriendsAdapter();
    }

    public ILoginAdapter getLoginAdapter(){
        return adapterContainer.getLoginAdapter();
    }

    public IMeetingAdapter getMeetingAdapter(){
        return adapterContainer.getMeetingAdapter();
    }
    public IUserAdapter getUserAdapter(){
        return adapterContainer.getUserAdapter();
    }

        public void setController(IGenericController controller) {
            this.controller = controller;
        }

}
