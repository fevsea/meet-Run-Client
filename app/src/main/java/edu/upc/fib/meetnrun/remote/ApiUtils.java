package edu.upc.fib.meetnrun.remote;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public class ApiUtils {

    public static final String BASE_URL = "http://10.4.41.144:8000/";

    public static MeetingService getMeetingService() {
        return RetrofitClient.getClient(BASE_URL).create(MeetingService.class);
    }

    public static SOServices getUserService() {
        return RetrofitClient.getClient(BASE_URL).create(SOServices.class);
    }
}
