package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackException;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class DeleteMeeting extends AsyncTask<Integer, String, Boolean> implements AsyncTaskCallback, AsyncTaskException {

    private GenericException exception;
    private IMeetingAdapter adapter;

    public DeleteMeeting() {
        adapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    protected Boolean doInBackground(Integer... params) {
        try {
            adapter.deleteMeetingByID(params[0]);
        }
        catch (NotFoundException |AuthorizationException e) {
            exception = e;
            return false;
        }
        return true;
    }

    protected void onPostExecute(Boolean s) {
        if (exception != null) {
            onExceptionReceived(exception);
        }
        else {
            onResponseReceived();
        }
    }

}
