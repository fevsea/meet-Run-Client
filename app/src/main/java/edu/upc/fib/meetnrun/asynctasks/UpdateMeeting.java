package edu.upc.fib.meetnrun.asynctasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;

public abstract class UpdateMeeting extends AsyncTask<Meeting, Void, Boolean> implements AsyncTaskCallbackBoolean{

    private IMeetingAdapter meetingAdapter;

    public UpdateMeeting() {
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    protected Boolean doInBackground(Meeting... params) {
        return meetingAdapter.updateMeeting(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        onResponseReceived(result);
    }

}