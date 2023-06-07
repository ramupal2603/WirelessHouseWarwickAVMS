package com.adverticoLTD.avms.keyLogSolution.network;



import com.adverticoLTD.avms.keyLogSolution.network.utils.NetworkConfig;
import com.adverticoLTD.avms.network.utils.WebApiHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetrofitClient {

    public static Retrofit retrofit;

    public void RetrofitClient() {

    }

    public static Retrofit getRetrofit() {

        if (retrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.connectTimeout(NetworkConfig.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
            builder.writeTimeout(NetworkConfig.WRITE_TIME_OUT, TimeUnit.SECONDS);
            builder.readTimeout(NetworkConfig.READ_TIME_OUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(NetworkConfig.RETRY);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);

            OkHttpClient okHttpClient = builder.build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(WebApiHelper.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();

        }

        return retrofit;
    }
}
