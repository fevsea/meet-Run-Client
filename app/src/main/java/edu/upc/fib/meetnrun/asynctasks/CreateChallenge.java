package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.CreateChallengeActivity;

public abstract class CreateChallenge extends AsyncTask<Challenge, String ,Void> implements AsyncTaskCallback {

    private User challenged;
    private Challenge challenge;

    public CreateChallenge(User challenged, Challenge challenge) {
        this.challenged = challenged;
        this.challenge = challenge;
    }

    @Override
    protected Void doInBackground(Challenge[] params) throws AuthorizationException, ParamsException {
        User current = CurrentSession.getInstance().getCurrentUser();
        CurrentSession.getInstance().getChallengeAdapter().createNewChallenge(current, challenged, (int)challenge.getDistance(), challenge.getDateDeadline());
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        onResponseReceived();
        onPostExecute(result);
    }
}