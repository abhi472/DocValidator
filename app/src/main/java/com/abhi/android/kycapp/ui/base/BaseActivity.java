package com.abhi.android.kycapp.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhi.android.kycapp.KYCApp;
import com.abhi.android.kycapp.R;
import com.abhi.android.kycapp.di.components.ActivityComponent;
import com.abhi.android.kycapp.di.components.DaggerActivityComponent;
import com.abhi.android.kycapp.di.modules.ActivityModule;
import com.abhi.android.kycapp.ui.error.ErrorDialog;
import com.abhi.android.kycapp.utils.BaseUtils;
import com.abhi.android.kycapp.utils.TfcProgressDialog;
import com.bumptech.glide.Glide;

import com.google.api.services.vision.v1.Vision;
import com.google.gson.JsonObject;

import java.util.concurrent.Callable;

import javax.inject.Inject;

import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseActivity
        extends AppCompatActivity
        implements MvpView {

    @Inject
    TfcProgressDialog mProgressDialog;

    private ActivityComponent mActivityComponent;
    private KYCApp kycApp;

    private Unbinder mUnBinder;

    public static final String TAG = "BaseActivity";
    private Disposable single;
    public int dismissTag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(((KYCApp) getApplication()).getComponent())
                .build();

        kycApp = (KYCApp) getApplication();


    }


    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        if (single != null)
            single.dispose();

        super.onDestroy();
    }


    @Override
    public void showLoading() {
        mProgressDialog.showDailyPopup(this);

    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismissPopup();
        }
    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    public void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView
                .findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }


    public void onError(int resId, int tag) {

            ErrorDialog.newInstance(getString(resId), tag).show(getSupportFragmentManager());

    }

    @Override
    public void onError(String message, int tag) {
        ErrorDialog.newInstance(message, tag).show(getSupportFragmentManager());
    }

    @Override
    public void onError(String header, String message, int tag) {

        ErrorDialog.newInstance(header,
                message,
                tag).show(getSupportFragmentManager());
    }

    @Override
    public void onError() {

        ErrorDialog.newInstance().show(getSupportFragmentManager());

    }

    @Override
    public void onFieldError(String message) {

    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showMessage(int resId) {

    }

    @Override
    public boolean isNetworkConnected() {
        return BaseUtils.isNetworkAvailable(this);
    }

    @Override
    public void hideKeyboard() {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    protected abstract void setUp();

       @Override
    public CharSequence getStringResource(int stringId) {
        return getString(stringId);
    }

    @Override
    public void goBack() {
        super.onBackPressed();
    }

    public void OnErrorDialogDismiss(int tag) {
    }





    @Override
    public Vision getVision() {
        return kycApp.getVision();
    }



}
