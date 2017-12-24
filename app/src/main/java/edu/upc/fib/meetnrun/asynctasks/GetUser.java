package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUser;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.CreateChallengeActivity;

public abstract class GetUser extends AsyncTask<Void,String,User> implements AsyncTaskCallbackUser{

    private int userID;

    public GetUser(int userID) {
        this.userID = userID;
    }

    @Override
    protected User doInBackground(Void... v) throws  NotFoundException{
            return CurrentSession.getInstance().getUserAdapter().getUser(userID);
    }

    @Override
    protected void onPostExecute(User result) {
        onResponseReceied(result);
        onPostExecute(result);
    }
}