package com.abhi.android.kycapp.ui.error;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.abhi.android.kycapp.R;
import com.abhi.android.kycapp.di.components.ActivityComponent;
import com.abhi.android.kycapp.ui.base.BaseActivity;
import com.abhi.android.kycapp.ui.base.BaseDialog;
import com.google.api.services.vision.v1.Vision;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by abhishek on 1/2/18.
 */

public class ErrorDialog extends BaseDialog implements ErrorDialogMvpView {

    public static final String TAG = "ErrorDialog";

    @Inject
    ErrorDialogMvpPresenter<ErrorDialogMvpView> mPresenter;

    @BindView(R.id.header)
    TextView header;
    @BindView(R.id.v1)
    View v1;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.okay)
    Button okay;
    int tag = 0;
    private String messageText;
    private BaseActivity mActivity;
    private String headerText;
    private ErrorDialog dialog;

    public static ErrorDialog newInstance(String message, int tag) {
        ErrorDialog fragment = new ErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putInt("tag", tag);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ErrorDialog newInstance(String head, String message, int tag) {
        ErrorDialog fragment = new ErrorDialog();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        bundle.putInt("tag", tag);
        bundle.putString("header", head);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    public static ErrorDialog newInstance() {
        return new ErrorDialog();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.error_dialog,
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
        mPresenter.onDetach();
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: startNetworkFetch");
    }


    @Override
    public void onFragmentDetached(String tag) {

    }


    @Override
    public Vision getVision() {
        return null;
    }

    @Override
    protected void setUp(View view) {


        setCancelable(false);



        Bundle bundle = getArguments();
        if (bundle != null) {
            messageText = bundle.getString("message");
            tag = bundle.getInt("tag");
            headerText = bundle.getString("header");
        }


        if (messageText != null)
            message.setText(messageText);

        if (headerText != null)
            header.setText(headerText);

        okay.setOnClickListener(view1 -> {

            dismissDialog(TAG);

            });
    }


    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }


}
