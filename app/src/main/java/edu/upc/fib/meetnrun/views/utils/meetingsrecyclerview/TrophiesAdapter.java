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


public class TrophiesAdapter extends RecyclerView.Adapter<TrophiesAdapter.ViewHolder> {
    private ArrayList<Trophies> galleryList;
    private final RecyclerViewOnClickListener listener;
    private Context context;

    public TrophiesAdapter(ArrayList<Trophies> galleryList, RecyclerViewOnClickListener listener) {
        this.galleryList = galleryList;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public TrophiesAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.trophie_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrophiesAdapter.ViewHolder viewHolder, int i) {
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        viewHolder.img.setImageResource((galleryList.get(i).getImage_ID()));
    }

    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        public ViewHolder(View view) {
            super(view);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}