package com.abhi.android.kycapp.api;

import com.abhi.android.kycapp.model.vision.VisionResponse;
import com.google.gson.JsonObject;


import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;


/**
 * Created by abhishek on 27/12/17.
 */

@Singleton
public class AppApiHelper implements ApiHelper {

    private NetworkModule mModule;

    @Inject
    public AppApiHelper(NetworkModule mModule) {
        this.mModule = mModule;
    }


    @Override
    public Observable<VisionResponse> getVisionResponse(String key,
                                                        JsonObject jsonObject) {
        return mModule.provideCVisionNeworkService().getVisionResponse(key, jsonObject);
    }
}
