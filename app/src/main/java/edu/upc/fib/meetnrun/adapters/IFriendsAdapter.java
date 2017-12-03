package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface IFriendsAdapter {
    boolean addFriend(int targetUserId) throws AutorizationException, ParamsException;

    List<User> getUserFriends(int page) throws AutorizationException;

    boolean removeFriend(int targetUserId) throws AutorizationException, ParamsException;

    List<User> listFriendsOfUser(int targetUserId, int page) throws AutorizationException, ParamsException;
}
