package com.abhi.android.kycapp.di.modules;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.abhi.android.kycapp.di.ActivityContext;
import com.abhi.android.kycapp.di.PerActivity;
import com.abhi.android.kycapp.ui.dialogs.PhoneNumberDialogMvpPresenter;
import com.abhi.android.kycapp.ui.dialogs.PhoneNumberDialogMvpView;
import com.abhi.android.kycapp.ui.dialogs.PhoneNumberDialogPresenter;
import com.abhi.android.kycapp.ui.error.ErrorDialogMvpPresenter;
import com.abhi.android.kycapp.ui.error.ErrorDialogMvpView;
import com.abhi.android.kycapp.ui.error.ErrorDialogPresenter;
import com.abhi.android.kycapp.ui.kyc_activity.KycMvpPresenter;
import com.abhi.android.kycapp.ui.kyc_activity.KycMvpView;
import com.abhi.android.kycapp.ui.kyc_activity.KycPresenter;
import com.abhi.android.kycapp.utils.TfcProgressDialog;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by abhishek on 22/12/17.
 */

@Module
public class ActivityModule {

    private AppCompatActivity mActivity;

    public ActivityModule(AppCompatActivity appCompatActivity) {
        mActivity = appCompatActivity;
    }

    @Provides
    @ActivityContext
    Context provideContext() {
        return mActivity;
    }

    @Provides
    AppCompatActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @PerActivity
    KycMvpPresenter<KycMvpView> provideKycPresenter(
            KycPresenter<KycMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    PhoneNumberDialogMvpPresenter<PhoneNumberDialogMvpView> providePhoneNumberDialogPresenter(
            PhoneNumberDialogPresenter<PhoneNumberDialogMvpView> presenter) {
        return presenter;
    }

    @Provides
    @PerActivity
    ErrorDialogMvpPresenter<ErrorDialogMvpView> provideErrorDialogPresenter(
            ErrorDialogPresenter<ErrorDialogMvpView> presenter) {
        return presenter;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    TfcProgressDialog provideDialog() {
        return new TfcProgressDialog();
    }



}
