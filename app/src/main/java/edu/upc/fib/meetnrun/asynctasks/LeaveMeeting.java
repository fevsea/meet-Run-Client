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
    LeaveMeeting.execute(meetingId,chatId)
*/

public abstract class LeaveMeeting extends AsyncTask<Integer,Void,Void> implements AsyncTaskCallback,AsyncTaskException{

    private GenericException exception;
    private IMeetingAdapter meetingAdapter;
    private IChatAdapter chatAdapter;

    public LeaveMeeting() {
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        //TODO possible millora: crida al servidor leaveChat
        try {
            meetingAdapter.leaveMeeting(integers[0], CurrentSession.getInstance().getCurrentUser().getId());
            Chat chat = chatAdapter.getChat(integers[1]);
            List<User> chatUsers = chat.getListUsersChat();
            chatUsers.remove(CurrentSession.getInstance().getCurrentUser());
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