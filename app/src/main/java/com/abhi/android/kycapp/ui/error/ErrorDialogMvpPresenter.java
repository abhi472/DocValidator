package com.abhi.android.kycapp.ui.error;


import com.abhi.android.kycapp.ui.base.DialogMvpView;
import com.abhi.android.kycapp.ui.base.MvpPresenter;

/**
 * Created by abhishek on 1/2/18.
 */

public interface ErrorDialogMvpPresenter<V extends DialogMvpView>
        extends MvpPresenter<V> {
    void clearSharedPref();
}
