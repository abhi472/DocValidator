package com.abhi.android.kycapp.ui.dialogs;

import android.text.TextUtils;

import com.abhi.android.kycapp.data.DataManagerHelper;
import com.abhi.android.kycapp.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by abhishek on 3/3/18.
 */

public class PhoneNumberDialogPresenter<V extends PhoneNumberDialogMvpView>
        extends BasePresenter<V>
        implements PhoneNumberDialogMvpPresenter<V> {

    @Inject
    public PhoneNumberDialogPresenter(DataManagerHelper dataManager,
                                      CompositeDisposable mCompositeDisposable) {
        super(dataManager, mCompositeDisposable);
    }

    @Override
    public void onNextClick(String s) {
        if(TextUtils.isEmpty(s)) {
            getMvpView().onFieldError("Invalid");
        } else {
            getMvpView().setNumber(s);
        }
    }
}
