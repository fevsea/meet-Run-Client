package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackFriends;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;

public abstract class GetPendingFriends extends AsyncTask<String, String, List<Friend>> implements AsyncTaskCallbackFriends{

    private IFriendsAdapter friendsAdapter;
    private int page;

    public GetPendingFriends(int page) {
        friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
        this.page = page;
    }

    @Override
    protected List<Friend> doInBackground(String... params) {
            return friendsAdapter.listUserPendingFriends(CurrentSession.getInstance().getCurrentUser().getId(), page);
    }

    @Override
    protected void onPostExecute(List<Friend> l) {
        onResponseReceived(l);
    }
}