package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.PositionUser;

public interface AsyncTaskCallbackRankingUser {
    void onResponseReceived(List<PositionUser> rankings);
}
