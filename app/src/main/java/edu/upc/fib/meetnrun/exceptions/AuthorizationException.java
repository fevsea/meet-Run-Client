package edu.upc.fib.meetnrun.exceptions;

import java.util.Map;

/**
 * Created by Awais Iqbal on 02/11/2017.
 */

public class AuthorizationException extends GenericException {
    public static final String TAG = "AUTORIZATIONEXCEPTION";

    public AuthorizationException(Map<String, String> problems) {
        super(problems);
    }
}
