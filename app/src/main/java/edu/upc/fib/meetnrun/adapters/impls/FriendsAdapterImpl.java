package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.adapters.models.FriendServer;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.adapters.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.calculateOffset;
import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class FriendsAdapterImpl implements IFriendsAdapter {
  private final SOServices mServices;

  public FriendsAdapterImpl(SOServices soServices) {
    mServices = soServices;
  }

  @Override
  public boolean addFriend(int targetUserId) throws AuthorizationException, ParamsException {
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
    }
    return ok;
  }

  @Override
  public List<Friend> getUserFriends(int page) throws AuthorizationException {
    List<Friend> ul = new ArrayList<>();
    try {
      int offset = calculateOffset(SOServices.PAGELIMIT, page);
      Response<PageServer<FriendServer>> ret =
        mServices.getCurrentUserFriends(SOServices.PAGELIMIT, offset).execute();
      if (!ret.isSuccessful())
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
      PageServer<FriendServer> u = ret.body();
      if (u != null) {
        for (int i = 0; i < u.getResults().size(); i++) {
          ul.add(u.getResults().get(i).toGenericModel());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ul;
  }

  @Override
  public boolean removeFriend(int targetUserId) throws AuthorizationException, ParamsException {
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
    }
    return ok;
  }

  @Override
  public List<Friend> listFriendsOfUser(int targetUserId, int page) throws AuthorizationException, ParamsException {
    List<Friend> ul = new ArrayList<>();
    try {
      int offset = calculateOffset(SOServices.PAGELIMIT, page);
      Response<PageServer<FriendServer>> ret = mServices.getAllFriendsOfUser(targetUserId, SOServices.PAGELIMIT, offset).execute();
      if (!ret.isSuccessful())
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());

      PageServer<FriendServer> u = ret.body();
      if (u != null) {
        for (int i = 0; i < u.getResults().size(); i++) {
          ul.add(u.getResults().get(i).toGenericModel());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ul;
  }

  @Override
  public List<Friend> listUserPendingFriends(int targetUserId, int page) throws AuthorizationException, NotFoundException {
    List<Friend> ul = new ArrayList<>();
    try {
      int offset = calculateOffset(SOServices.PAGELIMIT, page);
      Response<PageServer<FriendServer>> ret = mServices.getUserPendingFriends(targetUserId, SOServices.PAGELIMIT, offset).execute();
      if (!ret.isSuccessful())
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
      PageServer<FriendServer> u = ret.body();
      if (u != null) {
        for (int i = 0; i < u.getResults().size(); i++) {
          ul.add(u.getResults().get(i).toGenericModel());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ul;
  }

  @Override
  public List<Friend> listUserAcceptedFriends(int targetUserId, int page) throws AuthorizationException, NotFoundException {
    List<Friend> ul = new ArrayList<>();
    try {
      int offset = calculateOffset(SOServices.PAGELIMIT, page);
      Response<PageServer<FriendServer>> ret = mServices.getUserAcceptedFriends(targetUserId, SOServices.PAGELIMIT, offset).execute();
      if (!ret.isSuccessful())
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
      PageServer<FriendServer> u = ret.body();
      if (u != null) {
        for (int i = 0; i < u.getResults().size(); i++) {
          ul.add(u.getResults().get(i).toGenericModel());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return ul;
  }
}
