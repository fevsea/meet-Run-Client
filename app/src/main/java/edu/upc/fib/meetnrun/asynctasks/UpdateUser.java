package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class UpdateUser extends AsyncTask<User, Void, Boolean> implements AsyncTaskCallbackBoolean,AsyncTaskException{

    private GenericException exception;
    private IUserAdapter userAdapter;

    public UpdateUser() {
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }

    @Override
    protected Boolean doInBackground(User... params) {
        try {
            return userAdapter.updateUser(params[0]);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(Boolean actualitzat_correctament) {
        if (exception == null) onResponseReceived(actualitzat_correctament);
        else onExceptionReceived(exception);
        super.onPostExecute(actualitzat_correctament);
    }
}