package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackUser;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

public abstract class Register extends AsyncTask<String,String,User> implements AsyncTaskCallbackUser {

    private IUserAdapter userAdapter;
    private String name, surname, username, password1, quest, answ,pcInt;


    public Register(String name, String surname, String username, String password1, String quest, String answ, String pcInt) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password1 = password1;
        this.quest = quest;
        this.answ = answ;
        this.pcInt = pcInt;
        userAdapter = CurrentSession.getInstance().getUserAdapter();
    }

    @Override
    protected User doInBackground(String... registerUser) throws ParamsException {
        return userAdapter.registerUser(username, name, surname, pcInt, password1, quest, answ);
    }

    @Override
    protected void onPostExecute(User u) {
        onResponseReceied(u);
        super.onPostExecute(u);
    }
}