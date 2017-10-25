package edu.upc.fib.meetnrun.persistence;

import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Awais Iqbal on 25/10/2017.
 */

public class GenericController implements IGenericController {
    private GenericController(){}

    @Override
    public User getUser(int id) throws NotFoundException {
        return null;
    }

    @Override
    public Meeting getMeeting(int id) throws NotFoundException {
        return null;
    }

    @Override
    public boolean updateUser(User obj) throws ParamsException, NotFoundException {
        return false;
    }

    @Override
    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException {
        return false;
    }

    @Override
    public boolean deleteUserByID(int id) throws NotFoundException {
        return false;
    }

    @Override
    public boolean deleteMeetingByID(int id) throws NotFoundException {
        return false;
    }

    @Override
    public List<Meeting> getAllMeetings() {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public Meeting createMeetingPublic(String title, String description, Date meetingDateTime, int level, String latitude, String longitude) throws ParamsException {
        return null;
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String email, int postCode, String password) throws ParamsException {
        return null;
    }
}
