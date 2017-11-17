package edu.upc.fib.meetnrun;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;


public class MockUserAdapter implements IUserAdapter {

    private static List<User> l;
    private static int cont;
    private static MockUserAdapter instance = null;

    public static MockUserAdapter getInstance() {
        if (instance == null) {
            instance = new MockUserAdapter();
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
    public List<User> getAllUsers() {
        return l;
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException {
        return null;
    }

    @Override
    public User getUser(int targetUserId) throws NotFoundException {
        for (User user : l) {
            if (user.getId() == targetUserId) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean updateUser(User obj) throws ParamsException, NotFoundException, AutorizationException {
        return false;
    }

    @Override
    public boolean deleteUserByID(int targetUserId) throws NotFoundException, AutorizationException {
        return false;
    }

    @Override
    public List<Meeting> getUsersFutureMeetings(int targetUserId) throws AutorizationException, ParamsException {
        return null;
    }
}
