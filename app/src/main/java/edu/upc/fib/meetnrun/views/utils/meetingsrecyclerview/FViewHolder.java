package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import edu.upc.fib.meetnrun.R;

/**
 * Created by eric on 28/11/17.
 */

public class FViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView userPhoto;
    private TextView userName;
    private TextView name;
    private TextView postCode;
    private TextView userLevel;
    private CardView cardView;
    private WeakReference<RecyclerViewOnClickListener> listener;

    public FViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        this.listener = new WeakReference<>(listener);
        userPhoto = (TextView) itemView.findViewById(R.id.user_photo);
        userName = (TextView) itemView.findViewById(R.id.user_username);
        name = (TextView) itemView.findViewById(R.id.user_name);
        postCode = (TextView) itemView.findViewById(R.id.user_postcode);
        userLevel = (TextView) itemView.findViewById(R.id.user_level);
        cardView = (CardView) itemView.findViewById(R.id.user_cardview);
    }

    public TextView getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(TextView userPhoto) {
        this.userPhoto = userPhoto;
    }

    public TextView getUserName() {
        return userName;
    }

    public void setUserName(TextView userName) {
        this.userName = userName;
    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getPostCode() {
        return postCode;
    }

    public void setPostCode(TextView postCode) {
        this.postCode = postCode;
    }

    public TextView getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(TextView userLevel) {
        this.userLevel = userLevel;
    }

    public CardView getCardView() {
        return cardView;
    }

    public void setCardView(CardView cardView) {
        this.cardView = cardView;
    }

    @Override
    public void onClick(View view) {

        listener.get().onItemClicked(getAdapterPosition());
    }

}
