package edu.upc.fib.meetnrun.asynctasks.callbacks;


import edu.upc.fib.meetnrun.models.TrackingData;

public interface AsyncTaskCallbackTrackingData {
    void onResponseReceived(TrackingData tracking);
}
