package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class DeleteChat extends AsyncTask<Integer,String,String> implements AsyncTaskCallback{

    private IChatAdapter chatAdapter;

    public DeleteChat() {
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected String doInBackground(Integer... chatid) {
        chatAdapter.deleteChat(chatid[0]);
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        onResponseReceived();
    }
}
