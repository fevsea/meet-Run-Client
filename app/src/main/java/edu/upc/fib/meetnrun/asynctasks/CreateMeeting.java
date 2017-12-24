package edu.upc.fib.meetnrun.asynctasks;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import edu.upc.fib.meetnrun.adapters.IChatAdapter;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackMeeting;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.ParamsException;
import edu.upc.fib.meetnrun.models.Chat;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.views.MeetingFriendsActivity;

public abstract class CreateMeeting extends AsyncTask<String,Void,Meeting> implements AsyncTaskCallbackMeeting{
    private ArrayList<Integer> owner;
    private Date currentDate;
    private IMeetingAdapter meetingAdapter;
    private IChatAdapter chatAdapter;
    private String name;
    private String description;
    private boolean publ;
    private Integer level;
    private String date;
    private String lat;
    private String lon;

    public CreateMeeting(String name, String description, boolean publ, Integer level, String date, String lat, String lon) {
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
        chatAdapter = CurrentSession.getInstance().getChatAdapter();
        owner = new ArrayList<>();
        owner.add(CurrentSession.getInstance().getCurrentUser().getId());
        Calendar calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        this.name = name;
        this.description = description;
        this.publ = publ;
        this.level = level;
        this.date = date;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    protected Meeting doInBackground(String... strings) throws AuthorizationException,ParamsException{
        Meeting meeting= meetingAdapter.createMeeting(name,description,publ,level,date,lat,lon,null);
        Chat chat = chatAdapter.createChat(name,owner,1,meeting.getId(),"",0,currentDate);
        CurrentSession.getInstance().setChat(chat);
        return meeting;
    }

    @Override
    protected void onPostExecute(Meeting meeting){
        onResponseReceived(meeting);
        super.onPostExecute(meeting);
    }
}