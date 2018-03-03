package com.abhi.android.kycapp.ui.error;


import com.abhi.android.kycapp.data.DataManagerHelper;
import com.abhi.android.kycapp.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by abhishek on 1/2/18.
 */

public class ErrorDialogPresenter<V extends ErrorDialogMvpView>
        extends BasePresenter<V> implements ErrorDialogMvpPresenter<V>  {


    @Inject
    public ErrorDialogPresenter(DataManagerHelper dataManager,
                                CompositeDisposable mCompositeDisposable) {
        super(dataManager, mCompositeDisposable);
    }

    @Override
    public void clearSharedPref() {

    }
}
