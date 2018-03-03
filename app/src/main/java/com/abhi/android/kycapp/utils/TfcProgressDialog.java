package com.abhi.android.kycapp.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;

import com.abhi.android.kycapp.R;


/**
 * Created by abhishek on 26/12/17.
 */

public class TfcProgressDialog {
    private Context ctx;
    private Dialog dialog;
    private View v;

    private void init(final Context ctx) {
        this.ctx = ctx;
        dialog = new Dialog(ctx);

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.progress_dialog);
        if (dialog.getWindow() != null)
            v = dialog.getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);


    }

    public void showDailyPopup(Context ctx) {
        init(ctx);
        dialog.show();
    }

    public void dismissPopup() {
        if (dialog != null)
            dialog.dismiss();
    }

    public boolean isShowing() {
        return dialog != null && dialog.isShowing();
    }
}
