package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.persistence.IGenericController;
import edu.upc.fib.meetnrun.persistence.WebDBController;
import edu.upc.fib.meetnrun.views.CreateMeetingActivity;
import edu.upc.fib.meetnrun.views.EditMeetingActivity;
import edu.upc.fib.meetnrun.views.MeetingInfoActivity;
import edu.upc.fib.meetnrun.views.MyMeetingsActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MyMeetingsAdapter;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MeetingsListener;
import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MyMeetingsListener;


public class MyMeetingsFragment extends Fragment {

    private MyMeetingsAdapter meetingsAdapter;
    private View view;
    private IGenericController controller;

    public MyMeetingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list,container,false);
        this.view = view;
        controller = WebDBController.getInstance();
        setupRecyclerView();

        FloatingActionButton fab =
                (FloatingActionButton) getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.add_group_512);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                meetingsAdapter.addItem(getContext());
                //TODO createNewMeeting();
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.fragment_meeting_swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMeetingList();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void setupRecyclerView() {
        final RecyclerView meetingsList = view.findViewById(R.id.fragment_meeting_container);
        meetingsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<Meeting> meetings = new ArrayList<>();
        updateMeetingList();
        meetingsAdapter = new MyMeetingsAdapter(meetings, new MyMeetingsListener() {
            @Override
            public void onStartClicked(int position) {
                Meeting selectedMeeting = meetingsAdapter.getMeetingAtPosition(position);
                startMeeting(selectedMeeting);
            }

            @Override
            public void onLeaveClicked(int position) {
                Meeting selectedMeeting = meetingsAdapter.getMeetingAtPosition(position);
                leaveMeeting(selectedMeeting);
            }

            @Override
            public void onMeetingClicked(int position) {
                Toast.makeText(view.getContext(), "Showing selected meeting info", Toast.LENGTH_SHORT).show();
                Meeting meeting = meetingsAdapter.getMeetingAtPosition(position);
                Intent meetingInfoIntent = new Intent(getActivity(),MeetingInfoActivity.class);
                meetingInfoIntent.putExtra("title",meeting.getTitle());
                meetingInfoIntent.putExtra("description",meeting.getDescription());
                String datetime = meeting.getDate();
                meetingInfoIntent.putExtra("date",datetime.substring(0,datetime.indexOf('T')));
                meetingInfoIntent.putExtra("time",datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));
                meetingInfoIntent.putExtra("level",String.valueOf(meeting.getLevel()));
                meetingInfoIntent.putExtra("latitude",meeting.getLatitude());
                meetingInfoIntent.putExtra("longitude",meeting.getLongitude());
                startActivity(meetingInfoIntent);

            }
        });
        meetingsList.setAdapter(meetingsAdapter);

    }

    private void updateMeetingList() {
            new GetMyMeetings().execute();
    }

    private void createNewMeeting() {
        Intent intent = new Intent(getActivity(),CreateMeetingActivity.class);
        getActivity().finish();
        startActivity(intent);
    }

    private void startMeeting(Meeting meeting) {
        //TODO Start tracking
    }

    private void showDialog(String title, String message, String okButtonText, String negativeButtonText, DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(okButtonText, ok);
        if (negativeButtonText != null && cancel != null)
            builder.setNegativeButton(negativeButtonText, cancel);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void leaveMeeting(Meeting meeting) {
        showDialog(getString(R.string.leave_meeting_label), getString(R.string.leave_meeting_message),
                    getString(R.string.ok), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO leave meeting call
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }
        );
    }

    private class GetMyMeetings extends AsyncTask<String,String,String> {
        List<Meeting> l = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            //TODO  l = controller.getMyMeetings();
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            System.err.println("FINISHED");
            meetingsAdapter.updateMeetingsList(l);
            super.onPostExecute(s);
        }
    }
}
