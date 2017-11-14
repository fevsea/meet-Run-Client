package edu.upc.fib.meetnrun;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.User;


public class MockFriendsAdapter implements IFriendsAdapter {
    @Override
    public boolean addFriend(int targetUserId) throws AutorizationException, ParamsException {
        return false;
    }

    @Override
    public List<User> getUserFriends() throws AutorizationException {
        return null;
    }

    @Override
    public boolean removeFriend(int targetUserId) throws AutorizationException, ParamsException {
        return false;
    }

    @Override
    public List<User> listFriendsOfUser(int targetUserId) throws AutorizationException, ParamsException {
        return null;
    }
}
