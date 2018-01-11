package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.FeedMeeting;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public interface IUserAdapter {

    List<User> getAllUsers(int page);

    User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException;

    User getUser(int targetUserId) throws NotFoundException;

    boolean updateUser(User obj) throws ParamsException, NotFoundException, AuthorizationException;

    boolean deleteUserByID(int targetUserId) throws NotFoundException, AuthorizationException;

    public List<Meeting> getUserMeetingsFilteres(int targetUserId, String filterByTime)throws AuthorizationException, ParamsException;

    public List<Meeting> getUsersFutureMeetings(int targetUserId) throws AuthorizationException, ParamsException;

    public List<Meeting> getUserPastMeetings(int targetUserId) throws AuthorizationException, ParamsException;

    Statistics getUserStatisticsByID(int id) throws AuthorizationException;

    public List<FeedMeeting> getUsersFeed(int id) throws AuthorizationException;
}
