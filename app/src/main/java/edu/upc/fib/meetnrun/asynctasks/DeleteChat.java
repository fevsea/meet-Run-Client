package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import com.google.android.gms.auth.api.Auth;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class DeleteChat extends AsyncTask<Integer,String,String> implements AsyncTaskCallback,AsyncTaskException{

    private GenericException exception;
    private IChatAdapter chatAdapter;

    public DeleteChat() {
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected String doInBackground(Integer... chatid) {
        try {
            chatAdapter.deleteChat(chatid[0]);
        }
        catch (GenericException e){
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        if (exception == null) onResponseReceived();
        else onExceptionReceived(exception);
        super.onPostExecute(s);
    }
}
