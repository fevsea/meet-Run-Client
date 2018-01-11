package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackRankingUser;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackRankingZip;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Position;
import edu.upc.fib.meetnrun.models.PositionUser;

public abstract class GetRankingsZip extends AsyncTask<Void,String,List<Position> > implements AsyncTaskCallbackRankingZip,AsyncTaskException {

    private GenericException exception;


    @Override
    protected List<Position> doInBackground(Void... v) {
        try {
            return CurrentSession.getInstance().getRankingAdapter().getAvgForEachZipCode();
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Position> result) {
        if (exception == null) onResponseReceived(result);
        else onExceptionReceived(exception);
        super.onPostExecute(result);
    }
}