package edu.upc.fib.meetnrun.exceptions;

import java.util.Map;

/**
 * Created by Awais Iqbal on 02/11/2017.
 */

public class AutorizationException extends GenericException {
    public static final String TAG = "AUTORIZATIONEXCEPTION";

    public AutorizationException(Map<String, String> problems) {
        super(problems);
    }
}
