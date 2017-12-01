package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.Utils.calculateOffset;
import static edu.upc.fib.meetnrun.adapters.utils.Utils.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class FriendsAdapterImpl implements IFriendsAdapter {
    private final SOServices mServices;

    public FriendsAdapterImpl(SOServices soServices) {
        mServices = soServices;
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
    public List<User> getUserFriends(int page) throws AutorizationException {
        //TODO pending to TEST
        List<User> ul = new ArrayList<>();
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<UserServer>> ret =
                    mServices.getCurrentUserFriends(SOServices.PAGELIMIT, offset).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            PageServer<UserServer> u = ret.body();

            for (int i = 0; i < u.getResults().size(); i++) {
                ul.add(u.getResults().get(i).toGenericModel());
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
    public List<User> listFriendsOfUser(int targetUserId, int page) throws AutorizationException, ParamsException {
        //TODO pending to TEST
        List<User> ul = new ArrayList<>();
        try {
            int offset = calculateOffset(SOServices.PAGELIMIT, page);
            Response<PageServer<UserServer>> ret = mServices.getAllFriendsOfUser(targetUserId, SOServices.PAGELIMIT, offset).execute();
            if (!ret.isSuccessful())
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

            PageServer<UserServer> u = ret.body();

            for (int i = 0; i < u.getResults().size(); i++) {
                ul.add(u.getResults().get(i).toGenericModel());
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
}
