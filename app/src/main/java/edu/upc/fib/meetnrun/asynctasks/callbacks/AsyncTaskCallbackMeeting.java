package edu.upc.fib.meetnrun.asynctasks.callbacks;


import edu.upc.fib.meetnrun.models.Meeting;

public interface AsyncTaskCallbackMeeting {
    void onResponseReceived(Meeting meeting);
}
