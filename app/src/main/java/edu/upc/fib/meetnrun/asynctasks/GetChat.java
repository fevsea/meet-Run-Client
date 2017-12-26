package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChat;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class GetChat extends AsyncTask<Integer,Void,Chat> implements AsyncTaskCallbackChat, AsyncTaskException{

    private GenericException exception;

    private IChatAdapter chatAdapter;

    public GetChat() {
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected Chat doInBackground(Integer... integers)  {
        try {
            return chatAdapter.getChat(integers[0]);
        }
        catch (GenericException e) {
            exception = e;
            return null;
        }
    }
    @Override
    protected void onPostExecute(Chat chat) {
        if (exception == null) onResponseReceived(chat);
        else onExceptionReceived(exception);
        super.onPostExecute(chat);
    }

}