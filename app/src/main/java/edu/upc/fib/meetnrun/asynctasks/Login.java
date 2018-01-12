package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackString;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class Login extends AsyncTask<Void,String,String> implements AsyncTaskCallbackString,AsyncTaskException{

    private GenericException exception;
    private String username, password;
    private ILoginAdapter loginAdapter;

    public Login(String username, String password) {
        loginAdapter = CurrentSession.getInstance().getLoginAdapter();
        this.username = username;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... v) {
        try {
            return loginAdapter.login(username, password);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(String token) {
        if (exception == null) onResponseReceived(token);
        else onExceptionReceived(exception);
        super.onPostExecute(token);
    }
}
