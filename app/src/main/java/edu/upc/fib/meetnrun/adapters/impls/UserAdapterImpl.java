package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.MeetingServer;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.adapters.models.StatisticsServer;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import edu.upc.fib.meetnrun.exceptions.AutorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.remote.SOServices;
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
   * @throws AutorizationException
   * @throws ParamsException
   */
  @Override
  public List<Meeting> getUserMeetingsFilteres(int targetUserId, String filterByTime) throws AutorizationException, ParamsException {
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
  public List<Meeting> getUsersFutureMeetings(int targetUserId) throws AutorizationException, ParamsException {
    return getUserMeetingsFilteres(targetUserId, "future");
  }

  @Override
  public List<Meeting> getUserPastMeetings(int targetUserId) throws AutorizationException, ParamsException {
    return getUserMeetingsFilteres(targetUserId, "past");
  }

  public Statistics getUserStatisticsByID(int id) throws AutorizationException {

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

}
