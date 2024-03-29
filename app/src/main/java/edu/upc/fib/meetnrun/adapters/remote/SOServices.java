package edu.upc.fib.meetnrun.adapters.remote;

import java.util.List;

import edu.upc.fib.meetnrun.adapters.models.ChallengeServer;
import edu.upc.fib.meetnrun.adapters.models.FeedMeetingServer;
import edu.upc.fib.meetnrun.adapters.models.ChatServer;
import edu.upc.fib.meetnrun.adapters.models.Forms;
import edu.upc.fib.meetnrun.adapters.models.FriendServer;
import edu.upc.fib.meetnrun.adapters.models.MeetingServer;
import edu.upc.fib.meetnrun.adapters.models.PageServer;
import edu.upc.fib.meetnrun.adapters.models.PositionServer;
import edu.upc.fib.meetnrun.adapters.models.PositionUserServer;
import edu.upc.fib.meetnrun.adapters.models.StatisticsServer;
import edu.upc.fib.meetnrun.adapters.models.TrackServer;

import edu.upc.fib.meetnrun.adapters.models.PositionUserServer;
import edu.upc.fib.meetnrun.adapters.models.TrophiesListServer;
import edu.upc.fib.meetnrun.adapters.models.UserServer;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @GET("/users/{id}/meetings")
    Call<List<MeetingServer>> getUserMeetingFilteredMeetings(@Path("id") int id, @Query("filter") String filter);

    @GET("/users/{id}/statistics")
    Call<StatisticsServer> getUserStatisticsByID(@Path("id") int id);

    @POST("/users/{id}/ban")
    Call<Void> requestBan(@Path("id") int id);

    @GET("/trophies/{id}")
    Call<TrophiesListServer> getTrophiesListByID(@Path("id") int id);
    
    @GET("/feed/{id}")
    Call<List<FeedMeetingServer>> getFeedMeeting(@Path("id") int id);



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

    @DELETE("/users/token")
    Call<Void> resetToken();


    //PARTICIPANTS

    @GET("/meetings/{id}/participants")
    Call<PageServer<UserServer>> getAllParticipantsFromMeeting(@Path("id") int id, @Query("limit") int limit, @Query("offset") int offset);

    @POST("/meetings/{meetingID}/participants/{userID}")
    Call<Void> joinMeeting(@Path("meetingID") int meetingID, @Path("userID") int userID);

    @DELETE("/meetings/{meetingID}/participants/{userID}")
    Call<Void> leaveMeeting(@Path("meetingID") int meetingID, @Path("userID") int userID);


    //FRIENDS
    @GET("/users/friends")
    Call<PageServer<FriendServer>> getCurrentUserFriends(@Query("limit") int limit, @Query("offset") int offset);

    @POST("/users/friends/{id}")
    Call<Void> addFriend(@Path("id") int id);

    @DELETE("/users/friends/{id}")
    Call<Void> removeFriend(@Path("id") int id);

    @GET("/users/{id}/friends")
    Call<PageServer<FriendServer>> getAllFriendsOfUser(@Path("id") int id, @Query("limit") int limit, @Query("offset") int offset);

    @GET("/users/friends/{id}?accepted=True")
    Call<PageServer<FriendServer>> getUserAcceptedFriends(@Path("id") int id, @Query("limit") int limit, @Query("offset") int offset);

    @GET("/users/friends/{id}?accepted=False")
    Call<PageServer<FriendServer>> getUserPendingFriends(@Path("id") int id, @Query("limit") int limit, @Query("offset") int offset);


    //TRACKING
    @GET("/meetings/{idMeeting}/tracking/{idUser}")
    Call<TrackServer> getTracking(@Path("idUser") int userID, @Path("idMeeting") int meetingID);

    @POST("/meetings/{idMeeting}/tracking/{idUser}")
    Call<Void> addTracking(@Path("idUser") int userID, @Path("idMeeting") int meetingID, @Body TrackServer ts);

    @DELETE("/meetings/{idMeeting}/tracking/{idUser}")
    Call<Void> deleteTracking(@Path("idUser") int userID, @Path("idMeeting") int meetingID);


    //CHATS
    @GET("/chats")
    Call<PageServer<ChatServer>> getChats(@Query("limit") int limit, @Query("offset") int offset);

    @POST("/chats")
    Call<ChatServer> createChat(@Body Forms.ChatCreateUpdate cs);

    @GET("/chats/{id}")
    Call<ChatServer> getChat(@Path("id") int id);

    @PUT("/chats/{id}")
    Call<Void> updateChat(@Path("id") int id, @Body Forms.ChatCreateUpdate cs);

    @DELETE("/chats/{id}")
    Call<Void> deleteChat(@Path("id") int id);

    @GET("/chats/p2p/{id}")
    Call<ChatServer> getPrivateChat(@Path("id") int id);

    //CHALLENGES
    @GET("/challenges")
    Call<List<ChallengeServer>> getAllCurrentUserChallenges();

    @POST("/challenges")
    Call<ChallengeServer> createNewChallenge(@Body Forms.ChallengeCreator cs);

    @GET("/challenges/{id}")
    Call<ChallengeServer> getChallenge(@Path("id") int id);

    @DELETE("/challenges/{id}")
    Call<Void> deleteRejectChallenge(@Path("id") int id);

    @POST("/challenges/{id}")
    Call<Void> acceptChallenge(@Path("id") int id);

    //RANKINGS
    @GET("/rankings/zone")
    Call<List<PositionServer>> getAvgKMPostalCode();

    @GET("/rankings/zip")
    Call<List<Forms.Zip>> getAllPostCodes();

    @GET("/rankings/users")
    Call<PageServer<PositionUserServer>> getUsersInUsersRanking(@Query("limit") int limit, @Query("offset") int offset);

    @GET("/rankings/zone/{zip}")
    Call<PageServer<PositionUserServer>> getUsersInUsersRankingByPostCode(@Path("zip") String zip, @Query("limit") int limit, @Query("offset") int offset);

}
