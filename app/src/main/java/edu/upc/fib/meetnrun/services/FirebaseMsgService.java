package edu.upc.fib.meetnrun.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import edu.upc.fib.meetnrun.R;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.Challenge;
import edu.upc.fib.meetnrun.models.CurrentSession;
import edu.upc.fib.meetnrun.models.User;
import edu.upc.fib.meetnrun.views.LoginActivity;

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = FirebaseMsgService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = null;
        String type = null;

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

/*
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            type = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + type);
        }
*/

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            data = remoteMessage.getData();
            Log.d(TAG, "Message data payload: " + data.toString());
            try {
                type = data.get("type");
            }catch (Exception e){
                e.printStackTrace();
                return;
            }
            consumeMessage(type, data);
        } else {
            Log.w(TAG, "Message withoth payload");
        }

    }

    public void consumeMessage(String type, Map<String, String> data){
        switch (type) {
            case "notification":
                issueNotification(data.get("title"), data.get("text"));
                break;
            case "new_challenge":
                String titleNewChallenge = getString(R.string.new_challenge);
                String textNewChallenge = String.format(getString(R.string.new_challenge_text), data.get("challenger_username"));
                issueNotification(titleNewChallenge, textNewChallenge);
                break;
            case "challenge_accepted":
                String titleAccepted = getString(R.string.challenge_accepted);
                String textAccepted = String.format(getString(R.string.challenge_accepted_text), data.get("challenged_username"));
                issueNotification(titleAccepted, textAccepted);
                break;
            case "challenge_won":
                String titleWon = getString(R.string.challenge_won);
                String textWon;
                try {
                    Challenge ch = CurrentSession.getInstance().getChallengeAdapter().getChallenge(Integer.valueOf(data.get("challenge_id")));
                    int currentUserId = CurrentSession.getInstance().getCurrentUser().getId();
                    User opponent = (ch.getChallenged().getId()==currentUserId)?ch.getCreator():ch.getChallenged();
                    textWon = String.format(getString(R.string.challenge_expired_text), opponent.getUsername());
                }
                catch (AuthorizationException | NotFoundException e) {
                    textWon = "";
                }
                issueNotification(titleWon, textWon);
                break;
            case "challenge_lost":
                String titleLost = getString(R.string.challenge_lost);
                String textLost;
                try {
                    User u = CurrentSession.getInstance().getUserAdapter().getUser(Integer.valueOf(data.get("winner_id")));
                    textLost = String.format(getString(R.string.challenge_lost_text), u.getUsername());
                }
                catch (NotFoundException e) {
                    textLost = "";
                }
                issueNotification(titleLost, textLost);
                break;
            case "challenge_finalized":
                String titleFinalized = getString(R.string.challenge_expired);
                String textFinalized;
                try {
                    Challenge ch = CurrentSession.getInstance().getChallengeAdapter().getChallenge(Integer.valueOf(data.get("challenge_id")));
                    int currentUserId = CurrentSession.getInstance().getCurrentUser().getId();
                    User opponent = (ch.getChallenged().getId()==currentUserId)?ch.getCreator():ch.getChallenged();
                    textFinalized = String.format(getString(R.string.challenge_expired_text), opponent.getUsername());
                }
                catch (AuthorizationException | NotFoundException e) {
                    textFinalized = "";
                }
                issueNotification(titleFinalized, textFinalized);
                break;
            default:
                Log.w(TAG, "UNINPLEMENTED: " + type);
                issueNotification("UNINPLEMENTED", type);
        }
    }

    public void issueNotification(String title, String text) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = "12";
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }
}
