package edu.upc.fib.meetnrun.remote;

import edu.upc.fib.meetnrun.models.Meeting;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.persistence.persistenceModels.Forms;
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

    //MEETINGS
    @GET("/meetings")
    Call<Meeting[]> getMeetings();

    @POST("/meetings/")
    Call<Meeting> createMeeting(@Body Meeting meeting);

    @GET("/meetings/{id}")
    Call<Meeting> getMeeting(@Path("id") int id);

    @PATCH("/meetings/{id}/")
    Call<Void> updateMeeting(@Path("id") int id, @Body Meeting meeting);

    @DELETE("/meetings/{id}/")
    Call<Void> deletetMeeting(@Path("id") int id);

    //USERS

    @GET("/users")
    Call<User[]> getUsers();

    @POST("/users/")
    Call<User> registerUser(@Body Forms.UserRegistration user);

    @GET("/users/{id}")
    Call<User> getUser(@Path("id") int id);

    @PATCH("/users/{id}/")
    Call<Void> updateUser(@Path("id") int id, @Body User user);

    @DELETE("/users/{id}/")
    Call<Void> deleteUser(@Path("id") int id);


    //LOGIN

    @POST("/users/login")
    Call<Forms.Token> logIn(@Body Forms.LoginUser lu);

    @GET("/users/logout")
    Call<Void> logout();

    @GET("/users/current/")
    Call<User> getCurrentUser();


}
