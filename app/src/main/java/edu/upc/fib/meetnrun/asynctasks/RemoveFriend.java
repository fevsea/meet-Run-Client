package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IFriendsAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallback;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackBoolean;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;


public abstract class RemoveFriend extends AsyncTask<String,Void,Void> implements AsyncTaskCallbackBoolean{

    boolean ok = false;
    private IFriendsAdapter friendsAdapter;
    private IChatAdapter chatAdapter;

    public RemoveFriend() {
        friendsAdapter = CurrentSession.getInstance().getFriendsAdapter();
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
    }

    @Override
    protected Void doInBackground(String... s) throws AuthorizationException,ParamsException,NotFoundException{
        ok = friendsAdapter.removeFriend(Integer.parseInt(s[0]));
        if (ok) {
            Chat chat;
            User currentFriend = CurrentSession.getInstance().getFriend();
            chat = chatAdapter.getPrivateChat(currentFriend.getId());
            if (chat != null) {
                FirebaseDatabase.getInstance().getReference(String.valueOf(chat.getId())).removeValue();
                chatAdapter.deleteChat(chat.getId());
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        onResponseReceived(ok);
        super.onPostExecute(v);
    }
}