package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChats;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class GetChats extends AsyncTask<Void,Void,List<Chat>> implements AsyncTaskCallbackChats,AsyncTaskException {

    private GenericException exception;
    private int page;
    private IChatAdapter chatAdapter;

    public GetChats(int page) {
        this.page = page;
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected List<Chat> doInBackground(Void... v) throws AuthorizationException {
        try {
            return chatAdapter.getChats(page);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(List<Chat> chats) {
        if (exception == null) onResponseReceived(chats);
        else onExceptionReceived(exception);
        super.onPostExecute(chats);
    }
}