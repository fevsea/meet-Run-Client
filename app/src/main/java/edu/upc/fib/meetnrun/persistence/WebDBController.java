package edu.upc.fib.meetnrun.persistence;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.persistenceModels.Forms;
import edu.upc.fib.meetnrun.persistence.persistenceModels.MeetingServer;
import edu.upc.fib.meetnrun.persistence.persistenceModels.UserServer;
import edu.upc.fib.meetnrun.remote.ApiUtils;
import edu.upc.fib.meetnrun.remote.SOServices;
import edu.upc.fib.meetnrun.utils.JsonUtils;
import retrofit2.Response;

/**
 * Created by Awais Iqbal on 25/10/2017.
 */

public class WebDBController implements IGenericController {

    private static WebDBController instance = null;

    private WebDBController() {
        mServices = ApiUtils.getSOService();
    }

    public static WebDBController getInstance() {
        if (instance == null) {
            instance = new WebDBController();
        }
        return instance;
    }

    private SOServices mServices;

    @Override
    public User getUser(int id) throws NotFoundException {
        UserServer us = null;
        try {
            Response<UserServer> ret = mServices.getUser(id).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            us = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) throw (NotFoundException) e;
        }
        return us.toGenericModel();
    }


    @Override
    public Meeting getMeeting(int id) throws NotFoundException {
        MeetingServer m = null;
        try {
            Response<MeetingServer> ret = mServices.getMeeting(id).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) throw (NotFoundException) e;
        }
        return m.toGenericModel();
    }

    @Override
    public boolean updateUser(User obj) throws ParamsException, NotFoundException, AutorizationException {
        boolean ok = false;
        UserServer us = new UserServer(obj);
        try {
            Response<Void> ret = mServices.updateUser(obj.getId(), us).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            } else {
                ok = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) {
                throw (NotFoundException) e;
            } else if (e instanceof ParamsException) {
                throw (ParamsException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return ok;
    }

    @Override
    public boolean updateMeeting(Meeting obj) throws ParamsException, NotFoundException, AutorizationException {
        boolean ok = false;
        MeetingServer ms = new MeetingServer(obj);
        try {
            Response<Void> ret = mServices.updateMeeting(obj.getId(), ms).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            } else {
                ok = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) {
                throw (NotFoundException) e;
            } else if (e instanceof ParamsException) {
                throw (ParamsException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return ok;
    }

    @Override
    public boolean deleteUserByID(int id) throws NotFoundException, AutorizationException {
        boolean ok = true;
        try {
            Response<Void> ret = mServices.deleteUser(id).execute();
            if (!ret.isSuccessful()) {
                ok = false;
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) {
                throw (NotFoundException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return ok;
    }

    @Override
    public boolean deleteMeetingByID(int id) throws NotFoundException, AutorizationException {
        boolean ok = true;
        try {
            Response<Void> ret = mServices.deletetMeeting(id).execute();
            if (!ret.isSuccessful()) {
                ok = false;
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) {
                throw (NotFoundException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
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
    public Meeting createMeeting(String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) throws ParamsException, AutorizationException {
        Forms.CreateMeeting ur = new Forms.CreateMeeting(title, description, _public, level, date, latitude, longitude);
        UserServer u = null;
        MeetingServer m = null;
        try {
            Response<MeetingServer> ret = mServices.createMeeting(ur).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            m = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof ParamsException) {
                throw (ParamsException) e;
            } else if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return m.toGenericModel();
    }

    @Override
    public User registerUser(String userName, String firstName, String lastName, String postCode, String password, String question, String answer) throws ParamsException {
        Forms.UserRegistration ur = new Forms.UserRegistration(0, userName, firstName, lastName, postCode, question, answer, password, 1);
        UserServer u = null;
        try {
            Response<UserServer> ret = mServices.registerUser(ur).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            u = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof ParamsException) {
                throw (ParamsException) e;
            }
        }
        return u.toGenericModel();

    }

    @Override
    public String login(String username, String password) throws AutorizationException {
        String token = "";
        Forms.LoginUser lu = new Forms.LoginUser(username, password);
        try {
            Response<Forms.Token> ret = mServices.logIn(lu).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            token = ret.body().getToken();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return token;
    }

    @Override
    public User getCurrentUser() throws AutorizationException {
        UserServer u = null;
        try {
            Response<UserServer> ret = mServices.getCurrentUser().execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            u = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return u.toGenericModel();
    }

    @Override
    public boolean logout() throws AutorizationException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.logout().execute();
            if (ret.isSuccessful()) {
                ok = true;
            } else {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            }
        }
        return ok;
    }

    @Override
    public List<User> getParticipantsFromMeeting(int meetingId) throws AutorizationException, ParamsException {
        List<User> ul = new ArrayList<>();
        try {
            Response<List<UserServer>> ret = mServices.getAllParticipantsFromMeeting(meetingId).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            List<UserServer> u = ret.body();

            for (int i = 0; i < u.size(); i++) {
                ul.add(u.get(i).toGenericModel());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            } else if (e instanceof ParamsException) {
                throw (ParamsException) e;
            }
        }
        return ul;
    }

    @Override
    public boolean joinMeeting(int meetingId) throws AutorizationException, ParamsException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.joinMeeting(meetingId).execute();
            if (ret.isSuccessful()) {
                ok = true;
            } else {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException)
                throw (AutorizationException) e;
        }
        return ok;
    }

    @Override
    public boolean leaveMeeting(int meetingId) throws AutorizationException, ParamsException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.leaveMeeting(meetingId).execute();
            if (ret.isSuccessful()) {
                ok = true;
            } else {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException)
                throw (AutorizationException) e;
        }
        return ok;
    }

    @Override
    public List<Meeting> getUsersFutureMeetings(int userId) throws AutorizationException, ParamsException {
        List<Meeting> ul = new ArrayList<>();
        try {
            Response<List<MeetingServer>> ret = mServices.getAllFutureMeetings(userId).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            List<MeetingServer> u = ret.body();

            for (int i = 0; i < u.size(); i++) {
                ul.add(u.get(i).toGenericModel());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            } else if (e instanceof ParamsException) {
                throw (ParamsException) e;
            }
        }
        return ul;
    }

    @Override
    public List<User> getUserFriends() throws AutorizationException {
        List<User> ul = new ArrayList<>();
        try {
            Response<List<UserServer>> ret = mServices.getCurrentUserFriends().execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            List<UserServer> u = ret.body();

            for (int i = 0; i < u.size(); i++) {
                ul.add(u.get(i).toGenericModel());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException)
                throw (AutorizationException) e;
        }
        return ul;
    }

    @Override
    public boolean addFriend(int targetUserId) throws AutorizationException, ParamsException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.addFriend(targetUserId).execute();
            if (ret.isSuccessful()) {
                ok = true;
            } else {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            } else if (e instanceof ParamsException) {
                throw (ParamsException) e;
            }
        }
        return ok;
    }

    @Override
    public boolean removeFriend(int targetUserId) throws AutorizationException, ParamsException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.removeFriend(targetUserId).execute();
            if (ret.isSuccessful()) {
                ok = true;
            } else {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException) {
                throw (AutorizationException) e;
            } else if (e instanceof ParamsException) {
                throw (ParamsException) e;
            }
        }
        return ok;
    }

    @Override
    public List<User> listFriendsOfUser(int targetUserId) throws AutorizationException, ParamsException {
        List<User> ul = new ArrayList<>();
        try {
            Response<List<UserServer>> ret = mServices.getFriendsOfUser(targetUserId).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            List<UserServer> u = ret.body();

            for (int i = 0; i < u.size(); i++) {
                ul.add(u.get(i).toGenericModel());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof AutorizationException)
                throw (AutorizationException) e;
        }
        return ul;
    }

    private void checkErrorCodeAndThowException(int code, String string) throws GenericException {
        switch (code) {
            case 400:
                throw JsonUtils.CreateParamExceptionFromJson(string);
            case 401:
                throw JsonUtils.CreateAutorizationExceptionFromJson(string);
            case 404:
                throw JsonUtils.CreateNotFoundExceptionFromJson(string);
        }
    }

}