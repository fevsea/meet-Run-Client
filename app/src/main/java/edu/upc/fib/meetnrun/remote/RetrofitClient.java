package edu.upc.fib.meetnrun.remote;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import edu.upc.fib.meetnrun.models.CurrentSession;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("RETROFIT",message);
                }
            });
            Interceptor retry = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    // try the request
                    Response response = chain.proceed(request);
                    int tryCount = 0;
                    while (!response.isSuccessful() && tryCount < 4) {
                        Log.d("intercept", "Request is not successful - " + tryCount);
                        tryCount++;
                        // retry the request
                        response = chain.proceed(request);
                    }
                    // otherwise just pass the original response on
                    return response;
                }
            };
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().
                    connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS)
                    .addInterceptor(new AuthenticationInterceptor())
                    .retryOnConnectionFailure(true)
                    .addInterceptor(logging)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
