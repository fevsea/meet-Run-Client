package edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.FeedMeeting;

public class FeedMeetingsAdapter extends RecyclerView.Adapter<FeedMeetingViewHolder> {

    private List<FeedMeeting> meetings;
    private RecyclerViewOnClickListener listener;

    public FeedMeetingsAdapter(List<FeedMeeting> meetings, RecyclerViewOnClickListener listener) {
        this.meetings = meetings;
        this.listener = listener;
    }

    @Override
    public FeedMeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View v = layoutInflater.inflate(R.layout.feedmeeting_item, parent, false);
        return new FeedMeetingViewHolder(v, this.listener);
    }

    @Override
    public void onBindViewHolder(FeedMeetingViewHolder holder, int position) {
        FeedMeeting meeting = this.meetings.get(position);
        holder.bindMeeting(meeting);
    }

    public int getItemViewType(int position) {
        return meetings.get(position).getType();
    }

    public void updateMeetingsList(List<FeedMeeting> meetings) {
        this.meetings = meetings;
        notifyDataSetChanged();
    }

    public void addMeetings(List<FeedMeeting> meetings) {
        this.meetings.addAll(meetings);
        notifyDataSetChanged();
    }

    public FeedMeeting getItemAt(int position) {
        return meetings.get(position);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }
}
