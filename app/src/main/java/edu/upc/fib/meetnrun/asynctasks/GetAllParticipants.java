package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUsers;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;

public abstract class GetAllParticipants extends AsyncTask<Integer,Void,List<User>> implements AsyncTaskCallbackUsers,AsyncTaskException {

    private GenericException exception;
    private int page;
    private IMeetingAdapter meetingAdapter;
    private boolean isLastPage;
    private List<User> participants;

    public GetAllParticipants () {
        page = 0;
        isLastPage = false;
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
        participants = new ArrayList<>();
    }

    @Override
    protected List<User> doInBackground(Integer... integers) {
        try {
            List<User> participantsPage = new ArrayList<>();
            while (!isLastPage) {
                meetingAdapter.getParticipantsFromMeeting(integers[0], page);
                if (participantsPage.size() != 0) {
                    for (User u : participantsPage) {
                        participants.add(u);
                    }
                    ++page;
                } else isLastPage = true;
            }
            return participants;
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

}