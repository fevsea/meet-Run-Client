package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import java.lang.ref.WeakReference;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;


public class MyMeetingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private View view;
    private WeakReference<RecyclerViewOnClickListener> listener;
    private ImageButton startMeetingButton;

    public MyMeetingsViewHolder(View itemView, RecyclerViewOnClickListener listener) {
        super(itemView);
        view = itemView;
        this.listener = new WeakReference<>(listener);
    }

    public void bindMeeting(Meeting meeting) {

    }

    private GradientDrawable getColoredCircularShape(char letter) {
        int[] colors = view.getResources().getIntArray(R.array.colors);
        GradientDrawable circularShape = (GradientDrawable) ContextCompat.getDrawable(view.getContext(),R.drawable.user_profile_circular_text_view);
        int position = letter%colors.length;
        circularShape.setColor(colors[position]);
        return circularShape;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == startMeetingButton.getId()) {
            listener.get().onButtonClicked(getAdapterPosition());
        }
        else {
            listener.get().onMeetingClicked(getAdapterPosition());
        }
    }
}
