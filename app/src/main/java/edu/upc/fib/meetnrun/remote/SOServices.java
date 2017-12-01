package edu.upc.fib.meetnrun.remote;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.MeetingServer;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.adapters.models.TrackServer;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public interface SOServices {

    int PAGELIMIT = 10;

    //MEETINGS
    @GET("/meetings")
    Call<PageServer<MeetingServer>> getAllMeetings(@Query("limit") int limit, @Query("offset") int offset);

    @POST("/meetings")
    Call<MeetingServer> createMeeting(@Body Forms.CreateMeeting meeting);

    @GET("/meetings/{id}")
    Call<MeetingServer> getMeeting(@Path("id") int id);

    @PATCH("/meetings/{id}")
    Call<Void> updateMeeting(@Path("id") int id, @Body MeetingServer meeting);

    @DELETE("/meetings/{id}")
    Call<Void> deletetMeeting(@Path("id") int id);

    @GET("/meetings")
    Call<PageServer<MeetingServer>> getAllMeetingsFiltered(@Query("limit") int limit, @Query("offset") int offset, @Query("search") String query);

    //USERS

    @GET("/users")
    Call<PageServer<UserServer>> getAllUsers(@Query("limit") int limit, @Query("offset") int offset);

    @POST("/users")
    Call<UserServer> registerUser(@Body Forms.UserRegistration user);

    @GET("/users/{id}")
    Call<UserServer> getUser(@Path("id") int id);

    @PATCH("/users/{id}")
    Call<Void> updateUser(@Path("id") int id, @Body UserServer userServer);

    @DELETE("/users/{id}")
    Call<Void> deleteUser(@Path("id") int id);

    @POST("/users/changePassword")
    Call<Void> changePassword(@Body Forms.ChangePassword sp);


    //LOGIN

    @POST("/users/login")
    Call<Forms.Token> logIn(@Body Forms.LoginUser lu);

    @GET("/users/logout")
    Call<Void> logout();

    @GET("/users/current")
    Call<UserServer> getCurrentUser();

    @GET("/users/token")
    Call<Forms.Token> getFibaseToken();

    @POST("/users/token")
    Call<Void> updateFirebaseToken(@Body Forms.Token token);



    //PARTICIPANTS

    @GET("/meetings/{id}/participants")
    Call<PageServer<UserServer>> getAllParticipantsFromMeeting(@Path("id") int id, @Query("limit") int limit, @Query("offset") int offset);

    @POST("/meetings/{id}/participants")
    Call<Void> joinMeeting(@Path("id") int id);

    @DELETE("/meetings/{id}/participants")
    Call<Void> leaveMeeting(@Path("id") int id);

    @GET("/users/{id}/meetings")
    Call<List<MeetingServer>> getAllFutureMeetings(@Path("id") int id);


    //FRIENDS
    @GET("/users/friends")
    Call<PageServer<UserServer>> getCurrentUserFriends(@Query("limit") int limit, @Query("offset") int offset);

    @POST("/users/friends/{id}")
    Call<Void> addFriend(@Path("id") int id);

    @DELETE("/users/friends/{id}")
    Call<Void> removeFriend(@Path("id") int id);

    @GET("/users/{id}/friends")
    Call<PageServer<UserServer>> getAllFriendsOfUser(@Path("id") int id, @Query("limit") int limit, @Query("offset") int offset);

    //TRACKING
    @GET("/meetings/{idMeeting}/tracking/{idUser}")
    Call<TrackServer> getTracking(@Path("idUser") int userID, @Path("idMeeting") int meetingID);

    @POST("/meetings/{idMeeting}/tracking/{idUser}")
    Call<Void> addTracking(@Path("idUser") int userID, @Path("idMeeting") int meetingID, @Body TrackServer ts);

    @DELETE("/meetings/{idMeeting}/tracking/{idUser}")
    Call<Void> deleteTracking(@Path("idUser") int userID, @Path("idMeeting") int meetingID);

}
