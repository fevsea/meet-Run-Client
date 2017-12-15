package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.view.View;

import java.util.List;

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


}
