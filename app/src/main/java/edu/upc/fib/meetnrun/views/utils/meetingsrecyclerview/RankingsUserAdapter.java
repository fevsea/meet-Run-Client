package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by Javier on 15/12/2017.
 */

public class RankingsUserAdapter {
    private List<User> neighbors;
    private final RecyclerViewOnClickListener listener;
    private View v;
    private Context context;

    public RankingsUserAdapter(List<User> neighbors, RecyclerViewOnClickListener listener, Context context) {
        this.neighbors = neighbors;
        this.listener = listener;
        this.context = context;
    }

    public UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        v = layoutInflater.inflate(R.layout.user_item_rankings, parent, false);
        return new UsersViewHolder(v,listener, context);
    }

    public void onBindViewHolder(final UsersViewHolder holder, int position) {
        User user = neighbors.get(position);
        holder.bindMeeting(user);
    }

    public int getItemCount() {
        return neighbors.size();
    }
}
