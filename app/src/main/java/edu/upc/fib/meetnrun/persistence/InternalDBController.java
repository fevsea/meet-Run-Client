package edu.upc.fib.meetnrun.persistence;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.internalDB.AppDatabase;
import edu.upc.fib.meetnrun.persistence.internalDB.entity.MeetingDB;
import edu.upc.fib.meetnrun.persistence.internalDB.entity.UserDB;
import edu.upc.fib.meetnrun.persistence.persistenceModels.UserServer;

/**
 * Created by Awais Iqbal on 31/10/2017.
 */

public class InternalDBController implements IGenericController {
    private AppDatabase adb;
    public InternalDBController(Context context) {
        adb = AppDatabase.getAppDatabase(context);
    }
    private UserDB currentUser;

    @Override
    public List<Meeting> getAllMeetings() {
        List<MeetingDB> mdb = adb.meetingDao().getAll();
        List<Meeting> ml = new ArrayList<>();
        for (int i = 0; i < mdb.size(); i++) {
            ml.add(mdb.get(i).toGenericModel());
        }
        return ml;
    }

    @Override
    public Meeting createMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException {
        MeetingDB m = new MeetingDB(title,description,_public,level,date,latitude,longitude);
        long insertedId = adb.meetingDao().insert(m);
        int id = (int) insertedId;
        m = adb.meetingDao().findByID(id);
        return m.toGenericModel();
    }

    @Override
    public Meeting getMeeting(int id) throws NotFoundException {
        return adb.meetingDao().findByID(id).toGenericModel();
    }

    @Override
    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException {
        int numUpdate = adb.meetingDao().updateMeetings(new MeetingDB(obj));
        boolean ok = numUpdate== 0 ? false : true;
        return ok;
    }

    @Override
    public boolean deleteMeetingByID(int id) throws NotFoundException {
        MeetingDB m = adb.meetingDao().findByID(id);
        boolean b = false;
        if(m != null){
            adb.meetingDao().deleteByID(id);
            m = adb.meetingDao().findByID(id);
            b = m == null;
        }
        return b;
    }

    @Override
    public List<User> getAllUsers() {
        List<UserDB> mdb = adb.userDao().getAll();
        List<User> ul = new ArrayList<>();
        for (int i = 0; i < mdb.size(); i++) {
            ul.add(mdb.get(i).toGenericModel());
        }
        return ul;
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException {
        UserDB u = new UserDB(userName,firstName,lastName,postCode,question,answer,password);
        long insertedId = adb.userDao().insert(u);
        int id = (int) insertedId;
        u = adb.userDao().findByID(id);
        return u.toGenericModel();
    }

    @Override
    public User getUser(int id) throws NotFoundException {
        return adb.userDao().findByID(id).toGenericModel();
    }

    @Override
    public boolean updateUser(User obj) throws ParamsException, NotFoundException {
        int numUpdate = adb.userDao().updateUsers(new UserDB(obj));
        boolean ok = numUpdate== 0 ? false : true;
        return ok;
    }

    @Override
    public boolean deleteUserByID(int id) throws NotFoundException {
        UserDB m = adb.userDao().findByID(id);
        boolean b = false;
        if(m != null){
            adb.userDao().deleteByID(id);
            m = adb.userDao().findByID(id);
            b = m == null;
        }
        return b;
    }

    @Override
    public String login(String username, String password) {
        currentUser = adb.userDao().login(username,password);
        return "";
    }

    @Override
    public User getCurrentUser() {
        return currentUser.toGenericModel();
    }

    @Override
    public boolean logout() {
        currentUser = null;
        return true;
    }
}