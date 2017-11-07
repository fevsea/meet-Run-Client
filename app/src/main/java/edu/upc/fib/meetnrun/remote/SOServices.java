package edu.upc.fib.meetnrun.remote;

import java.util.List;

import edu.upc.fib.meetnrun.persistence.persistenceModels.Forms;
import edu.upc.fib.meetnrun.persistence.persistenceModels.MeetingServer;
import edu.upc.fib.meetnrun.persistence.persistenceModels.UserServer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public interface SOServices {

    //MEETINGS
    @GET("/meetings")
    Call<MeetingServer[]> getMeetings();

    @POST("/meetings")
    Call<MeetingServer> createMeeting(@Body Forms.CreateMeeting meeting);

    @GET("/meetings/{id}")
    Call<MeetingServer> getMeeting(@Path("id") int id);

    @PATCH("/meetings/{id}")
    Call<Void> updateMeeting(@Path("id") int id, @Body MeetingServer meeting);

    @DELETE("/meetings/{id}")
    Call<Void> deletetMeeting(@Path("id") int id);

    //USERS

    @GET("/users")
    Call<UserServer[]> getUsers();

    @POST("/users")
    Call<UserServer> registerUser(@Body Forms.UserRegistration user);

    @GET("/users/{id}")
    Call<UserServer> getUser(@Path("id") int id);

    @PATCH("/users/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body UserServer userServer);

    @DELETE("/users/{id}")
    Call<Void> deleteUser(@Path("id") int id);


    //LOGIN

    @POST("/users/login")
    Call<Forms.Token> logIn(@Body Forms.LoginUser lu);

    @GET("/users/logout")
    Call<Void> logout();

    @GET("/users/current")
    Call<UserServer> getCurrentUser();

    //PARTICIPANTS

    @GET("/meetings/{id}/participants")
    Call<List<UserServer>> getAllParticipantsFromMeeting(@Path("id") int id);

    @POST("/meetings/{id}/participants")
    Call<Void> joinMeeting(@Path("id") int id);

    @DELETE("/meetings/{id}/participants")
    Call<Void> leaveMeeting(@Path("id") int id);

    @GET("/users/{id}/meetings")
    Call<List<MeetingServer>> getAllFutureMeetings(@Path("id") int id);


    //FRIENDS
    @GET("/users/friends")
    Call<List<UserServer>> getCurrentUserFriends();

    @POST("/users/friends/{id}")
    Call<Void> addFriend(@Path("id") int id);

    @DELETE("/users/friends/{id}")
    Call<Void> removeFriend(@Path("id") int id);

    @GET("/users/{id}/friends")
    Call<List<UserServer>> getFriendsOfUser(@Path("id") int id);


}
