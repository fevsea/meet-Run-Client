package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.MeetingServer;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.Utils.calculateOffset;
import static edu.upc.fib.meetnrun.adapters.utils.Utils.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class UserAdapterImpl implements IUserAdapter {
    private SOServices mServices;

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
        } catch (GenericException e) {
            e.printStackTrace();
        }
        List<UserServer> lus = pus.getResults();
        List<User> lu = new ArrayList<>();
        for (int i = 0; i < lus.size(); i++) {
            lu.add(lus.get(i).toGenericModel());
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
        } catch (GenericException e) {
            e.printStackTrace();
            if (e instanceof NotFoundException) throw (NotFoundException) e;
        }
        return us.toGenericModel();
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
    public List<Meeting> getUsersFutureMeetings(int userId, int page) throws AutorizationException, ParamsException {
        List<Meeting> ul = new ArrayList<>();
        //TODO Pending to TEST
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<MeetingServer>> ret =
                    mServices.getAllFutureMeetings(SOServices.PAGELIMIT, offset, userId).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            PageServer<MeetingServer> u = ret.body();

            for (int i = 0; i < u.getResults().size(); i++) {
                ul.add(u.getResults().get(i).toGenericModel());
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

}
