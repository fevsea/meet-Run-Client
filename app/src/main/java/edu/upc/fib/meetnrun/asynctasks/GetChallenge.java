package edu.upc.fib.meetnrun.asynctasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChallengeAdapter;
import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChallenge;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class GetChallenge extends AsyncTask<Integer, String, Challenge> implements AsyncTaskCallbackChallenge,AsyncTaskException {

    private GenericException exception;
    private IChallengeAdapter challengeAdapter;

    public GetChallenge() {
        challengeAdapter = CurrentSession.getInstance().getChallengeAdapter();
    }

    @Override
    protected Challenge doInBackground(Integer... params) {
        try {
            return challengeAdapter.getChallenge(params[0]);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Challenge challenge) {
        if (exception == null) onResponseReceived(challenge);
        else onExceptionReceived(exception);
        super.onPostExecute(challenge);
    }

}