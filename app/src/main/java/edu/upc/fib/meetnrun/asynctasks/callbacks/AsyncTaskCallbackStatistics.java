package edu.upc.fib.meetnrun.asynctasks.callbacks;


import edu.upc.fib.meetnrun.models.Statistics;

public interface AsyncTaskCallbackStatistics {
    void onResponseReceived(Statistics s);
}
