package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackMeetings;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;

/*
    new GetMyMeetings.execute(userId)
 */
public abstract class GetMyMeetings extends AsyncTask<Integer,Void,List<Meeting>> implements AsyncTaskCallbackMeetings {

    private IUserAdapter userAdapter;

    public GetMyMeetings() {
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }

    @Override
    protected List<Meeting> doInBackground(Integer... integers) {
        return userAdapter.getUsersFutureMeetings(integers[0]);
    }

    @Override
    protected void onPostExecute(List<Meeting> myMeetings) {
        onResponseReceived(myMeetings);
        super.onPostExecute(myMeetings);
    }

}