package edu.upc.fib.meetnrun.asynctasks;


import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackRankingUser;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUser;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.models.User;

public abstract class GetRankingsUser extends AsyncTask<Void,String,List<PositionUser> > implements AsyncTaskCallbackRankingUser,AsyncTaskException {

    private GenericException exception;
    private int page;
    private Integer zip;

    public GetRankingsUser(Integer page, Integer zip) {
        this.page = page;
        this.zip = zip;
    }

    @Override
    protected List<PositionUser> doInBackground(Void... v) {
        try {
            if (zip == null) return CurrentSession.getInstance().getRankingAdapter().getUsersFromUsersRanking(page);
            else return CurrentSession.getInstance().getRankingAdapter().getUsersFromUsersRankingFilterByZIP(String.valueOf(zip),page);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<PositionUser> result) {
        if (exception == null) onResponseReceived(result);
        else onExceptionReceived(exception);
        super.onPostExecute(result);
    }
}