package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class UpdateChat extends AsyncTask<Chat,String,String> implements AsyncTaskCallback{

    private IChatAdapter chatAdapter;

    public UpdateChat() {
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }
    @Override
    protected String doInBackground(Chat... chats) {
        chatAdapter.updateChat(chats[0]);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        onResponseReceived();
    }
}