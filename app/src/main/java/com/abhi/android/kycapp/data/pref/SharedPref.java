package com.abhi.android.kycapp.data.pref;

import android.content.Context;
import android.content.SharedPreferences;


import com.abhi.android.kycapp.di.ApplicationContext;
import com.abhi.android.kycapp.di.PreferenceInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by abhi on 12/24/17.
 */

@Singleton
public class SharedPref implements SharedPrefHelper  {

    private static final String PREF_KEY_DEVICE_TOKEN = "PREF_KEY_DEVICE_TOKEN";
    private static final String PREF_KEY_PHONE_NUMBER = "PREF_KEY_PHONE_NUMBER";
    private static final String PREF_KEY_COUNTRY_CODE = "PREF_KEY_COUNTRY_CODE";
    private final SharedPreferences pref;


    @Inject
    public SharedPref(@ApplicationContext Context context,
                      @PreferenceInfo String prefFileName) {
        pref = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }



}
