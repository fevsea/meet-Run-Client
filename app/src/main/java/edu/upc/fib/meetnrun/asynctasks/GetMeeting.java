package edu.upc.fib.meetnrun.asynctasks;


import android.os.AsyncTask;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackMeeting;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;

public abstract class GetMeeting extends AsyncTask<Integer,Void,Meeting> implements AsyncTaskCallbackMeeting,AsyncTaskException{

    private GenericException exception;
    private IMeetingAdapter meetingAdapter;

    public GetMeeting() {
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    protected Meeting doInBackground(Integer... integers) {
         try {
             return meetingAdapter.getMeeting(integers[0]);
         }
         catch (GenericException e) {
             exception = e;
             return null;
         }
    }

    @Override
    protected void onPostExecute(Meeting meeting) {
        if (exception == null) onResponseReceived(meeting);
        else onExceptionReceived(exception);
        super.onPostExecute(meeting);
    }

}