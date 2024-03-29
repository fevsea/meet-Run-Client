package edu.upc.fib.meetnrun.adapters.remote;

import android.util.Log;

import java.io.IOException;

import edu.upc.fib.meetnrun.models.CurrentSession;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Awais Iqbal on 25/10/2017.
 */

class AuthenticationInterceptor implements Interceptor {

    public AuthenticationInterceptor() {
    }



    @Override
    public Response intercept(Chain chain) throws IOException {

        Request original = chain.request();
        String token = CurrentSession.getInstance().getToken();
        Response r = null;

        if (token != null && !token.equals("")){
            Request.Builder builder = original.newBuilder();
            builder = builder.header("Authorization","Token " + CurrentSession.getInstance().getToken());
            builder = builder.header("UserServer-Agent","Android");
            Request request = builder.build();
            r = chain.proceed(request);
        } else {
            r = chain.proceed(original);
        }
        Log.d("AUTHENTICATION","TOKEN USED: "+token);
        return r;

    }
}
