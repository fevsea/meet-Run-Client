package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackFriends;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;

public abstract class GetAllFriends extends AsyncTask<Void,Void,Void> implements AsyncTaskCallbackFriends,AsyncTaskException {

    private GenericException exception;
    private List<Friend> friends;
    private IFriendsAdapter friendsAdapter;
    private boolean isLastPage;
    private int pageNumber;

    public GetAllFriends() {
        friends = new ArrayList<>();
        friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
        isLastPage = false;
        pageNumber = 0;
    }


    @Override
    protected Void doInBackground(Void... v) {
        try {
            List<Friend> friendsPage;
            while (!isLastPage) {
                friendsPage = friendsAdapter.listUserAcceptedFriends(CurrentSession.getInstance().getCurrentUser().getId(), pageNumber);
                if (friendsPage.size() != 0) {
                    for (Friend f : friendsPage) {
                        friends.add(f);
                    }
                    ++pageNumber;
                } else isLastPage = true;
            }
        }
        catch (GenericException e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        if (exception == null) onResponseReceived(friends);
        else onExceptionReceived(exception);
        super.onPostExecute(v);
    }
}