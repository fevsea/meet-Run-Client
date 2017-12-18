package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Friend;

public class PendingFriendsAdapter extends FriendsAdapter {

    public PendingFriendsAdapter(List<Friend> friends, RecyclerViewOnClickListener listener, Context context) {
        super(friends, listener, context);
    }

    @Override
    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        v = layoutInflater.inflate(R.layout.pending_friend_item, parent, false);
        return new PendingFriendViewHolder(v,listener, context);
    }
}
