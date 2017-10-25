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
        List<Meeting> l = new ArrayList<>();

        try {
            Response<Meeting[]> res = mServices.getMeetings().execute();
            l.addAll(Arrays.asList(res.body()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < l.size(); i++) {
            Log.e("MAIN","i: "+i+" titlle: "+ l.get(i).getTitle());
            System.err.println("i: "+i+" titlle: "+ l.get(i).getTitle());
        }

        return l;
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

    @Override
    public String login(String username, String Password) {
        return null;
    }


}
