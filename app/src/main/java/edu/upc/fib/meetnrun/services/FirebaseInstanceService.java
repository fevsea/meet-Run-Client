package edu.upc.fib.meetnrun.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import edu.upc.fib.meetnrun.adapters.ILoginAdapter;
import edu.upc.fib.meetnrun.exceptions.AuthorizationException;
import edu.upc.fib.meetnrun.exceptions.NotFoundException;
import edu.upc.fib.meetnrun.models.CurrentSession;

/**
 * Created by alejandro on 28/11/17.
 */

public class FirebaseInstanceService extends FirebaseInstanceIdService {
    private static final String TAG = FirebaseInstanceService.class.getSimpleName();


    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        ILoginAdapter userAdapter = CurrentSession.getInstance().getLoginAdapter();
        try {
            userAdapter.uppdateFirebaseToken(refreshedToken);
        } catch (AuthorizationException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        // sendRegistrationToServer(refreshedToken);
    }
}
