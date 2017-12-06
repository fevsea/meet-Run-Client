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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import edu.upc.fib.meetnrun.R;
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
