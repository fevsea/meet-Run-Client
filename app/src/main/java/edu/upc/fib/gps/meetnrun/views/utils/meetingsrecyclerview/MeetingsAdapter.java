package edu.upc.fib.gps.meetnrun.views.utils.meetingsrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import edu.upc.fib.gps.meetnrun.R;
import edu.upc.fib.gps.meetnrun.models.Meeting;

public class MeetingsAdapter extends RecyclerView.Adapter<MeetingsViewHolder> {

    private List<Meeting> meetings;
    private RecyclerViewOnClickListener listener;

    public MeetingsAdapter(List<Meeting> meetings, RecyclerViewOnClickListener listener) {
        this.meetings = meetings;
        this.listener = listener;
    }

    @Override
    public MeetingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.meeting_item, parent, false);
        return new MeetingsViewHolder(view,listener);
    }

    //TODO delete this once connection to db is available
    public void addItem(Context context) {
        int id = 1;
        String title = "Meeting at this place";
        String description = "Meeting description test";
        String letters = "ABCDEFGHIJKLMNÑOPQRSTUVWXYZ";
        Random random = new Random();
        String creatorAuthor = letters.charAt(random.nextInt(letters.length())) + "User name here";
        Date dateTime = Calendar.getInstance().getTime();
        boolean isPublic = true; int level = 10; float latitude = 0; float longitude = 0;
        meetings.add(new Meeting(id,title,description,creatorAuthor,dateTime,isPublic,level,latitude,longitude));
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
        //TODO this.meetings = meetings;
        notifyDataSetChanged();
    }

    public Meeting getMeetingAtPosition(int position) {
        return meetings.get(position);
    }
}
