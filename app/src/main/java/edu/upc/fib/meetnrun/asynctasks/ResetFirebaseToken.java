package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class ResetFirebaseToken extends AsyncTask<Void,Void,Void> implements AsyncTaskCallback,AsyncTaskException{

    private GenericException exception;
    private ILoginAdapter loginAdapter;

    public ResetFirebaseToken(){
        loginAdapter = CurrentSession.getInstance().getLoginAdapter();
    }

    @Override
    protected Void doInBackground(Void... v){
        try {
            loginAdapter.resetFirebaseToken();
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
