package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.view.View;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUsers;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;

public abstract class GetUsers extends AsyncTask<String,Void,List<User>> implements AsyncTaskCallbackUsers {

    private IUserAdapter userAdapter;
    private int page;

    public GetUsers(int page) {
        this.page = page;
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }



    @Override
    protected List<User> doInBackground(String... strings) {
        return userAdapter.getAllUsers(page);
    }

    @Override
    protected void onPostExecute(List<User> users) {
        onResponseReceived(users);
        super.onPostExecute(users);
    }
}