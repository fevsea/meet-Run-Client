package edu.upc.fib.meetnrun.asynctasks.callbacks;


import edu.upc.fib.meetnrun.models.User;

public interface AsyncTaskCallbackUser {
    void onResponseReceived(User u);
}
