package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.Position;

public interface AsyncTaskCallbackRankingZip {
    void onResponseReceived(List<Position> rankings);
}
