package edu.upc.fib.meetnrun.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.persistenceModels.MeetingServer;
import edu.upc.fib.meetnrun.persistence.persistenceModels.UserServer;
import edu.upc.fib.meetnrun.persistence.persistenceModels.Forms;
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
        UserServer us = null;
        try {
            Response<UserServer> ret = mServices.getUser(id).execute();
            us = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return us.toGenericModel();
    }

    @Override
    public Meeting getMeeting(int id) throws NotFoundException {
        MeetingServer m = null;
        try {
            Response<MeetingServer> ret = mServices.getMeeting(id).execute();

            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m.toGenericModel();
    }

    @Override
    public boolean updateUser(User obj) throws ParamsException, NotFoundException {
        boolean ok = false;
        UserServer us = new UserServer(obj);
        try {
            Response<Void> res = mServices.updateUser(obj.getId(),us).execute();
            if (res.isSuccessful()) ok = true;
            //TODO check not foud exception
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException {
        boolean ok = false;
        MeetingServer ms = new MeetingServer(obj);
        try {
            Response<Void> res = mServices.updateMeeting(obj.getId(),ms).execute();
            if (res.isSuccessful()) ok = true;
            //TODO check not foud exception
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteUserByID(int id) throws NotFoundException {
        boolean ok = false;
        try {
            Response<Void> res = mServices.deleteUser(id).execute();
            if (res.isSuccessful()) ok = true;
            //TODO check not foud exception
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public boolean deleteMeetingByID(int id) throws NotFoundException {
        boolean ok = false;
        try {
            Response<Void> res = mServices.deletetMeeting(id).execute();
            if (res.isSuccessful()) ok = true;
            //TODO check not foud exception
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }

    @Override
    public List<Meeting> getAllMeetings() {
        List<Meeting> l = new ArrayList<>();
        try {
            Response<MeetingServer[]> res = mServices.getMeetings().execute();
            MeetingServer[] array = res.body();
            for (int i = 0; i < array.length; i++) {
                l.add(array[i].toGenericModel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> l = new ArrayList<>();
        try {
            Response<UserServer[]> res = mServices.getUsers().execute();
            UserServer[] array = res.body();
            for (int i = 0; i < array.length; i++) {
                l.add(array[i].toGenericModel());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return l;
    }

    @Override
    public Meeting createMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException {
        MeetingServer m = new MeetingServer(0, title, description, _public, level, date, latitude, longitude);
        try {
            Response<MeetingServer> ret = mServices.createMeeting(m).execute();
            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return m.toGenericModel();
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException {
        Forms.UserRegistration ur = new Forms.UserRegistration(0,userName,firstName,lastName,postCode,question,answer,password);
        UserServer u = null;
        try {
            Response<UserServer> ret = mServices.registerUser(ur).execute();
            u = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return u.toGenericModel();

    }

    @Override
    public String login(String username, String password) {
        String token = "";
        Forms.LoginUser lu = new Forms.LoginUser(username,password);
        try {
            Response<Forms.Token> ret = mServices.logIn(lu).execute();
            token = ret.body().getToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }

    @Override
    public User getCurrentUser() {
        UserServer u = null;
        try {
            Response<UserServer> ret = mServices.getCurrentUser().execute();
            u = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return u.toGenericModel();
    }

    @Override
    public boolean logout() {
        boolean ok = false;
        try {
            Response<Void> res = mServices.logout().execute();
            if (res.isSuccessful()) ok = true;
            //TODO check not foud exception
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }


}
