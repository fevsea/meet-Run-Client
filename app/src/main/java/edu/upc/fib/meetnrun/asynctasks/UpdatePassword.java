package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class UpdatePassword extends AsyncTask<String, Void, Boolean> implements AsyncTaskCallbackBoolean,AsyncTaskException{

    private GenericException exception;
    private ILoginAdapter loginAdapter;

    public UpdatePassword() {
        loginAdapter = CurrentSession.getInstance().getLoginAdapter();
    }

    @Override
    protected Boolean doInBackground(String... params) throws AuthorizationException, ForbiddenException {
         try {
             return loginAdapter.changePassword(params[0], params[1]);
         }
         catch (GenericException e) {
             exception = e;
             return null;
         }
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if (exception == null) onResponseReceived(b);
        else onExceptionReceived(exception);
        super.onPostExecute(b);
    }
}