package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUser;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class GetUser extends AsyncTask<Void,String,User> implements AsyncTaskCallbackUser,AsyncTaskException{

    private GenericException exception;
    private int userID;

    public GetUser(int userID) {
        this.userID = userID;
    }

    @Override
    protected User doInBackground(Void... v) {
        try {
            return CurrentSession.getInstance().getUserAdapter().getUser(userID);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(User result) {
        if (exception == null) onResponseReceived(result);
        else onExceptionReceived(exception);
        super.onPostExecute(result);
    }
}