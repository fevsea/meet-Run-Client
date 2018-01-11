package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUser;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class ReportUser extends AsyncTask<Void,String,Boolean> implements AsyncTaskCallbackBoolean,AsyncTaskException {

    private GenericException exception;
    private int userID;

    public ReportUser(int userID) {
        this.userID = userID;
    }

    @Override
    protected Boolean doInBackground(Void... v) {
        try {
            return CurrentSession.getInstance().getUserAdapter().banUser(userID);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean reported) {
        if (exception == null) onResponseReceived(reported);
        else onExceptionReceived(exception);
        super.onPostExecute(reported);
    }
}