package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackRankingUser;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackRankingUserZips;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.PositionUser;

/**
 * Created by Javier on 10/01/2018.
 */

public abstract class GetRankingsUserAllZips extends AsyncTask<List<String>, Void, Void> implements AsyncTaskCallbackRankingUserZips,AsyncTaskException {
    private GenericException exception;

    protected List<String> doInBackground(Void... v) {
        try {
            return CurrentSession.getInstance().getRankingAdapter().getAllPostalCodes();
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    protected void onPostExecute(List<String> result) {
        if (exception == null) onResponseReceived(result);
        else onExceptionReceived(exception);
        super.onPostExecute(null);
    }
}
