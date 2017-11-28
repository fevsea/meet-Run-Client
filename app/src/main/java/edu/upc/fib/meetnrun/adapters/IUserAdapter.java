package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface IUserAdapter {

    public List<User> getAllUsers(int page);

    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException;

    public User getUser(int targetUserId) throws NotFoundException;

    public boolean updateUser(User obj) throws ParamsException, NotFoundException, AutorizationException;

    public boolean deleteUserByID(int targetUserId) throws NotFoundException, AutorizationException;

    public List<Meeting> getUserMeetingsFilteres(int targetUserId, String filterByTime)throws AutorizationException, ParamsException;

    public List<Meeting> getUsersFutureMeetings(int targetUserId) throws AutorizationException, ParamsException;

    public List<Meeting> getUserPastMeetings(int targetUserId) throws AutorizationException, ParamsException;
}
