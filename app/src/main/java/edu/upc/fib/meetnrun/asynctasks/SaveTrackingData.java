package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.TrackingData;

public abstract class SaveTrackingData extends AsyncTask<TrackingData, String, Boolean> {

    Exception exception = null;

    @Override
    protected void onPreExecute() {
        contentMain.setVisibility(View.GONE);
        pauseButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected Boolean doInBackground(TrackingData... params) {
        try {
            IMeetingAdapter meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
            Integer userID = CurrentSession.getInstance().getCurrentUser().getId();
            TrackingData td = params[0];
            Log.i(TAG, "Saving for (user, meeting)" + userID + " " + meetingId);
            Log.i(TAG, "Saving... " + params[0].toString());
            meetingAdapter.addTracking(userID, meetingId, td.getAverageSpeed(), td.getDistance(), td.getSteps(), td.getTotalTimeMillis(), td.getCalories(), td.getRoutePoints());
        } catch (ForbiddenException | AuthorizationException e) {
            exception = e;
            return false;
        }
        return true;
    }