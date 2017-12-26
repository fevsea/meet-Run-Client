package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChallenges;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class GetChallenges extends AsyncTask<String,String,List<Challenge> > implements AsyncTaskCallbackChallenges,AsyncTaskException {

    private GenericException exception;

    @Override
    protected List<Challenge> doInBackground(String... params) throws AuthorizationException{
        try {
            return CurrentSession.getInstance().getChallengeAdapter().getCurrentUserChallenges();
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Challenge> challenges) {
        if (exception == null) onResponseReceived(challenges);
        else onExceptionReceived(exception);
        super.onPostExecute(challenges);
    }
}
