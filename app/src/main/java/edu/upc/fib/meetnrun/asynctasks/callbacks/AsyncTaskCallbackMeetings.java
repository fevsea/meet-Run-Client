package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.Meeting;

public interface AsyncTaskCallbackMeetings {
    void onResponseReceived(List<Meeting> meetings);
}
