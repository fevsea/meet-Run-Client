package edu.upc.fib.meetnrun.exceptions;

import java.util.Map;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public class NotFoundException extends GenericException {
    public static final String TAG = "NOTFOUNDEXCEPTION";

        public NotFoundException(Map<String,String> problems) {
            super(problems);
        }
}
