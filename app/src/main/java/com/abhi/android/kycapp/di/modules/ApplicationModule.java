package com.abhi.android.kycapp.di.modules;

import android.app.Application;
import android.content.Context;


import com.abhi.android.kycapp.AppConstants;
import com.abhi.android.kycapp.R;
import com.abhi.android.kycapp.api.ApiHelper;
import com.abhi.android.kycapp.api.AppApiHelper;
import com.abhi.android.kycapp.data.DataManager;
import com.abhi.android.kycapp.data.DataManagerHelper;
import com.abhi.android.kycapp.data.pref.SharedPref;
import com.abhi.android.kycapp.data.pref.SharedPrefHelper;
import com.abhi.android.kycapp.di.ApplicationContext;
import com.abhi.android.kycapp.di.DatabaseInfo;
import com.abhi.android.kycapp.di.PreferenceInfo;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.services.vision.v1.Vision;
import com.google.gson.JsonObject;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by abhishek on 22/12/17.
 */

@Module
public class ApplicationModule {

    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    DataManagerHelper provideDataManager(DataManager manager) {
        return manager;
    }



    @Provides
    @Singleton
    SharedPrefHelper providePreferencesHelper(SharedPref sharedPref) {
        return sharedPref;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper helper) {
        return helper;
    }

    @Provides
    @Singleton
    Vision.Builder provideVisionBuilder() {
        return new Vision.Builder(
                new NetHttpTransport(),
                new AndroidJsonFactory(),
                null);
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }


    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }


    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/source-sans-pro/SourceSansPro-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }


}
