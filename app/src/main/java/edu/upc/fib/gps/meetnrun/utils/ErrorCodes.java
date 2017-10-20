package edu.upc.fib.gps.meetnrun.utils;

/**
 * Created by Awais Iqbal on 20/10/2017.
 */

public final class ErrorCodes {
    public static final int OK = 200;
    public static final int GENERAL_ERROR = -1;


    public static final int REGISTER_USERNAME_INCORRECT = 1;
    public static final int REGISTER_EMAIL_ALREADY_USED = 2;
    public static final int REGISTER_PASSWORD_FORMAT_ERROR = 3;
    public static final int REGISTER_INVALID_POSTCODE = 4;


    public static final int LOGIN_USERNAME_INCORRECT = 5;

    public static final int CREATEMEETING_PAST_DATETIME= 6;
    //TODO follow the same protocol for all the action and possible errors

}
