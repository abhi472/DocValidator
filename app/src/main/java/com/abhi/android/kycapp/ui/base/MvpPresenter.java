package com.abhi.android.kycapp.ui.base;

/**
 * Created by abhishek on 26/12/17.
 */

public interface MvpPresenter<V extends MvpView> {

    void onAttach(V mvpView);

    void onDetach();

    void onBackClick();

}
