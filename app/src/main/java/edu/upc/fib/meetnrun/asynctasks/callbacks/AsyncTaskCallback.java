package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.Meeting;

public interface AsyncTaskCallback {
    void onResponseReceived();

    void onExceptionReceived();
}
