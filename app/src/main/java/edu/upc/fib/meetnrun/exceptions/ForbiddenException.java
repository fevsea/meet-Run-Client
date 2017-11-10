package edu.upc.fib.meetnrun.exceptions;

import java.util.Map;

/**
 * Created by Awais Iqbal on 02/11/2017.
 */

public class ForbiddenException extends GenericException {
    public static final String TAG = "FORBIDDENEXCEPTION";

    public ForbiddenException(Map<String, String> problems) {
        super(problems);
    }
}
