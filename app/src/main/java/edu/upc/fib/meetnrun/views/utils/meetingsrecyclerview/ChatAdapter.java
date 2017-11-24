package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 21/11/17.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {

    private List<Chat> chats;
    private RecyclerViewOnClickListener listener;
    private View v;

    public ChatAdapter(List<Chat> chats, RecyclerViewOnClickListener listener) {
        this.chats = chats;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Chat c = chats.get(position);

        return 1;
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
        holder.bindChat(chat);
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
