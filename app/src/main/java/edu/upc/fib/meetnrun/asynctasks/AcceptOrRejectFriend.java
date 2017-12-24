package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;

public abstract class AcceptOrRejectFriend extends AsyncTask<Friend, String, Boolean> implements AsyncTaskCallbackBoolean{

    private IFriendsAdapter friendsAdapter;

    public AcceptOrRejectFriend() {
        friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
    }

    @Override
    protected Boolean doInBackground(Friend... params) throws AuthorizationException,ParamsException{
        Friend friend = params[0];
        if (friend.isAccepted()) {
            Log.d("AcceptFriend", friend.getUser().getUsername());
            return friendsAdapter.addFriend(friend.getUser().getId());
        }
        else {
            Log.d("RejectFriend", friend.getUser().getUsername());
            return friendsAdapter.removeFriend(friend.getUser().getId());
        }
    }

    @Override
    protected void onPostExecute(Boolean s) {
        onResponseReceived(s);
    }


}