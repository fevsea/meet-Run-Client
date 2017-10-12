package edu.upc.fib.gps.meetnrun.Meetings.MeetingsRecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MeetingsViewHolder extends RecyclerView.ViewHolder{


    public View view;

    public MeetingsViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public void bindMeeting(Meeting meeting) {
        //TODO once the meeting model is created, bind the views to its values
    }
}
