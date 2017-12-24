package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class AddFriend extends AsyncTask<String,String,Boolean> implements AsyncTaskCallbackBoolean{

    private IFriendsAdapter friendsAdapter;

    public AddFriend() {
        friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
    }

    @Override
    protected Boolean doInBackground(String... s) throws AuthorizationException,ParamsException{
        return friendsAdapter.addFriend(Integer.parseInt(s[0]));
    }

    @Override
    protected void onPostExecute(Boolean b) {
        onResponseReceived(b);
        super.onPostExecute(b);
    }
}