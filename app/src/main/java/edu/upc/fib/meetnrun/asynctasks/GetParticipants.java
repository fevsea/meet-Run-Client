package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUsers;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class GetParticipants extends AsyncTask<Integer,Void,List<User>> implements AsyncTaskCallbackUsers,AsyncTaskException {

    private GenericException exception;
    private int page;
    private IMeetingAdapter meetingAdapter;

    public GetParticipants (int page) {
        setPage(page);
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    protected List<User> doInBackground(Integer... integers)  {
        try {
            return meetingAdapter.getParticipantsFromMeeting(integers[0],page);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<User> users) {
        if (exception == null) onResponseReceived(users);
        else onExceptionReceived(exception);
        super.onPostExecute(users);
    }

    private int getPage() {
        return this.page;
    }

    private void setPage(int page) {
        this.page = page;
    }
}