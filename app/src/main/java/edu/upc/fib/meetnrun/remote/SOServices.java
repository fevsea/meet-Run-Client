package edu.upc.fib.meetnrun.remote;

import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public interface SOServices {
    @GET("/users")
    Call<User[]> getUsers();

    @GET("/meetings")
    Call<Meeting[]> getMeetings();

    @POST("meetings")
    Call<Void> createMeeting(@Body Meeting meeting);

    @GET("/meetings/{id}")
    Call<Meeting> getMeeting(@Path("id") String id);

    @DELETE("/meetings/{id}")
    Call<Void> deletetMeeting(@Path("id") String id, @Body Meeting meeting);

    @PATCH("/meetings/{id}")
    Call<Meeting> updateMeeting(@Path("id") String id, @Body Meeting meeting);

    @POST("/login")
    Call<String> logIn(@Field("username") String username, @Field("password") String password);


}
