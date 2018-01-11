package edu.upc.fib.meetnrun.asynctasks.callbacks;


import edu.upc.fib.meetnrun.exceptions.GenericException;

public interface AsyncTaskException {
    void onExceptionReceived(GenericException e);
}
