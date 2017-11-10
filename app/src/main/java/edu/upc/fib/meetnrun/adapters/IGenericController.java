package edu.upc.fib.meetnrun.adapters;

import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 17/10/2017.
 */

public interface IGenericController {
    //MEETINGS
    public List<Meeting> getAllMeetings();

    public Meeting createMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException, AutorizationException;

    public Meeting getMeeting(int targetMeetingid) throws NotFoundException;

    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException, AutorizationException;

    public boolean deleteMeetingByID(int id) throws NotFoundException, AutorizationException;

    public List<User> getParticipantsFromMeeting(int targetMeetingId) throws AutorizationException, ParamsException;

    public boolean joinMeeting(int targetMeetingId) throws AutorizationException, ParamsException;

    public boolean leaveMeeting(int targetMeetingId) throws AutorizationException, ParamsException;

    //USERS
    public List<User> getAllUsers();

    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException;

    public User getUser(int targetUserId) throws NotFoundException;

    public boolean updateUser(User obj) throws ParamsException, NotFoundException, AutorizationException;

    public boolean deleteUserByID(int targetUserId) throws NotFoundException, AutorizationException;

    public List<Meeting> getUsersFutureMeetings(int targetUserId) throws AutorizationException, ParamsException;

    //LOGIN
    public String login(String username, String password) throws AutorizationException;

    public User getCurrentUser() throws AutorizationException;

    public boolean logout() throws AutorizationException;


    //FRIENDS
    public boolean addFriend(int targetUserId) throws AutorizationException, ParamsException;

    public List<User> getUserFriends() throws AutorizationException;

    public boolean removeFriend(int targetUserId) throws AutorizationException, ParamsException;

    public List<User> listFriendsOfUser(int targetUserId) throws AutorizationException, ParamsException;
}
