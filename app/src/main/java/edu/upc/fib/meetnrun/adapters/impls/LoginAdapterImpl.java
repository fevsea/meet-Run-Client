package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.adapters.remote.SOServices;
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
  public String login(String username, String password) throws AuthorizationException {
    String token = "";
    Forms.LoginUser lu = new Forms.LoginUser(username, password);
    try {
      Response<Forms.Token> ret = mServices.logIn(lu).execute();
      if (!ret.isSuccessful()) {
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
      }
      Forms.Token tok = ret.body();
      if (tok != null) {
        token = tok.getToken();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return token;
  }

  @Override
  public User getCurrentUser() throws AuthorizationException {
    UserServer u = null;
    try {
      Response<UserServer> ret = mServices.getCurrentUser().execute();
      if (!ret.isSuccessful()) {
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
      }
      u = ret.body();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return u != null ? u.toGenericModel() : null;
  }

  @Override
  public boolean logout() throws AuthorizationException {
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
    }
    return ok;
  }

  @Override
  public boolean changePassword(String oldPassword, String newPassword) throws AuthorizationException, ForbiddenException {
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
    }
    return ok;
  }

  @Override
  public String getFirebaseToken() throws AuthorizationException, NotFoundException {
    String token = "";
    try {
      Response<Forms.Token> ret = mServices.getFibaseToken().execute();
      if (!ret.isSuccessful()) {
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
      }
      Forms.Token tok = ret.body();
      if (tok != null) {
        token = tok.getToken();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return token;
  }

  @Override
  public boolean updateFirebaseToken(String token) throws AuthorizationException, NotFoundException {
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
    }
    return ok;
  }

  @Override
  public boolean resetFirebaseToken() throws AuthorizationException {
    boolean ok = true;
        try {
          Response<Void> ret = mServices.resetToken().execute();
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
