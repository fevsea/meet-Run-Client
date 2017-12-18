package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.view.View;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackFriends;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;

public abstract class GetFriends extends AsyncTask<Integer,Void,List<Friend>> implements AsyncTaskCallbackFriends{

    private IFriendsAdapter friendsAdapter;
    private int page;

    public GetFriends(int page) {
        friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
        this.page = page;
    }

    @Override
    protected List<Friend> doInBackground(Integer... integers) {
         return friendsAdapter.listUserAcceptedFriends(CurrentSession.getInstance().getCurrentUser().getId(), page);
    }

    @Override
    protected void onPostExecute(List<Friend> friends) {
        onResponseReceived(friends);
        super.onPostExecute(friends);
    }

}
