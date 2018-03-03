package com.abhi.android.kycapp;

import android.app.Application;

import com.abhi.android.kycapp.data.DataManagerHelper;
import com.abhi.android.kycapp.di.components.ApplicationComponent;
import com.abhi.android.kycapp.di.components.DaggerApplicationComponent;
import com.abhi.android.kycapp.di.modules.ApplicationModule;
import com.facebook.stetho.Stetho;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by abhishek on 3/3/18.
 */

public class KYCApp extends Application {

    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @Inject
    Vision.Builder visionBuilder;

    @Inject
    DataManagerHelper mDataManager;

    private Vision vision;


    private ApplicationComponent mApplicationComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);



        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }


        CalligraphyConfig.initDefault(mCalligraphyConfig);

        visionBuilder.setVisionRequestInitializer(
                new VisionRequestInitializer("AIzaSyB5Zq1CDnrqZYf1EJ_fu9ldxWiiljlqAlU")
        );
        vision = visionBuilder.build();
    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }



    public Vision getVision() {
        return vision;
    }
}
