package edu.upc.fib.meetnrun.persistence;

import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import retrofit2.Callback;

/**
 * Created by Awais Iqbal on 17/10/2017.
 */

public interface IGenericController {
    public List<Meeting> getAllMeetings();
    public Meeting createMeetingPublic( String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException;
    public Meeting getMeeting(int id) throws NotFoundException;
    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException;
    public boolean deleteMeetingByID(int id) throws NotFoundException;
    public List<User> getAllUsers();
    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException;

    public User getUser(int id) throws NotFoundException;
    public boolean updateUser(User obj) throws ParamsException, NotFoundException;
    public boolean deleteUserByID(int id) throws NotFoundException;
    public String login(String username, String password);
    public User getCurrentUser();
    public boolean logout();
}