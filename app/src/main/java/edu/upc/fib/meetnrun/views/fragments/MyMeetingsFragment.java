package edu.upc.fib.meetnrun.views.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.adapters.IUserAdapter;
import edu.upc.fib.meetnrun.asynctasks.GetMyMeetings;
import edu.upc.fib.meetnrun.asynctasks.LeaveMeeting;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.BaseActivity;
import edu.upc.fib.meetnrun.views.TrackingActivity;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MyMeetingsAdapter;
import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.views.utils.meetingsrecyclerview.MyMeetingsListener;


public class MyMeetingsFragment extends BaseFragment {

    private MyMeetingsAdapter meetingsAdapter;
    private View view;
    public MyMeetingsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meeting_list,container,false);
        this.view = view;
        setupRecyclerView();

        FloatingActionButton fab =
                getActivity().findViewById(R.id.activity_fab);
        fab.setImageResource(R.drawable.add_group_512);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewMeeting();
            }
        });
        final SwipeRefreshLayout swipeRefreshLayout =
                view.findViewById(R.id.fragment_meeting_swipe);
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
                Toast.makeText(view.getContext(), "Showing selectedUsers meeting info", Toast.LENGTH_SHORT).show();
                Meeting meeting = meetingsAdapter.getMeetingAtPosition(position);
                Intent meetingInfoIntent = new Intent();
                meetingInfoIntent.putExtra("title",meeting.getTitle());
                meetingInfoIntent.putExtra("chat",meeting.getChatID());
                meetingInfoIntent.putExtra("owner",meeting.getOwner().getUsername());
                meetingInfoIntent.putExtra("id",meeting.getId());
                meetingInfoIntent.putExtra("description",meeting.getDescription());
                String datetime = meeting.getDate();
                meetingInfoIntent.putExtra("date",datetime.substring(0,datetime.indexOf('T')));
                meetingInfoIntent.putExtra("time",datetime.substring(datetime.indexOf('T')+1,datetime.indexOf('Z')));
                meetingInfoIntent.putExtra("level",String.valueOf(meeting.getLevel()));
                meetingInfoIntent.putExtra("latitude",meeting.getLatitude());
                meetingInfoIntent.putExtra("longitude",meeting.getLongitude());
                BaseActivity.startWithFragment(getActivity(), new MeetingInfoFragment(), meetingInfoIntent);

            }
        });
        updateMeetingList();
        meetingsList.setAdapter(meetingsAdapter);

    }

    private void updateMeetingList() {
            callGetMyMeetings(CurrentSession.getInstance().getCurrentUser().getId());
    }

    private void createNewMeeting() {
        BaseActivity.startWithFragment(getActivity(), new CreateMeetingFragment());
    }

    private void startMeeting(Meeting meeting) {
        Intent intent = new Intent(getActivity(), TrackingActivity.class);
        intent.putExtra("id", meeting.getId());
        startActivity(intent);
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

    private void leaveMeeting(final Meeting meeting) {
        showDialog(getString(R.string.leave_meeting_label), getString(R.string.leave_meeting_message),
                    getString(R.string.ok), getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callLeaveMeeting(meeting.getId(),meeting.getChatID());
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

    private void callGetMyMeetings(int userId) {
        GetMyMeetings getMyMeetings = new GetMyMeetings() {

            @Override
            public void onResponseReceived(List<Meeting> myMeetings) {
                meetingsAdapter.updateMeetingsList(myMeetings);
            }
        };
        try {
            getMyMeetings.execute(userId);
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
        catch (ParamsException e) {
            Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
        }
    }


    private void callLeaveMeeting(int meetingId, int chatId) {
        LeaveMeeting leaveMeeting = new LeaveMeeting() {
            @Override
            public void onResponseReceived() {
                updateMeetingList();
            }
        };
        try {
            leaveMeeting.execute(meetingId,chatId);
        }
        catch (AuthorizationException e) {
            Toast.makeText(getActivity(), R.string.authorization_error, Toast.LENGTH_LONG).show();
        }
        catch (ParamsException e) {
            Toast.makeText(getActivity(), R.string.params_error, Toast.LENGTH_LONG).show();
        }
    }

    public int getTitle() {
        return R.string.mymeetings_label;
    }

}
