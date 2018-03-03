package com.abhi.android.kycapp.di.components;

import android.app.Application;
import android.content.Context;

import com.abhi.android.kycapp.KYCApp;
import com.abhi.android.kycapp.data.DataManagerHelper;
import com.abhi.android.kycapp.di.ApplicationContext;
import com.abhi.android.kycapp.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by abhishek on 22/12/17.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(KYCApp app);


    @ApplicationContext
    Context context();

    Application application();

    DataManagerHelper getDataManager();



}
