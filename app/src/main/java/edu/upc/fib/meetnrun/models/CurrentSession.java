package edu.upc.fib.meetnrun.models;

import edu.upc.fib.meetnrun.adapters.AdaptersContainer;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IChallengeAdapter;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.adapters.IRankingAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public class CurrentSession {

    private static final CurrentSession instance = new CurrentSession();
    private String token;
    private User currentUser;
    private AdaptersContainer adapterContainer;
    private Chat chat;
    private User friend;

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

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public AdaptersContainer getAdapterContainer() {
        return adapterContainer;
    }

    public void setAdapterContainer(AdaptersContainer adapterContainer) {
        this.adapterContainer = adapterContainer;
    }

    public IFriendsAdapter getFriendsAdapter() {
        return adapterContainer.getFriendsAdapter();
    }

    public ILoginAdapter getLoginAdapter() {
        return adapterContainer.getLoginAdapter();
    }

    public IMeetingAdapter getMeetingAdapter() {
        return adapterContainer.getMeetingAdapter();
    }

    public IUserAdapter getUserAdapter() {
        return adapterContainer.getUserAdapter();
    }

    public IChatAdapter getChatAdapter() {
        return adapterContainer.getChatAdapter();
    }

    public IChallengeAdapter getChallengeAdapter() {
        return adapterContainer.getChallengesAdapter();
    }

    public IRankingAdapter getRankingAdapter(){
        return adapterContainer.getRankingsAdapter();
    }

}
