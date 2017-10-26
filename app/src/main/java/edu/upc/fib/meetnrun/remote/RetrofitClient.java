package edu.upc.fib.meetnrun.remote;

import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
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
                    Log.w("RETROFIT",message);
                }
            });
            Interceptor retry = new Interceptor() {
                //Interceptor used to retry request when get some IOException
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    Response response = null;
                    int tryCount = 0, max = 10;
                    while (tryCount < max) {
                    try {
                        response = chain.proceed(request);
                        Log.d("RETOFITCLIENT","Response received from server");
                        tryCount = max;
                    } catch (IOException  ioe){
                        tryCount++;
                        Log.e("RETOFITCLIENT","IOException happened in the request to server...");
                        Log.e("RETOFITCLIENT","RETRYING Request...");
                    }
                    }
                    return response;
                }
            };
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().
                    connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS)
                    .addInterceptor(new AuthenticationInterceptor())
                    .addInterceptor(retry)
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
