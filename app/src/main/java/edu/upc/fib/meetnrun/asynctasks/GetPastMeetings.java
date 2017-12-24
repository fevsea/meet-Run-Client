package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackMeetings;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;

public abstract class GetPastMeetings extends AsyncTask<Integer, Integer, List<Meeting> > implements AsyncTaskCallbackMeetings {

    private IUserAdapter userAdapter;

    public GetPastMeetings() {
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }
    @Override
    protected List<Meeting> doInBackground(Integer... integers) throws AuthorizationException,ParamsException{
        return userAdapter.getUserPastMeetings(integers[0]);
    }

    @Override
    protected void onPostExecute(List<Meeting> meetings) {
        onResponseReceived(meetings);
        super.onPostExecute(meetings);
    }
}
