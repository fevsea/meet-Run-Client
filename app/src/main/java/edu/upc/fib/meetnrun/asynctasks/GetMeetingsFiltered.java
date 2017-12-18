package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;


public abstract class GetMeetingsFiltered extends AsyncTask<String,Void,List<Meeting> > implements AsyncTaskCallback {
    private int page;
    private IMeetingAdapter meetingAdapter;

    public GetMeetingsFiltered(int page) {
        setPage(page);
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }
    @Override
    protected List<Meeting> doInBackground(String... query) {
        return meetingAdapter.getAllMeetingsFilteredByName(query[0],page);
    }

    @Override
    protected void onPostExecute(List<Meeting> meetings) {
        onResponseReceived(meetings);
    }

    private int getPage() {
        return this.page;
    }

    private void setPage(int page) {
        this.page = page;
    }

}