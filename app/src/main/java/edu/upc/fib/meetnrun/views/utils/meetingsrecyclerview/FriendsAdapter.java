package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 2/11/17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    private List<User> users;
    private final RecyclerViewOnClickListener listener;
    private View v;
    private Context context;
    private boolean isGroup;

    public FriendsAdapter(List<User> users, RecyclerViewOnClickListener listener, Context context, boolean isGroup) {
        this.users = users;
        this.listener = listener;
        this.context = context;
        this.isGroup = isGroup;
        notifyDataSetChanged();
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        v = layoutInflater.inflate(R.layout.user_item, parent, false);
        return new FriendsViewHolder(v,listener, context);
    }

    @Override
    public void onBindViewHolder(final FriendsViewHolder holder, int position) {
        User user = users.get(position);
        holder.bindMeeting(user, isGroup);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public void updateFriendsList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public User getFriendAtPosition(int position) {
        return users.get(position);
    }
}
