package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;


/*
    new JoinMeeting.execute(meetingId,userId,chatId)
 */
public abstract class JoinMeeting extends AsyncTask<Integer,Void,Void> implements AsyncTaskCallback,AsyncTaskException {

    private GenericException exception;
    private Chat chat;
    private IChatAdapter chatAdapter;
    private IMeetingAdapter meetingAdapter;



    public JoinMeeting() {
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    protected Void doInBackground(Integer... integers) throws AuthorizationException,ParamsException,NotFoundException{
        //TODO possible millora: crida al servidor joinChat
        try {
            meetingAdapter.joinMeeting(integers[0], integers[1]);
            Chat chat = chatAdapter.getChat(integers[2]);
            List<User> chatUsers = chat.getListUsersChat();
            chatUsers.add(CurrentSession.getInstance().getCurrentUser());
            chat.setListUsersChat(chatUsers);
            chatAdapter.updateChat(chat);
        }
        catch (GenericException e) {
            exception = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        if (exception == null) onResponseReceived();
        else onExceptionReceived(exception);
        super.onPostExecute(v);
    }
}