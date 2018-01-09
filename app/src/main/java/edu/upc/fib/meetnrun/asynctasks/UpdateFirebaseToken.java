package edu.upc.fib.meetnrun.asynctasks;


import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.services.FirebaseInstanceService;


public abstract class UpdateFirebaseToken extends AsyncTask<String,Void,Void> implements AsyncTaskCallback,AsyncTaskException {

    private GenericException exception;


    @Override
    protected Void doInBackground(String... token) {
        try {
            new FirebaseInstanceService().onTokenRefresh();
        }
        catch (GenericException e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        if (exception == null) onResponseReceived();
        else onExceptionReceived(exception);
        super.onPostExecute(v);
    }
}