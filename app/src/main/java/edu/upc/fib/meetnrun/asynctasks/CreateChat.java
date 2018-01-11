package edu.upc.fib.meetnrun.asynctasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChat;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskException;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class CreateChat extends AsyncTask<Void,Void,Chat> implements AsyncTaskCallbackChat, AsyncTaskException{

    private GenericException exception;
    private IChatAdapter chatAdapter;
    private String chatName;
    private List<Integer> listUsersChatIDs;
    private int type;
    private Integer meetingId;
    private String lastMessage;
    private String lastMessageUserNamePosition;
    private Date lastDateTime;

    public CreateChat(String chatName, List<Integer> listUsersChatIDs, int type, Integer meetingId, String lastMessage,
                      String lastMessageUserNamePosition, Date lastDateTime) {

        chatAdapter = CurrentSession.getInstance().getChatAdapter();
        this.chatName = chatName;
        this.listUsersChatIDs = listUsersChatIDs;
        this.type = type;
        this.meetingId = meetingId;
        this.lastMessage = lastMessage;
        this.lastMessageUserNamePosition = lastMessageUserNamePosition;
        this.lastDateTime = lastDateTime;
    }

    @Override
    protected Chat doInBackground(Void... v) {
        try {
            return chatAdapter.createChat(chatName, listUsersChatIDs, type, meetingId,
                    lastMessage, lastMessageUserNamePosition, lastDateTime);
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