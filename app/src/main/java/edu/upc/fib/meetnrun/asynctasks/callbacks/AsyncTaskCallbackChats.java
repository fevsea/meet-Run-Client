package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.Chat;

public interface AsyncTaskCallbackChats {
    void onResponseReceived(List<Chat> chats);
}
