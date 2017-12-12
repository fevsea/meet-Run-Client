package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Friend;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface IFriendsAdapter {
    boolean addFriend(int targetUserId) throws AuthorizationException, ParamsException;

    List<Friend> getUserFriends(int page) throws AuthorizationException;

    boolean removeFriend(int targetUserId) throws AuthorizationException, ParamsException;

    List<Friend> listFriendsOfUser(int targetUserId, int page) throws AuthorizationException, ParamsException;

    List<Friend> listUserPendingFriends(int targetUserId, int page) throws AuthorizationException,NotFoundException;

    List<Friend> listUserAcceptedFriends(int targetUserId, int page) throws AuthorizationException,NotFoundException;
}
