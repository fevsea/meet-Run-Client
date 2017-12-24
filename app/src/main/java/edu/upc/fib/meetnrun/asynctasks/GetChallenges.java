package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChallenges;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class GetChallenges extends AsyncTask<String,String,List<Challenge> > implements AsyncTaskCallbackChallenges {


    @Override
    protected List<Challenge> doInBackground(String... params) throws AuthorizationException{
            return CurrentSession.getInstance().getChallengeAdapter().getCurrentUserChallenges();
    }

    @Override
    protected void onPostExecute(List<Challenge> challenges) {
        onResponseReceived(challenges);
        super.onPostExecute(challenges);
    }
}
