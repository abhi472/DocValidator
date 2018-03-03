package com.abhi.android.kycapp.ui.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.abhi.android.kycapp.R;
import com.abhi.android.kycapp.di.components.ActivityComponent;
import com.abhi.android.kycapp.ui.base.BaseActivity;
import com.abhi.android.kycapp.ui.base.BaseDialog;
import com.abhi.android.kycapp.ui.kyc_activity.KycActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by abhishek on 3/3/18.
 */

public class PhoneNumberDialog
        extends BaseDialog
        implements PhoneNumberDialogMvpView {

    private String TAG = "phone_number_dialog";

    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.contact)
    EditText contact;
    @BindView(R.id.okay)
    Button okay;
    Unbinder unbinder;
    private KycActivity mActivity;

    @Inject
    PhoneNumberDialogMvpPresenter<PhoneNumberDialogMvpView> mPresenter;


    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    protected void setUp(View view) {

        okay.setOnClickListener(view1 -> {
            mPresenter.onNextClick(contact.getText().toString());

        });

    }

    @Override
    public void onFieldError(String message) {
        contact.setError(message);
    }

    public static PhoneNumberDialog newInstance() {
        return new PhoneNumberDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (KycActivity) context;
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.phone_dialog,
                container,
                true);

        ActivityComponent component = getActivityComponent();
        if (component != null) {

            component.inject(this);

            setUnBinder(ButterKnife.bind(this, view));

            mPresenter.onAttach(this);
        }

        setUp(view);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setNumber(String s) {
        mActivity.setNumber(s);
        dismissDialog(TAG);
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

}
