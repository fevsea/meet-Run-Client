package edu.upc.fib.meetnrun.remote;

import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public interface SOServices {
    @GET("/users")
    Call<User[]> getUsers();

    @GET("/meetings")
    Call<Meeting[]> getMeetings();


}
