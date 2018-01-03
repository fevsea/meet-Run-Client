package edu.upc.fib.meetnrun.asynctasks.callbacks;


import edu.upc.fib.meetnrun.models.Challenge;

public interface AsyncTaskCallbackChallenge {
    void onResponseReceived(Challenge challenge);
}
