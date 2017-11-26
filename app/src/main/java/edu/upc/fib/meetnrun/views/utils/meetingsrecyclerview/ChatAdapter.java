package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 21/11/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<Chat> chats;
    private RecyclerViewOnClickListener listener;
    private View v;
    private boolean newChat = false;

    public ChatAdapter(List<Chat> chats, RecyclerViewOnClickListener listener) {
        this.chats = chats;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        v = layoutInflater.inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        Chat chat = chats.get(position);
        if (!chat.getMessage().getName().equals(CurrentSession.getInstance().getCurrentUser().getUsername())) {
            newChat = true;
        }
        holder.bindChat(chat, newChat);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }


    public void updateChatList(List<Chat> chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public Chat getChatAtPosition(int position) {
        return chats.get(position);
    }
}
