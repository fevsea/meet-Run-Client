package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface IFriendsAdapter {
    public boolean addFriend(int targetUserId) throws AutorizationException, ParamsException;

    public List<User> getUserFriends() throws AutorizationException;

    public boolean removeFriend(int targetUserId) throws AutorizationException, ParamsException;

    public List<User> listFriendsOfUser(int targetUserId) throws AutorizationException, ParamsException;
}
