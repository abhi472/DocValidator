package com.abhi.android.kycapp.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.abhi.android.kycapp.R;

/**
 * Created by abhishek on 1/3/18.
 */

public class OverlayView extends LinearLayout {
    private Bitmap bitmap;
    private boolean kycMode;

    public OverlayView(Context context) {
        super(context);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.OverlayView,
                0,
                0);
        try {
            kycMode = !(ta.getBoolean(R.styleable.OverlayView_circular, false));
        } finally {
            ta.recycle();
        }
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public OverlayView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (bitmap == null) {
            if (kycMode)
                createRectWindowFrame();
            else
                createWindowFrame();
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    protected void createWindowFrame() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.translucent));
        paint.setAlpha(99);
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        float centerX = getWidth() / 2;
        float radius = getResources().getDimensionPixelSize(R.dimen.camera_focus_radius);
        float centerY = getHeight()/2;

        osCanvas.drawCircle(centerX, centerY, radius, paint);
    }

    protected void createRectWindowFrame() {
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas osCanvas = new Canvas(bitmap);

        RectF outerRectangle = new RectF(0, 0, getWidth(), getHeight());

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.translucent));
        paint.setAlpha(99);
        osCanvas.drawRect(outerRectangle, paint);

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        osCanvas.drawRect(
                getResources().getDimensionPixelSize(R.dimen.kyc_screen_overlay_left_margin),
                getResources().getDimensionPixelSize(R.dimen.kyc_screen_overlay_top_margin),
                getResources().getDimensionPixelSize(R.dimen.kyc_screen_overlay_left_margin)
                        + getResources().getDimensionPixelSize(R.dimen.kyc_screen_overlay_width),
                getResources().getDimensionPixelSize(R.dimen.kyc_screen_overlay_top_margin)
                        + getResources().getDimensionPixelSize(R.dimen.kyc_screen_overlay_height),
                paint);
    }

    public void setKycMode(Boolean b) {
        kycMode = b;
    }

    @Override
    public boolean isInEditMode() {
        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        bitmap = null;
    }
}
