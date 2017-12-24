package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChats;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class GetChats extends AsyncTask<Void,Void,List<Chat>> implements AsyncTaskCallbackChats {

    private int page;
    private IChatAdapter chatAdapter;

    public GetChats(int page) {
        this.page = page;
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected List<Chat> doInBackground(Void... v) throws AuthorizationException {
        return chatAdapter.getChats(page);
    }

    @Override
    protected void onPostExecute(List<Chat> chats) {
        onResponseReceived(chats);
        super.onPostExecute(chats);
    }
}