package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class CreateChallenge extends AsyncTask<Challenge, String ,Void> implements AsyncTaskCallback, AsyncTaskException {

    private GenericException exception;
    private User challenged;
    private Challenge challenge;

    public CreateChallenge(User challenged, Challenge challenge) {
        this.challenged = challenged;
        this.challenge = challenge;
    }

    @Override
    protected Void doInBackground(Challenge[] params) {
        try {
            User current = CurrentSession.getInstance().getCurrentUser();
            CurrentSession.getInstance().getChallengeAdapter().createNewChallenge(current, challenged, (int) challenge.getDistance(), challenge.getDateDeadline());
        }
        catch (GenericException e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (exception == null) onResponseReceived();
        else onExceptionReceived(exception);
        super.onPostExecute(result);
    }
}