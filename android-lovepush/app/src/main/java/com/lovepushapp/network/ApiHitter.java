package com.lovepushapp.network;


import com.lovepushapp.core.utils.API_GLOBALS;
import com.lovepushapp.interfaces.RestApiInterface;

import java.io.IOException;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import okhttp3.Dispatcher;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiHitter {


    public Retrofit RestConfigWithOutAuth() {
        return new Retrofit.Builder()
                .baseUrl(API_GLOBALS.BASE_URL)
                .client(provideClient(false))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RestApiInterface ApiHitter() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_GLOBALS.BASE_URL)
                .client(provideClient(false))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(RestApiInterface.class);
    }


    public Retrofit RestConfigWithAuth() {
        return new Retrofit.Builder()
                .baseUrl(API_GLOBALS.BASE_URL)
                .client(provideClient(true))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static RestApiInterface getInstaToken() {
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("https://api.instagram.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(RestApiInterface.class);

    }



    static OkHttpClient provideClient(final boolean isAuthorized) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.MINUTES);
        builder.readTimeout(15, TimeUnit.MINUTES);
        builder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                HttpUrl url = request.url()
                        .newBuilder()
                        .build();
                TimeZone tz = TimeZone.getDefault();
                if (isAuthorized) {
                    //for post login api's
                    request = request.newBuilder()
                            /* .addHeader(GlobalsVariables.URL.deviceType, "A")
                             .addHeader(GlobalsVariables.URL.timezone, tz.getID())
                             .addHeader(GlobalsVariables.URL.language, (String) new SharedStorage().getSharedData(user_language, TYPE_STRING))
                             .addHeader(GlobalsVariables.URL.deviceToken, (String) new SharedStorage().getSharedData(GlobalsVariables.STORAGE.TOKEN_REFRESH, GlobalsVariables.STORAGE.TYPE_STRING))
                        */.url(url)
                            .build();
                } else {
                    request = request.newBuilder()
                            /* .addHeader(GlobalsVariables.URL.deviceType, "A")
                             .addHeader(GlobalsVariables.URL.timezone, tz.getID())
                             .addHeader(GlobalsVariables.URL.language, (String) new SharedStorage().getSharedData(user_language, TYPE_STRING))
                             .addHeader(GlobalsVariables.URL.deviceToken, (String) new SharedStorage().getSharedData(GlobalsVariables.STORAGE.TOKEN_REFRESH, GlobalsVariables.STORAGE.TYPE_STRING))
                         */.url(url)
                            .build();
                }
                return chain.proceed(request);
            }
        });

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(logging);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(5);
        builder.dispatcher(dispatcher);
        return builder.build();
    }


}