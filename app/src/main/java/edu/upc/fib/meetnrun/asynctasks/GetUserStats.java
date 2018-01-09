package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackStatistics;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Statistics;
import edu.upc.fib.meetnrun.models.User;

public abstract class GetUserStats extends AsyncTask<String,Void,Statistics> implements AsyncTaskCallbackStatistics,AsyncTaskException {

    private GenericException exception;
    private User u;
    private IUserAdapter userAdapter;

    public GetUserStats(User u) {
        this.u = u;
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }

    @Override
    protected Statistics doInBackground(String... strings) {
            //TODO: Que tot no sigui de current user
            try {
                int id = u.getId();
                return userAdapter.getUserStatisticsByID(id);
            }
            catch (GenericException e) {
                exception = e;
                return null;
            }
            //TODO: Hacerlo bien, sin hardcoded
            //u=iUserAdapter.getUser(userId);
    }

    @Override
    protected void onPostExecute(Statistics s){
        if (exception == null) onResponseReceived(s);
        else onExceptionReceived(exception);
        super.onPostExecute(s);
    }
}