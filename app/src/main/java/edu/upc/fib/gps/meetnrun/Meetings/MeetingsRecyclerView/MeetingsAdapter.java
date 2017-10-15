package edu.upc.fib.gps.meetnrun.Meetings.MeetingsRecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import edu.upc.fib.gps.meetnrun.R;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsViewHolder> {

    private List<Meeting> meetings;

    public MeetingsAdapter(List<Meeting> meetings) {
        this.meetings = meetings;
    }

    @Override
    public MeetingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.meeting_item, parent, false);
        return new MeetingsViewHolder(view);
    }

    //TODO delete this once connection to db is available
    public void addItem(Context context) {
        meetings.add(new Meeting());
        notifyItemInserted(getItemCount());
        notifyDataSetChanged();
        Toast.makeText(context,"New meeting: " + getItemCount(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBindViewHolder(final MeetingsViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        holder.bindMeeting(meeting);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }


    public void updateMeetingsList(List<Meeting> meetings) {
       //TODO update this.meetings = meetings;
        notifyDataSetChanged();
    }

    public Meeting getMeetingAtPosition(int position) {
        return meetings.get(position);
    }
}
