package edu.upc.fib.meetnrun.asynctasks;


import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackTrophies;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Trophie;

public abstract class GetTrophies extends AsyncTask<Integer,Void,List<Trophie>> implements AsyncTaskCallbackTrophies {

    private IUserAdapter userAdapter;

    public GetTrophies() {
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }

    @Override
    protected List<Trophie> doInBackground(Integer... integers) {
        return userAdapter.getUserTrophieByID(integers[0]);
    }

    @Override
    protected void onPostExecute(List<Trophie> trophies) {
        Log.e("asynctask", String.valueOf(trophies));
        onResponseReceived(trophies);
        super.onPostExecute(trophies);
    }
}
