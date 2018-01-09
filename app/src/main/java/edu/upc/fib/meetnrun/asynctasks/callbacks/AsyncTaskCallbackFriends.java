package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.Friend;

public interface AsyncTaskCallbackFriends {
    void onResponseReceived(List<Friend> friends);
}
