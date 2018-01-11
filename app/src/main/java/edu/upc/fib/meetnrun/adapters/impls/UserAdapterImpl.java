package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.MeetingServer;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.adapters.models.StatisticsServer;
import edu.upc.fib.meetnrun.adapters.models.TrophiesListServer;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import edu.upc.fib.meetnrun.adapters.remote.SOServices;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.Trophie;
import edu.upc.fib.meetnrun.models.User;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.calculateOffset;
import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class UserAdapterImpl implements IUserAdapter {
    private final SOServices mServices;

    public UserAdapterImpl(SOServices soServices) {
        mServices = soServices;
    }

    public List<User> getAllUsers(int page) {
        PageServer<UserServer> pus = null;
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<UserServer>> ret = mServices.getAllUsers(SOServices.PAGELIMIT, offset).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            pus = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<User> lu = new ArrayList<>();
        if (pus != null) {
            List<UserServer> lus = pus.getResults();
            for (int i = 0; i < lus.size(); i++) {
                lu.add(lus.get(i).toGenericModel());
            }
        }
        return lu;
    }

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
        }
        return us != null ? us.toGenericModel() : null;
    }

    @Override
    public boolean updateUser(User obj) throws ParamsException, NotFoundException, AuthorizationException {
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
        }
        return ok;
    }

    @Override
    public boolean deleteUserByID(int id) throws NotFoundException, AuthorizationException {
        boolean ok = true;
        try {
            Response<Void> ret = mServices.deleteUser(id).execute();
            if (!ret.isSuccessful()) {
                ok = false;
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
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
        }
        return u != null ? u.toGenericModel() : null;

    }


    /**
     * Given a target UserID and a Filter, returns all the meeting who meet the conditions
     *
     * @param targetUserId Target user who most be in the meeting
     * @param filterByTime Filter can be : past, future, all
     * @return List of {@link List<Meeting>}
     * @throws AuthorizationException
     * @throws ParamsException
     */
    @Override
    public List<Meeting> getUserMeetingsFilteres(int targetUserId, String filterByTime) throws AuthorizationException, ParamsException {
        List<Meeting> ul = new ArrayList<>();
        try {
            Response<List<MeetingServer>> ret =
                    mServices.getUserMeetingFilteredMeetings(targetUserId, filterByTime).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            List<MeetingServer> u = ret.body();
            if (u != null) {
                for (int i = 0; i < u.size(); i++) {
                    ul.add(u.get(i).toGenericModel());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ul;
    }

    @Override
    public List<Meeting> getUsersFutureMeetings(int targetUserId) throws AuthorizationException, ParamsException {
        return getUserMeetingsFilteres(targetUserId, "future");
    }

    @Override
    public List<Meeting> getUserPastMeetings(int targetUserId) throws AuthorizationException, ParamsException {
        return getUserMeetingsFilteres(targetUserId, "past");
    }

    public Statistics getUserStatisticsByID(int id) throws AuthorizationException {

        StatisticsServer ss = null;
        try {
            Response<StatisticsServer> ret = mServices.getUserStatisticsByID(id).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            ss = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ss != null ? ss.toGenericModel() : null;
    }

    @Override
    public List<Trophie> getUserTrophieByID(int id) throws AuthorizationException {
        TrophiesListServer tls = null;
        try {
            Response<TrophiesListServer> ret = mServices.getTrophiesListByID(id).execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            tls = ret.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tls != null ? tls.toGenericModel() : null;
    }

    @Override
    public boolean banUser(int targetUserID) throws ForbiddenException {
        boolean ok = true;
        try {
            Response<Void> ret = mServices.requestBan(targetUserID).execute();
            if (!ret.isSuccessful()) {
                ok = false;
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ok;
    }

}
