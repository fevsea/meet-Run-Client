package edu.upc.fib.meetnrun.asynctasks.callbacks;


import java.util.List;

import edu.upc.fib.meetnrun.models.Trophie;

public interface AsyncTaskCallbackTrophies {
    void onResponseReceived(List<Trophie> trophies);
}