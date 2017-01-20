package com.tiagoespinha.popmovee.services;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by tiago on 19/01/2017.
 */

@Module
public class TMDBServiceModule {
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(Application application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Singleton
    static TMDBService provideTMDBService() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        clientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url().newBuilder().addQueryParameter("api_key","").build();
                request = request.newBuilder().url(url).build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/")
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(TMDBService.class);
    }
}
