package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackMeetings;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;

/*
    new GetMyMeetings.execute(userId)
 */
public abstract class GetMyMeetings extends AsyncTask<Integer,Void,List<Meeting>> implements AsyncTaskCallbackMeetings,AsyncTaskException {

    private GenericException exception;
    private IUserAdapter userAdapter;

    public GetMyMeetings() {
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }

    @Override
    protected List<Meeting> doInBackground(Integer... integers) {
        try {
            return userAdapter.getUsersFutureMeetings(integers[0]);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Meeting> myMeetings) {
        if (exception == null) onResponseReceived(myMeetings);
        else onExceptionReceived(exception);
        super.onPostExecute(myMeetings);
    }

}