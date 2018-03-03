package com.abhi.android.kycapp.ui.kyc_activity;


import com.abhi.android.kycapp.ui.base.MvpPresenter;

import java.io.File;

/**
 * Created by abhishek on 8/1/18.
 */

public interface KycMvpPresenter<V extends KycMvpView> extends MvpPresenter<V> {
    void startDetection(File absolutePath);

    void startUploadKyc(String s);

}
