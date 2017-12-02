package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class LoginAdapterImpl implements ILoginAdapter {
    private final SOServices mServices;

    public LoginAdapterImpl(SOServices soServices) {
        mServices = soServices;
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
    public boolean changePassword(String oldPassword, String newPassword) throws AutorizationException, ForbiddenException {
        boolean ok = false;
        try {
            Response<Void> ret = mServices.changePassword(new Forms.ChangePassword(oldPassword, newPassword)).execute();
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
            } else if (e instanceof ForbiddenException) {
                throw (ForbiddenException) e;
            }
        }
        return ok;
    }

    @Override
    public String getFirebaseToken() throws AutorizationException, NotFoundException {
        String token = "";
        try {
            Response<Forms.Token> ret = mServices.getFibaseToken().execute();
            if (!ret.isSuccessful()) {
                checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
            }
            token = ret.body().getToken();
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
        return token;
    }

    @Override
    public boolean uppdateFirebaseToken(String token) throws AutorizationException, NotFoundException {
        boolean ok = true;
        try {
            Forms.Token ownTokenModel = new Forms.Token(token);
            Response<Void> ret = mServices.updateFirebaseToken(ownTokenModel).execute();
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
}
