package edu.upc.fib.meetnrun.asynctasks;


import java.util.List;

import edu.upc.fib.meetnrun.models.Meeting;

public interface AsyncTaskCallback {
    void onResponseReceived(List<Meeting> meetings);
}
