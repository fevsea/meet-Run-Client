package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;

public abstract class AcceptOrRejectFriend extends AsyncTask<Friend, String, Boolean> implements AsyncTaskCallbackBoolean, AsyncTaskException{

    private GenericException exception;
    private IFriendsAdapter friendsAdapter;

    public AcceptOrRejectFriend() {
        friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
    }

    @Override
    protected Boolean doInBackground(Friend... params) {
        try {
            Friend friend = params[0];
            if (friend.isAccepted()) {
                Log.d("AcceptFriend", friend.getUser().getUsername());
                return friendsAdapter.addFriend(friend.getUser().getId());
            } else {
                Log.d("RejectFriend", friend.getUser().getUsername());
                return friendsAdapter.removeFriend(friend.getUser().getId());
            }
        }
        catch (GenericException e) {
            exception = e;
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        if (exception == null) onResponseReceived(s);
        else onExceptionReceived(exception);
        super.onPostExecute(s);
    }


}