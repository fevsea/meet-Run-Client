package edu.upc.fib.meetnrun.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Awais Iqbal on 24/10/2017.
 */

public class RetrofitClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient(String baseUrl) {
        if (retrofit==null) {
            OkHttpClient client = new OkHttpClient.Builder().
                    connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100,TimeUnit.SECONDS)
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
