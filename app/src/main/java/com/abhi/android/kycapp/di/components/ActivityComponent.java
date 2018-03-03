package com.abhi.android.kycapp.di.components;


import com.abhi.android.kycapp.KYCApp;
import com.abhi.android.kycapp.di.PerActivity;
import com.abhi.android.kycapp.di.modules.ActivityModule;
import com.abhi.android.kycapp.ui.dialogs.PhoneNumberDialog;
import com.abhi.android.kycapp.ui.error.ErrorDialog;
import com.abhi.android.kycapp.ui.kyc_activity.KycActivity;

import dagger.Component;

/**
 * Created by abhishek on 26/12/17.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(KycActivity activity);

    void inject(ErrorDialog dialog);

    void inject(PhoneNumberDialog dialog);


}
