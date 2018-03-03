package com.abhi.android.kycapp.api;


import com.abhi.android.kycapp.BuildConfig;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by abhishek on 27/12/17.
 */

@Singleton
public class NetworkModule {

    @Inject
    public NetworkModule() {
    }

    Retrofit provideCall() {

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();

        return new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
               // .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .build();
    }

    Retrofit priovideVisionCall() {

//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .addNetworkInterceptor(new StethoInterceptor())
//                .build();

        return new Retrofit.Builder()
                .baseUrl("https://vision.googleapis.com/")
             //   .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .build();
    }



    @SuppressWarnings("unused")
    public RetrofitApiNodes providesNetworkService() {
        return provideCall().create(RetrofitApiNodes.class);
    }

    public RetrofitApiNodes provideCVisionNeworkService() {
        return priovideVisionCall().create(RetrofitApiNodes.class);
    }



}
