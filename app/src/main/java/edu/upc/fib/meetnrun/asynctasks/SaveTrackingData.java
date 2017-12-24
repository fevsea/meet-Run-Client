package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.TrackingData;

public abstract class SaveTrackingData extends AsyncTask<TrackingData, String, Void> implements AsyncTaskCallback {

    private final String TAG = getClass().getSimpleName();
    private int meetingId;

    public SaveTrackingData(int meetingId) {
        this.meetingId = meetingId;
    }

    @Override
    protected Void doInBackground(TrackingData... params) throws AuthorizationException, ForbiddenException{
        IMeetingAdapter meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
        Integer userID = CurrentSession.getInstance().getCurrentUser().getId();
        TrackingData td = params[0];
        Log.i(TAG, "Saving for (user, meeting)" + userID + " " + meetingId);
        Log.i(TAG, "Saving... " + params[0].toString());
        meetingAdapter.addTracking(userID, meetingId, td.getAverageSpeed(), td.getDistance(), td.getSteps(), td.getTotalTimeMillis(), td.getCalories(), td.getRoutePoints());
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        onResponseReceived();
        onPostExecute(v);
    }
}