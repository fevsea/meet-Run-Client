package edu.upc.fib.meetnrun;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;


public class MockUserAdapter implements IUserAdapter {
    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException {
        return null;
    }

    @Override
    public User getUser(int targetUserId) throws NotFoundException {
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
