package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class DeleteAccount extends AsyncTask<Void,Void,Void> implements AsyncTaskCallback{

    private IUserAdapter userAdapter;

    @Override
    protected Void doInBackground(Void... v) throws NotFoundException, AuthorizationException,ParamsException{
        userAdapter.deleteUserByID(CurrentSession.getInstance().getCurrentUser().getId());
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        onResponseReceived();
        super.onPostExecute(v);
    }
}
