package com.abhi.android.kycapp.data;

import android.content.Context;

import com.abhi.android.kycapp.api.ApiHelper;
import com.abhi.android.kycapp.data.pref.SharedPrefHelper;
import com.abhi.android.kycapp.di.ApplicationContext;
import com.abhi.android.kycapp.model.vision.VisionResponse;
import com.google.gson.JsonObject;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import retrofit2.Response;

/**
 * Created by abhi on 12/24/17.
 */

@Singleton
public class DataManager implements DataManagerHelper {

    private final Context mContext;
    private final SharedPrefHelper mSharedPrefHelper;
    private final ApiHelper helper;


    @Inject
    public DataManager(@ApplicationContext Context context,
                       SharedPrefHelper mSharedPrefHelper,
                       ApiHelper helper) {
        mContext = context;
        this.mSharedPrefHelper = mSharedPrefHelper;
        this.helper = helper;
    }


    @Override
    public Observable<VisionResponse> getVisionResponse(String key, JsonObject jsonObject) {
        return helper.getVisionResponse(key, jsonObject);
    }
}
