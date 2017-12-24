package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class UpdateUser extends AsyncTask<User, Void, Boolean> implements AsyncTaskCallbackBoolean{

    private IUserAdapter userAdapter;

    public UpdateUser() {
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }

    @Override
    protected Boolean doInBackground(User... params) throws AuthorizationException,NotFoundException,ParamsException {
        return userAdapter.updateUser(params[0]);
    }

    @Override
    protected void onPostExecute(Boolean actualitzat_correctament) {
        onResponseReceived(actualitzat_correctament);
        super.onPostExecute(actualitzat_correctament);
    }
}