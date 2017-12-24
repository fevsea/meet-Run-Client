package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChallengeAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class AcceptOrRejectChallenge extends AsyncTask<Boolean, String, Void> implements AsyncTaskCallback {

    private int challengeId;
    private IChallengeAdapter challengeAdapter;

    public AcceptOrRejectChallenge(int challengeId) {
        this.challengeId = challengeId;
        challengeAdapter = CurrentSession.getInstance().getChallengeAdapter();
    }

    @Override
    protected Void doInBackground(Boolean... params) throws AuthorizationException, NotFoundException{
        if (params[0]) {
            challengeAdapter.acceptChallenge(challengeId);
        }
        else {
            challengeAdapter.deleteRejectChallenge(challengeId);
        }
        return null;
    }

    protected void onPostExecute(Void s) {
        onResponseReceived();
        super.onPostExecute(s);
    }

}