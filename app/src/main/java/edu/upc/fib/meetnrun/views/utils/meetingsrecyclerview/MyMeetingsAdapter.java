package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;

public class MyMeetingsAdapter extends RecyclerView.Adapter<MyMeetingsViewHolder>{


    private List<Meeting> meetings;
    private final MyMeetingsListener listener;

    public MyMeetingsAdapter(List<Meeting> meetings, MyMeetingsListener listener) {
        this.meetings = meetings;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public MyMeetingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.mymeeting_item, parent, false);
        return new MyMeetingsViewHolder(view,listener);    }

    @Override
    public void onBindViewHolder(MyMeetingsViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        holder.bindMeeting(meeting);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }


    public void updateMeetingsList(List<Meeting> meetings) {
        this.meetings = meetings;
        notifyDataSetChanged();
    }

    public Meeting getMeetingAtPosition(int position) {
        return meetings.get(position);
    }
}
