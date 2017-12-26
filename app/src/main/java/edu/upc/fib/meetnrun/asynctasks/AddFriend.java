package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class AddFriend extends AsyncTask<String,String,Boolean> implements AsyncTaskCallbackBoolean, AsyncTaskException{

    private GenericException exception;
    private IFriendsAdapter friendsAdapter;

    public AddFriend() {
        friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
    }

    @Override
    protected Boolean doInBackground(String... s) {
        try {
            return friendsAdapter.addFriend(Integer.parseInt(s[0]));
        }
        catch (GenericException e) {
            exception = e;
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if (exception == null) onResponseReceived(b);
        else onExceptionReceived(exception);
        super.onPostExecute(b);
    }
}