package com.zeymur.youtubeclient;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.FormatterClosedException;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.CookieJar;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class ServiceGenerator {
    private OkHttpClient.Builder httpClientBuilder;
    private Retrofit.Builder retrofitbuilder;
    private Retrofit retrofit;

    public ServiceGenerator(Context context, String urlApi, Gson gson) {
        this.httpClientBuilder = new OkHttpClient.Builder()
                .cache(new Cache(context.getCacheDir(), 5242880)); // 5 * 1024 * 1024 = 5MB

        this.retrofitbuilder = new Retrofit.Builder()
                .baseUrl(urlApi);

        if (gson != null) this.retrofitbuilder.addConverterFactory(GsonConverterFactory.create(gson));
        else retrofitbuilder.addConverterFactory(GsonConverterFactory.create());

    }

    public ServiceGenerator(Context context, String urlApi, Gson gson, CookieJar cookieJar) {
        this.httpClientBuilder = new OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .cache(new Cache(context.getCacheDir(), 5242880)); // 5 * 1024 * 1024 = 5MB

        this.retrofitbuilder = new Retrofit.Builder()
                .baseUrl(urlApi);

        if (gson != null) this.retrofitbuilder.addConverterFactory(GsonConverterFactory.create(gson));
        else retrofitbuilder.addConverterFactory(GsonConverterFactory.create());

    }

    public ServiceGenerator(Context context, String urlApi, Gson gson, String consumerKey, String consumerSecret) {
        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(consumerKey, consumerSecret);

        this.httpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .cache(new Cache(context.getCacheDir(), 5242880)); // 5 * 1024 * 1024 = 5MB

        this.retrofitbuilder = new Retrofit.Builder()
                .baseUrl(urlApi);

        if (gson != null) this.retrofitbuilder.addConverterFactory(GsonConverterFactory.create(gson));
        else retrofitbuilder.addConverterFactory(GsonConverterFactory.create());

    }


    public <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, null);
    }

    public <S> S createService(Class<S> serviceClass, String userName, String password) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            String authToken = Credentials.basic(userName, password);
            return createService(serviceClass, authToken);
        }

        return createService(serviceClass, null, null);
    }

    public <S> S createService(Class<S> serviceClass, final String authToken) {
        if (!TextUtils.isEmpty(authToken)) {
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(authToken);

            if (!httpClientBuilder.interceptors().contains(interceptor))
                httpClientBuilder.addInterceptor(interceptor);
        }

        retrofitbuilder.client(httpClientBuilder.build());
        retrofit = retrofitbuilder.build();

        return retrofit.create(serviceClass);
    }

}
