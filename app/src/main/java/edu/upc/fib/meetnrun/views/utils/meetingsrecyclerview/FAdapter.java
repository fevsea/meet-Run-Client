package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;

/**
 * Created by eric on 28/11/17.
 */

public class FAdapter extends RecyclerView.Adapter<FViewHolder>  {


    private List<User> friendList = new ArrayList<>();
    private List<User> selectedFriends = new ArrayList<>();
    private Context c;
    private View v;
    private RecyclerViewOnClickListener listener;
    private CardView cardView;
    private User f;
    private boolean isGroup;

    public FAdapter(Context c, List<User> users, boolean isGroup, RecyclerViewOnClickListener listener) {
        selectedFriends.add(CurrentSession.getInstance().getCurrentUser());
        this.friendList = users;
        this.c = c;
        this.listener = listener;
        this.isGroup = isGroup;
        notifyDataSetChanged();
    }

    public FAdapter(List<User> friendList, Context c, boolean isGroup) {
        selectedFriends.add(CurrentSession.getInstance().getCurrentUser());
        this.friendList = friendList;
        this.c = c;
        this.isGroup = isGroup;
    }

    public void addSelectedFriend(User f) {
        selectedFriends.add(f);
    }

    public void removeSelectedFriend(User f) {
        selectedFriends.remove(f);
    }

    public void deleteSeledctedFriends() {
        selectedFriends.clear();
    }

    @Override
    public FViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        v = LayoutInflater.from(c).inflate(R.layout.user_item, parent, false);
        return new FViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(FViewHolder holder, int position) {

        f = friendList.get(position);

        char letter = f.getUsername().charAt(0);
        String firstLetter = String.valueOf(letter);

        holder.getUserPhoto().setText(firstLetter);
        holder.getUserPhoto().setBackground(getColoredCircularShape((letter)));

        holder.getUserName().setText(f.getUsername());
        holder.getName().setText(f.getFirstName()+" "+f.getLastName());
        holder.getPostCode().setText(f.getPostalCode());

        String level = String.valueOf(f.getLevel());
        if (level.equals("null")) level = "0";

        holder.getUserLevel().setText(level);

        cardView = holder.getCardView();

        if (isGroup) {
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cardView.isSelected()) {
                        cardView.setCardBackgroundColor(Color.WHITE);
                        cardView.setSelected(false);
                        f.setSelected(false);
                    }
                    else {
                        cardView.setCardBackgroundColor(ContextCompat.getColor(c, R.color.colorPrimaryLight));
                        cardView.setSelected(true);
                        f.setSelected(true);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }

    public List<User> getSelectedFriends() {
        return selectedFriends;
    }

    public User getFirstUser() {
        return selectedFriends.get(0);
    }

    public int getNumbSelectedFriends() {
        return selectedFriends.size()-1;
    }

    public void updateFriendsList(List<User> users) {
        this.friendList = users;
        notifyDataSetChanged();
    }

    public User getFriendAtPosition(int position) {
        return friendList.get(position);
    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = v.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(v.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

}
