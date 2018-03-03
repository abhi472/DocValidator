package com.abhi.android.kycapp.ui.kyc_activity;

import com.abhi.android.kycapp.ui.base.MvpView;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;

/**
 * Created by abhishek on 8/1/18.
 */

public interface KycMvpView extends MvpView {

    CognitoCachingCredentialsProvider getCredentialProvider();

    TransferUtility getTransferUtility(AmazonS3Client s3Client);


    void enableButton();

    void showSuccess();

    void showPhoneDialog();
}
