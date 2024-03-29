package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUser;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class GetCurrentUser extends AsyncTask<String,String,User>  implements AsyncTaskCallbackUser,AsyncTaskException{

    private GenericException exception;
    private ILoginAdapter loginAdapter;

    public GetCurrentUser() {
        loginAdapter = CurrentSession.getInstance().getLoginAdapter();
    }

    @Override
    protected User doInBackground(String... s) {
        try {
            CurrentSession.getInstance().setToken(s[0]);
            return loginAdapter.getCurrentUser();
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(User u) {
        if (exception == null) onResponseReceived(u);
        else onExceptionReceived(exception);
        super.onPostExecute(u);
    }
}