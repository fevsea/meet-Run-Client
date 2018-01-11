package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.FeedMeeting;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.Trophie;
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

    public List<Meeting> getUserMeetingsFilteres(int targetUserId, String filterByTime) throws AuthorizationException, ParamsException;

    public List<Meeting> getUsersFutureMeetings(int targetUserId) throws AuthorizationException, ParamsException;

    public List<Meeting> getUserPastMeetings(int targetUserId) throws AuthorizationException, ParamsException;

    public Statistics getUserStatisticsByID(int id) throws AuthorizationException;

    /**
     * Returns {@link List<Trophie>} following the next order:
     * "km_1": false,
     * "km_10": false,
     * "km_100": false,
     * "km_1000": false,
     * "km_10000": false,
     * "h_1": false,
     * "h_10": false,
     * "h_100": false,
     * "h_1000": false,
     * "meetings_1": true,
     * "meetings_5": false,
     * "meetings_10": false,
     * "meetings_20": false,
     * "meetings_50": false,
     * "level_1": true,
     * "level_5": false,
     * "level_10": false,
     * "level_25": false,
     * "level_40": false,
     * "level_50": false,
     * "max_distance_1": false,
     * "max_distance_5": false,
     * "max_distance_10": false,
     * "max_distance_21": false,
     * "max_distance_42": false,
     * "steps_10000": false,
     * "steps_20000": false,
     * "steps_25000": false,
     * "steps_50000": false,
     * "steps_100000": false,
     * "challenges_1": false,
     * "challenges_5": false,
     * "challenges_10": false,
     * "challenges_20": false,
     * "friends_1": true,
     * "friends_5": false,
     * "friends_10": false,
     * "friends_20": false
     * @param id user ID
     * @return List of trophies
     * @throws AuthorizationException Exception thrown when you don't have permission
     */
    public List<Trophie> getUserTrophieByID(int id) throws AuthorizationException;

    public List<FeedMeeting> getUsersFeed(int id) throws AuthorizationException;
}
