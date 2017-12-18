package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChat;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class GetChat extends AsyncTask<Integer,Void,Chat> implements AsyncTaskCallbackChat{

    private IChatAdapter chatAdapter;

    public GetChat() {
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected Chat doInBackground(Integer... integers) {
            return chatAdapter.getChat(integers[0]);
    }
    @Override
    protected void onPostExecute(Chat chat) {
        onResponseReceived(chat);
        super.onPostExecute(chat);
    }

}