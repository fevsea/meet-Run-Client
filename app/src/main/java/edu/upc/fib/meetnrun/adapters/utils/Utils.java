package edu.upc.fib.meetnrun.adapters.utils;

import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.utils.JsonUtils;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class Utils {

    public static void checkErrorCodeAndThowException(int code, String string) throws GenericException {
            switch (code) {
                case 400:
                    throw JsonUtils.CreateParamExceptionFromJson(string);
                case 401:
                    throw JsonUtils.CreateAutorizationExceptionFromJson(string);
                case 404:
                    throw JsonUtils.CreateNotFoundExceptionFromJson(string);
            }
        }
}
