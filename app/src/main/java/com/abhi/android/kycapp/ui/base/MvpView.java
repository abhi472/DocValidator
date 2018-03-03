package com.abhi.android.kycapp.ui.base;

import android.support.annotation.StringRes;


import com.google.api.services.vision.v1.Vision;

/**
 * Created by abhishek on 26/12/17.
 */

public interface MvpView {

    void showLoading();

    void hideLoading();

    void onError(@StringRes int resId, int tag);

    void onError(String message, int tag);

    void onError(String header, String message, int tag);

    void onFieldError(String message);

    void onError();

    void showMessage(String message);

    void showMessage(@StringRes int resId);

    boolean isNetworkConnected();

    void hideKeyboard();

    void onFragmentDetached(String tag);


    CharSequence getStringResource(int stringId);

    void goBack();

    Vision getVision();




}
