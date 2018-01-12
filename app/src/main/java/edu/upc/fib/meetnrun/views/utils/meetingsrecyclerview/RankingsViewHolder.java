package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    public void setPositionColors(int positionNum){
        if (positionNum==0){
            position.setBackground(getColoredCircularShape(0));
        }
        else if (positionNum==1){
            position.setBackground(getColoredCircularShape(1));
        }
        else if (positionNum==2){
            position.setBackground(getColoredCircularShape(2));
        }
        else {
            position.setBackground(getColoredCircularShape(3));
        }
    }
    public void bindUserRanking(PositionUser ranking, int positionNum) {
        position = view.findViewById(R.id.ranking_item_user_photo2);
        km = view.findViewById(R.id.ranking_item_km);
        username = view.findViewById(R.id.ranking_item_username);
        realname = view.findViewById(R.id.ranking_item_name);
        zip = view.findViewById(R.id.ranking_item_postcode);

        position.setText(String.valueOf(positionNum+1));
        setPositionColors(positionNum);

        Log.e("AAA",ranking.getPosition() + "");
        km.setText(String.valueOf(ranking.getDistance()/1000.000 + " km"));
        username.setText(ranking.getUserID());
        realname.setText(ranking.getFirstName() + " " + ranking.getLastName());
        String postalCode=String.valueOf(ranking.getZip());
        if (postalCode.length()==4) postalCode="0"+postalCode;
        zip.setText(postalCode);

        view.setOnClickListener(this);
    }

    public void bindZipRanking(Position ranking, int positionNum) {
        position = view.findViewById(R.id.ranking_zip_position);
        km = view.findViewById(R.id.zip_km);
        zip = view.findViewById(R.id.zip_code);

        position.setText(String.valueOf(positionNum+1));
        setPositionColors(positionNum);
        km.setText(String.valueOf(ranking.getDistance()/1000.000 + " km"));
        String postalCode=String.valueOf(ranking.getZip());
        if (postalCode.length()==4) postalCode="0"+postalCode;
        zip.setText(postalCode);

        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Log.d("EEE","Clickeddddd");
        listener.get().onItemClicked(getAdapterPosition());
    }

    private GradientDrawable getColoredCircularShape(int pos) {

        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);

        if (pos == 0) {
            circularShape.setColor(ContextCompat.getColor(view.getContext(), R.color.gold));
        }
        else if (pos == 1) {
            circularShape.setColor(ContextCompat.getColor(view.getContext(), R.color.silver));
        }
        else if (pos == 2) {
            circularShape.setColor(ContextCompat.getColor(view.getContext(), R.color.bronze));
        }
        else {
            circularShape.setColor(ContextCompat.getColor(view.getContext(), R.color.blue_profile));
        }
        return circularShape;
    }
}
