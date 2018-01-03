package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.Challenge;

public interface AsyncTaskCallbackChallenges {
    void onResponseReceived(List<Challenge> challenges);
}
