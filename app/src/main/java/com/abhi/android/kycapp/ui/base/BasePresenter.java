package com.abhi.android.kycapp.ui.base;


import com.abhi.android.kycapp.data.DataManagerHelper;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by abhishek on 26/12/17.
 */

public class BasePresenter<V extends MvpView> implements MvpPresenter<V> {


    private final DataManagerHelper mDataManager;
    private final CompositeDisposable mCompositeDisposable;
    private V mvpView;

    @Inject
    public BasePresenter(DataManagerHelper dataManager,
                         CompositeDisposable mCompositeDisposable) {
        this.mDataManager = dataManager;
        this.mCompositeDisposable = mCompositeDisposable;

    }

    public DataManagerHelper getDataManager() {
        return mDataManager;
    }

    public CompositeDisposable getCompositeDisposable() {
        return mCompositeDisposable;
    }

    @Override
    public void onAttach(V mvpView) {
        this.mvpView = mvpView;

    }

    @Override
    public void onDetach() {
        mCompositeDisposable.dispose();
        this.mvpView = null;
    }

    @Override
    public void onBackClick() {

        getMvpView().goBack();
    }



    public  V getMvpView() {
        return mvpView;
    }
}
