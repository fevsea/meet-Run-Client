package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.Message;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 2/11/17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsViewHolder> {

    private List<User> users;
    private RecyclerViewOnClickListener listener;
    private View v;

    public FriendsAdapter(List<User> users, RecyclerViewOnClickListener listener) {
        this.users = users;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public FriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        v = layoutInflater.inflate(R.layout.user_item, parent, false);
        return new FriendsViewHolder(v,listener);
    }

    @Override
    public void onBindViewHolder(final FriendsViewHolder holder, int position) {
        User user = users.get(position);
        holder.bindMeeting(user);
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
