package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import java.util.ArrayList;

import edu.upc.fib.meetnrun.models.Trophies;
import edu.upc.fib.meetnrun.R;


public class TrophiesAdapter extends RecyclerView.Adapter<TrophiesViewHolder> {
    private ArrayList<Trophies> galleryList;
    private final RecyclerViewOnClickListener listener;
    private Context context;

    public TrophiesAdapter(ArrayList<Trophies> galleryList, RecyclerViewOnClickListener listener) {
        this.galleryList = galleryList;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public TrophiesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.trophie_item, viewGroup, false);
        return new TrophiesViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(TrophiesViewHolder viewHolder, int i) {
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageResource((galleryList.get(i).getImage_ID()));
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public Trophies getTrophieAtPosition(int position) {
        return galleryList.get(position);
    }
}