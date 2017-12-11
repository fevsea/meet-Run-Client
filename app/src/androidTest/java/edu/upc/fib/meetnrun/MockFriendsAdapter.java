/*package edu.upc.fib.meetnrun;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.User;


public class MockFriendsAdapter implements IFriendsAdapter {

    private static List<User> l;
    private static int cont;
    private static MockFriendsAdapter instance = null;

    public static MockFriendsAdapter getInstance() {
        if (instance == null) {
            instance = new MockFriendsAdapter();
            cont = 2;
            l = new ArrayList<>();
            l.add(new User(cont, "ericR", "Eric", "Rodriguez", "08830", "question", 2));
            cont++;
            l.add(new User(cont, "awais", "Awais", "Iqbal", "08820", "question", 1));
            cont++;
            l.add(new User(cont, "marcP", "Marc", "Paricio", "08630", "question", 4));
            cont++;
        }
        return instance;
    }

    @Override
    public boolean addFriend(int targetUserId) throws AutorizationException, ParamsException {
        l.add(new User(cont, "monica", "Monica", "Follana", "06830", "question", 7));
        cont++;
        return true;
    }

    @Override
    public List<User> getUserFriends() throws AutorizationException {
        return l;
    }

    @Override
    public boolean removeFriend(int targetUserId) throws AutorizationException, ParamsException {
        for (int i = 0; i < l.size(); i++) {
            if (l.get(i).getId() == targetUserId) {
                l.remove(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<User> listFriendsOfUser(int targetUserId) throws AutorizationException, ParamsException {
        return null;
    }
}*/
