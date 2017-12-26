package edu.upc.fib.meetnrun.asynctasks;

import android.os.AsyncTask;
import java.util.List;
import edu.upc.fib.meetnrun.adapters.IMeetingAdapter;
import edu.upc.fib.meetnrun.asynctasks.callbacks.AsyncTaskCallbackMeetings;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.Meeting;


public abstract class GetMeetings extends AsyncTask<Void,Void,List<Meeting>> implements AsyncTaskCallbackMeetings {

    private int page;
    private IMeetingAdapter meetingAdapter;

    public GetMeetings (int page) {
        setPage(page);
        meetingAdapter = CurrentSession.getInstance().getMeetingAdapter();
    }

    @Override
    protected List<Meeting> doInBackground(Void... v) {
        return meetingAdapter.getAllMeetings(page);
    }

    @Override
    protected void onPostExecute(List<Meeting> meetings) {
        onResponseReceived(meetings);
        super.onPostExecute(meetings);
    }

    private int getPage() {
        return this.page;
    }

    private void setPage(int page) {
        this.page = page;
    }

}
