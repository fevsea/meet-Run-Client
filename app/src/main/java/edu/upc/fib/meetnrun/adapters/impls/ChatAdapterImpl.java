package edu.upc.fib.meetnrun.adapters.impls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.models.ChatServer;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.remote.SOServices;
import retrofit2.Response;

import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.calculateOffset;
import static edu.upc.fib.meetnrun.adapters.utils.UtilsAdapter.checkErrorCodeAndThowException;

/**
 * Created by Awais Iqbal on 01/12/2017.
 */

public class ChatAdapterImpl implements IChatAdapter {
  private final SOServices mServices;

  public ChatAdapterImpl(SOServices soServices) {
    mServices = soServices;
  }

  @Override
  public Chat createChat(String chatName, List<Integer> listUsersChatIDs, int type, Integer meetingID, String lastMessage, int lastMessageUserNamePosition, Date lastDateTime) throws AuthorizationException, ParamsException {
    Forms.ChatCreateUpdate ur = new Forms.ChatCreateUpdate(chatName, listUsersChatIDs, type, meetingID, lastMessage, lastMessageUserNamePosition, lastDateTime);
    ChatServer u = null;
    try {
      Response<ChatServer> ret = mServices.createChat(ur).execute();
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
      } else if (e instanceof AuthorizationException) {
        throw (AuthorizationException) e;
      }
    }
    return u != null ? u.toGenericModel() : null;
  }

  @Override
  public List<Chat> getChats(int page) throws AuthorizationException {
    List<Chat> l = new ArrayList<>();
    try {
      int offset = calculateOffset(SOServices.PAGELIMIT, page);
      Response<PageServer<ChatServer>> res =
        mServices.getChats(SOServices.PAGELIMIT, offset).execute();
      PageServer<ChatServer> psm = res.body();
      if (psm != null) {
        for (int i = 0; i < psm.getResults().size(); i++) {
          l.add(psm.getResults().get(i).toGenericModel());
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return l;
  }

  @Override
  public boolean updateChat(Chat c) throws AuthorizationException, ParamsException, NotFoundException {
    boolean ok = false;
    Forms.ChatCreateUpdate ur = new Forms.ChatCreateUpdate(c);
    try {
      Response<Void> ret = mServices.updateChat(c.getId(), ur).execute();
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
      } else if (e instanceof AuthorizationException) {
        throw (AuthorizationException) e;
      }
    }
    return ok;
  }

  @Override
  public boolean deleteChat(int id) throws AuthorizationException, ParamsException, NotFoundException {
    boolean ok = true;
    try {
      Response<Void> ret = mServices.deleteChat(id).execute();
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
  public Chat getChat(int id) throws AuthorizationException, NotFoundException {
    ChatServer cs = null;
    try {
      Response<ChatServer> ret = mServices.getChat(id).execute();
      if (!ret.isSuccessful())
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
      cs = ret.body();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return cs != null ? cs.toGenericModel() : null;
  }

  @Override
  public Chat getPrivateChat(int targetUserID) throws AuthorizationException, NotFoundException {
    ChatServer cs = null;
    try {
      Response<ChatServer> ret = mServices.getPrivateChat(targetUserID).execute();
      if (!ret.isSuccessful())
        checkErrorCodeAndThowException(ret.code(), ret.errorBody().string());
      cs = ret.body();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return cs != null ? cs.toGenericModel() : null;
  }
}
