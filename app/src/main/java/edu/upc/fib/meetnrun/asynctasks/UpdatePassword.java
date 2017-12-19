package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ForbiddenException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class UpdatePassword extends AsyncTask<String, Void, Boolean> implements AsyncTaskCallbackBoolean{

    private ILoginAdapter loginAdapter;

    public UpdatePassword() {
        loginAdapter = CurrentSession.getInstance().getLoginAdapter();
    }

    @Override
    protected Boolean doInBackground(String... params) {
         return loginAdapter.changePassword(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(Boolean b) {
        onResponseReceived(b);
        super.onPostExecute(b);
    }
}