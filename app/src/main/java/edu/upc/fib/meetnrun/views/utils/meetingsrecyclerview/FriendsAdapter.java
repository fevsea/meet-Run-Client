package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Friend;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 4/12/17.
 */

public class FriendsAdapter extends RecyclerView.Adapter<UsersViewHolder> {

    private List<Friend> friends;
    protected final RecyclerViewOnClickListener listener;
    protected View v;
    protected Context context;

    public FriendsAdapter(List<Friend> friends, RecyclerViewOnClickListener listener, Context context) {
        this.friends = friends;
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
        Friend f = friends.get(position);
        User friend = f.getFriend();
        if (CurrentSession.getInstance().getCurrentUser().getUsername().equals(friend.getUsername())) friend = f.getUser();
        holder.bindMeeting(friend);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public void addFriends(List<Friend> friends) {
        this.friends.addAll(friends);
        notifyDataSetChanged();
    }


    public void updateFriendsList(List<Friend> friends) {
        this.friends = friends;
        notifyDataSetChanged();
    }

    public Friend getFriendAtPosition(int position) {

        return friends.get(position);
    }
}
