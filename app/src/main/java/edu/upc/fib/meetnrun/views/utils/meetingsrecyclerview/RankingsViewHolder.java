package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.Position;
import edu.upc.fib.meetnrun.models.PositionUser;
import edu.upc.fib.meetnrun.models.RankingUser;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;

/**
 * Created by Javier on 05/01/2018.
 */

public class RankingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private final View view;
    private final WeakReference<RecyclerViewOnClickListener> listener;
    private TextView position;
    private TextView km;
    private TextView username;
    private TextView realname;
    private TextView zip;


    public RankingsViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindUserRanking(PositionUser ranking) {
        position = view.findViewById(R.id.ranking_item_user_photo2);
        km = view.findViewById(R.id.ranking_item_km);
        username = view.findViewById(R.id.ranking_item_username);
        realname = view.findViewById(R.id.ranking_item_name);
        zip = view.findViewById(R.id.ranking_item_postcode);

        position.setText(ranking.getPosition());
        km.setText(String.valueOf(ranking.getDistance() + " km"));
        username.setText(ranking.getUserID());
        realname.setText(ranking.getFirstName() + " " + ranking.getLastName());
        zip.setText(ranking.getZip());

        view.setOnClickListener(this);
    }

    public void bindZipRanking(Position ranking) {
        position = view.findViewById(R.id.ranking_item_user_photo2);
        km = view.findViewById(R.id.ranking_item_km);
        zip = view.findViewById(R.id.ranking_item_postcode);

        position.setText(ranking.getPosition());
        km.setText(String.valueOf(ranking.getDistance() + " km"));
        zip.setText(ranking.getZip());

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            listener.get().onItemClicked(getAdapterPosition());
    }
}
