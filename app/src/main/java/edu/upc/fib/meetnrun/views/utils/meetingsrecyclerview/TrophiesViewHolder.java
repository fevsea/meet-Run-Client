package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import edu.upc.fib.meetnrun.R;

public class TrophiesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final View view;
    ImageView img;
    WeakReference<RecyclerViewOnClickListener> listener;

    public TrophiesViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
        img = view.findViewById(R.id.img);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Log.d("TROPHIE","ONCLICK");
        listener.get().onItemClicked(getAdapterPosition());
    }
}