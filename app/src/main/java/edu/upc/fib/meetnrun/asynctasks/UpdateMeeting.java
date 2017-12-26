package edu.upc.fib.meetnrun.asynctasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;

public abstract class UpdateMeeting extends AsyncTask<Meeting, Void, Boolean> implements AsyncTaskCallbackBoolean,AsyncTaskException{

    private GenericException exception;
    private IMeetingAdapter meetingAdapter;

    public UpdateMeeting() {
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    protected Boolean doInBackground(Meeting... params) throws AuthorizationException,ParamsException,NotFoundException {
        try {
            return meetingAdapter.updateMeeting(params[0]);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (exception == null) onResponseReceived(result);
        else onExceptionReceived(exception);
        super.onPostExecute(result);
    }

}