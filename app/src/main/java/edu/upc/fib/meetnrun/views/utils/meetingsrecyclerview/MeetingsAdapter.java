package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsViewHolder> {

    private List<Meeting> meetings;
    private final RecyclerViewOnClickListener listener;
    private List<Meeting> myMeetings;

    public MeetingsAdapter(List<Meeting> meetings, RecyclerViewOnClickListener listener) {
        this.meetings = meetings;
        this.listener = listener;
        notifyDataSetChanged();
    }

    @Override
    public MeetingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.meeting_item, parent, false);
        return new MeetingsViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(final MeetingsViewHolder holder, int position) {
        Meeting meeting = meetings.get(position);
        boolean joined = false;
        if (myMeetings != null) {
            for (Meeting joinedMeeting : myMeetings) {
                if (joinedMeeting.getId().equals(meeting.getId())) joined = true;
            }
        }
        holder.bindMeeting(meeting,joined);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }


    public void addMeetings(List<Meeting> meetings) {
        this.meetings.addAll(meetings);
        notifyDataSetChanged();
    }

    public void updateMeetingsList(List<Meeting> meetings) {
        this.meetings = meetings;
        notifyDataSetChanged();
    }

    public void setMyMeetings(List<Meeting> meetings) {
        this.myMeetings = meetings;
        notifyDataSetChanged();
    }

    public Meeting getMeetingAtPosition(int position) {
        return meetings.get(position);
    }
}
