package edu.upc.fib.meetnrun.adapters.utils;

import edu.upc.fib.meetnrun.exceptions.GenericException;
import edu.upc.fib.meetnrun.utils.UtilsGlobal;

/**
 * Created by Awais Iqbal on 07/11/2017.
 */

public class UtilsAdapter {

    public static void checkErrorCodeAndThowException(int code, String string) throws GenericException {
        switch (code) {
            case 400:
                throw UtilsGlobal.CreateParamExceptionFromJson(string);
            case 401:
                throw UtilsGlobal.CreateAutorizationExceptionFromJson(string);
            case 403:
                throw UtilsGlobal.CreateForbiddenExceptionFromJson();
            case 404:
                throw UtilsGlobal.CreateNotFoundExceptionFromJson(string);
        }
    }

    public static int calculateOffset(int limit,int page){
        return limit*page;
    }

}
