package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.User;

public interface AsyncTaskCallbackUsers {
    void onResponseReceived(List<User> users);
}
