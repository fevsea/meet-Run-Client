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

public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    private List<User> users;
    private final RecyclerViewOnClickListener listener;
    private View v;
    private Context context;

    public UsersAdapter(List<User> users, RecyclerViewOnClickListener listener, Context context) {
        this.users = users;
        this.listener = listener;
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        v = layoutInflater.inflate(R.layout.user_item, parent, false);
        return new UsersViewHolder(v,listener, context);
    }

    @Override
    public void onBindViewHolder(final UsersViewHolder holder, int position) {
        User user = users.get(position);
        holder.bindMeeting(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addFriends(List<User> friends) {
        this.users.addAll(friends);
        notifyDataSetChanged();
    }


    public void updateFriendsList(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    public User getFriendAtPosition(int position) {
        return users.get(position);
    }
}
