package com.abhi.android.kycapp.ui.dialogs;

import com.abhi.android.kycapp.ui.base.MvpPresenter;

/**
 * Created by abhishek on 3/3/18.
 */

public interface PhoneNumberDialogMvpPresenter<V extends PhoneNumberDialogMvpView>
        extends MvpPresenter<V> {
    void onNextClick(String s);
}
