package edu.upc.fib.meetnrun.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMsgService extends FirebaseMessagingService {

    private static final String TAG = FirebaseMsgService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String json = null;
        String type = null;

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            type = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + type);
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            json = remoteMessage.getData().toString();
            Log.d(TAG, "Message data payload: " + json);
        }

        consumeMessage(type, json);
    }

    public void consumeMessage(String type, String json){
        switch (type) {
            default:
                Log.w(TAG, "UNINPLEMENTED: " +type);
        }
    }
}
