package edu.upc.fib.meetnrun.persistence;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.remote.ApiUtils;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

/**
 * Created by Awais Iqbal on 25/10/2017.
 */

public class GenericController implements IGenericController {

    private static GenericController instance = null;

    private GenericController(){
        mServices = ApiUtils.getSOService();
    }

    public static GenericController getInstance() {
        if(instance == null) {
            instance = new GenericController();
        }
        return instance;
    }

    private SOServices mServices;

    @Override
    public User getUser(int id) throws NotFoundException {
        User u = null;
        return null;
    }

    @Override
    public Meeting getMeeting(int id) throws NotFoundException {
        Meeting m = null;
        try {
            Response<Meeting> ret = mServices.getMeeting(id).execute();
            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m;
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
        List<Meeting> l = new ArrayList<>();

        try {
            Response<Meeting[]> res = mServices.getMeetings().execute();
            l.addAll(Arrays.asList(res.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return l;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> l = new ArrayList<>();

        try {
            Response<User[]> res = mServices.getUsers().execute();
            l.addAll(Arrays.asList(res.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return l;
    }

    @Override
    public Meeting createMeetingPublic(Integer id, String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException {
        Meeting m = new Meeting(0, title, description, _public, level, date, latitude, longitude);
        try {
            Response<Meeting> ret = mServices.createMeeting(m).execute();
            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m;
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String email, int postCode, String password, String question, String answer) throws ParamsException {
        return null;
    }

    @Override
    public String login(String username, String Password) {
        return null;
    }


}
