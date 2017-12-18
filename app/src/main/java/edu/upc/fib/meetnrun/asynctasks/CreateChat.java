package edu.upc.fib.meetnrun.asynctasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import java.util.Date;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackChat;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;

public abstract class CreateChat extends AsyncTask<Void,Void,Chat> implements AsyncTaskCallbackChat{

    private IChatAdapter chatAdapter;
    private String chatName;
    private List<Integer> listUsersChatIDs;
    private int type;
    private int meetingId;
    private String lastMessage;
    private int lastMessageUserNamePosition;
    private Date lastDateTime;

    public CreateChat(String chatName, List<Integer> listUsersChatIDs, int type, Integer meetingId, String lastMessage,
                      int lastMessageUserNamePosition, Date lastDateTime) {

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
        return chatAdapter.createChat(chatName, listUsersChatIDs, type,meetingId,
                lastMessage, lastMessageUserNamePosition, lastDateTime);
    }

    @Override
    protected void onPostExecute(Chat chat) {
        onResponseReceived(chat);
        super.onPostExecute(chat);
    }
}