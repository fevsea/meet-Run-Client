package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IChallengeAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class AcceptOrRejectChallenge extends AsyncTask<Boolean, String, Void> implements AsyncTaskCallback, AsyncTaskException {

    private GenericException exception;

    private int challengeId;
    private IChallengeAdapter challengeAdapter;

    public AcceptOrRejectChallenge(int challengeId) {
        this.challengeId = challengeId;
        challengeAdapter = CurrentSession.getInstance().getChallengeAdapter();
    }

    @Override
    protected Void doInBackground(Boolean... params) throws AuthorizationException, NotFoundException{
        try {
            if (params[0]) {
                challengeAdapter.acceptChallenge(challengeId);
            } else {
                challengeAdapter.deleteRejectChallenge(challengeId);
            }
        }
        catch (GenericException e) {
            exception = e;
        }
        return null;
    }

    protected void onPostExecute(Void s) {
        if (exception == null) onResponseReceived();
        else onExceptionReceived(exception);
        super.onPostExecute(s);
    }

}