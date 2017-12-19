package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackTrackingData;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.TrackingData;

public abstract class GetPastMeetingsTracking extends AsyncTask<Integer, Integer, TrackingData> implements AsyncTaskCallbackTrackingData{

    private IMeetingAdapter meetingAdapter;

    public GetPastMeetingsTracking() {
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    protected TrackingData doInBackground(Integer... integers) {
        return meetingAdapter.getTracking(integers[0], integers[1]);
    }


    @Override
    protected void onPostExecute(TrackingData tracking) {
        onResponseReceived(tracking);
        super.onPostExecute(tracking);
    }
}