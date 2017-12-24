package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.view.View;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUser;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class GetCurrentUser extends AsyncTask<String,String,User>  implements AsyncTaskCallbackUser{

    private ILoginAdapter loginAdapter;

    public GetCurrentUser() {
        loginAdapter = CurrentSession.getInstance().getLoginAdapter();
    }

    @Override
    protected User doInBackground(String... s) throws  AuthorizationException{
        CurrentSession.getInstance().setToken(s[0]);
        return loginAdapter.getCurrentUser();
    }

    @Override
    protected void onPostExecute(User u) {
        onResponseReceied(u);
        super.onPostExecute(u);
    }
}