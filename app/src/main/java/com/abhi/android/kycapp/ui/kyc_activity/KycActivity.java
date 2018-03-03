package com.abhi.android.kycapp.ui.kyc_activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abhi.android.kycapp.R;
import com.abhi.android.kycapp.custom.AutoFitTextureView;
import com.abhi.android.kycapp.custom.CameraSourcePreview;
import com.abhi.android.kycapp.custom.GraphicOverlay;
import com.abhi.android.kycapp.ui.base.BaseActivity;
import com.abhi.android.kycapp.ui.dialogs.PhoneNumberDialog;
import com.abhi.android.kycapp.utils.AwsConstants;
import com.abhi.android.kycapp.utils.CameraSource;
import com.abhi.android.kycapp.utils.DeviceDensity;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KycActivity extends BaseActivity implements KycMvpView {


    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int CROP_PIC = 100;
    private static final int RC_HANDLE_GMS = 2001;
    @BindView(R.id.faceOverlay)
    GraphicOverlay mGraphicOverlay;
    @BindView(R.id.preview)
    CameraSourcePreview mPreview;
    @BindView(R.id.topLayout)
    LinearLayout topLayout;

    private CameraSource mCameraSource = null;

    private File dir;
    private CameraSource.PictureCallback callback;


    @Inject
    KycMvpPresenter<KycMvpView> mPresenter;
    @BindView(R.id.texture)
    AutoFitTextureView texture;
    @BindView(R.id.btn_takepicture)
    ImageButton btnTakepicture;
    @BindView(R.id.powered)
    TextView powered;
    @BindView(R.id.back)
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_kyc_camera);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(this);
        setUp();
    }

    @Override
    protected void setUp() {

        callback = getPictureCallback();
        btnTakepicture.setOnClickListener(view -> {
            btnTakepicture.setEnabled(false);

            mCameraSource.takePicture(null, callback);
        });

        back.setOnClickListener(view -> {
            mPresenter.onBackClick();
        });


        if (isPermissionGranted()) {
            createCameraSource();

        } else {

            askForPermission();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                // close the app
                Toast.makeText(this,
                        "Sorry!!!, you can't use this app without granting permission",
                        Toast.LENGTH_LONG).show();
                finish();
            } else {
                createCameraSource();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void startCameraSource() {

        // check that the device has play services available.
        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
                getApplicationContext());
        if (code != ConnectionResult.SUCCESS) {
            Dialog dlg =
                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
            dlg.show();
        }

        if (mCameraSource != null) {
            try {
                mPreview.start(mCameraSource, mGraphicOverlay);
            } catch (IOException e) {
                Log.e(TAG, "Unable to start camera source.", e);
                mCameraSource.release();
                mCameraSource = null;
            }
        }
    }

    private void createCameraSource() {

        Context context = getApplicationContext();
        FaceDetector detector = new FaceDetector.Builder(context)
                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                .build();

        detector.setProcessor(
                new MultiProcessor.Builder<>(new GraphicFaceTrackerFactory())
                        .build());

        if (!detector.isOperational()) {
            // Note: The first time that an app using face API is installed on a device, GMS will
            // download a native library to the device in order to do detection.  Usually this
            // completes before the app is run for the first time.  But if that download has not yet
            // completed, then the above call will not detect any faces.
            //
            // isOperational() can be used to check if the required native library is currently
            // available.  The detector will automatically become operational once the library
            // download completes on device.
            Log.w(TAG, "Face detector dependencies are not yet available.");
        }

        mCameraSource = new CameraSource.Builder(context, detector)
                .setRequestedPreviewSize(640, 480)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedPreviewSize(1280, 1024)
                .setRequestedFps(2.0f)
                .setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
                .build();
    }


    public boolean isPermissionGranted() {
        return (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);


    }

    public void askForPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CAMERA_PERMISSION);

    }

    @Override
    protected void onResume() {
        super.onResume();

        startCameraSource();
    }

    /**
     * Stops the camera.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mPreview.stop();
    }

    /**
     * Releases the resources associated with the camera source, the associated detector, and the
     * rest of the processing pipeline.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
        }
    }

    public CameraSource.PictureCallback getPictureCallback() {
        return bytes -> {
            try {

                Bitmap loadedImage = null;
                Bitmap rotatedBitmap = null;
                loadedImage = BitmapFactory.decodeByteArray(bytes,
                        0,
                        bytes.length);

                int degrees = checkForRotation(bytes);



                Matrix rotateMatrix = new Matrix();



                switch (degrees) {
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotateMatrix.postRotate(270);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotateMatrix.postRotate(90);

                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotateMatrix.postRotate(0);
                        break;
                }

                // convert byte array into bitmap
                rotatedBitmap = Bitmap.createBitmap(loadedImage, 0, 0,
                        loadedImage.getWidth(), loadedImage.getHeight(),
                        rotateMatrix, false);


                startCropping(rotatedBitmap);
            } catch (Exception e) {
                KycActivity.this.startCameraSource();
                e.printStackTrace();
            }
        };
    }


    private int checkForRotation(byte[] bytes) {

        File imageFile = new File(getExternalFilesDir(null), "kyc.jpg");
        int orintation  = 11;
        try {

            BufferedOutputStream ostream = new BufferedOutputStream(new FileOutputStream(imageFile));

            // save image into gallery
            ostream.write(bytes);
            ostream.flush();
            ostream.close();

            ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());

            orintation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);


        } catch (Exception e) {
        }

        return orintation;

    }

    private void startCropping(Bitmap bitmapOrg) {
        try {
            File imageFile;


            Paint paint = new Paint();
            paint.setFilterBitmap(true);

            int targetWidth = bitmapOrg.getWidth();
            int targetHeight = bitmapOrg.getHeight();

            float aspectRatioWidth = targetWidth / DeviceDensity.getWidth(this);
            float aspectRatioHeight = targetHeight / DeviceDensity.getHeight(this);


            Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                    targetHeight,
                    Bitmap.Config.ARGB_8888);

            RectF rectf = new RectF(
                    (int) (aspectRatioWidth
                            * getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_left_margin)),
                    (int) (aspectRatioHeight
                            * getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_top_margin)),
                    (int) (aspectRatioWidth
                            * (getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_left_margin)
                            + getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_width))),
                    (int) (aspectRatioHeight
                            * (getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_top_margin)
                            + getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_height))));

            // RectF rectf = new RectF(200, 200, 200, 200);

            Canvas canvas = new Canvas(targetBitmap);
            Path path = new Path();

            path.addRect(rectf, Path.Direction.CW);
            canvas.clipPath(path);

            canvas.drawBitmap(bitmapOrg, new Rect(0,
                            0,
                            bitmapOrg.getWidth(),
                            bitmapOrg.getHeight()),
                    new Rect(0,
                            0,
                            targetWidth,
                            targetHeight),
                    paint);


            Matrix matrix = new Matrix();
            matrix.postScale(1f, 1f);
            Bitmap resizedBitmap = Bitmap.createBitmap(targetBitmap,
                    (int) (aspectRatioWidth
                            * getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_left_margin)),
                    (int) (aspectRatioHeight
                            * getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_top_margin)),
                    (int) (aspectRatioHeight
                            * getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_width)),
                    (int) (aspectRatioHeight
                            * getResources()
                            .getDimensionPixelSize(R.dimen.kyc_screen_overlay_height)),
                    matrix,
                    true);

            /*convert Bitmap to resource */

            imageFile = new File(getExternalFilesDir(null), "kyc.jpg");


            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
            // save image into gallery
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 95, ostream);
            FileOutputStream fout = new FileOutputStream(imageFile);
            fout.write(ostream.toByteArray());
            fout.close();
            mPresenter.startDetection(imageFile);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showPhoneDialog() {
        PhoneNumberDialog.newInstance().show(getSupportFragmentManager());
    }

    @Override
    public CognitoCachingCredentialsProvider getCredentialProvider() {
        return new CognitoCachingCredentialsProvider(
                getApplicationContext(), // Application Context
                AwsConstants.COGNITO_POOL_ID, // Identity Pool ID
                Regions.AP_SOUTH_1 // Region enum
        );
    }

    @Override
    public TransferUtility getTransferUtility(AmazonS3Client s3Client) {
        return new TransferUtility(s3Client, getApplicationContext());
    }



    @Override
    public void enableButton() {
        btnTakepicture.setEnabled(true);
    }

    @Override
    public void showSuccess() {
        Toast.makeText(this, "upload successful", Toast.LENGTH_SHORT).show();
    }

    public void setNumber(String s) {
        mPresenter.startUploadKyc(s);
    }

    //==============================================================================================
    // Graphic Face Tracker
    //==============================================================================================

    /**
     * Factory for creating a face tracker to be associated with a new face.  The multiprocessor
     * uses this factory to create face trackers as needed -- one for each individual.
     */
    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {
        @Override
        public Tracker<Face> create(Face face) {
            return new GraphicFaceTracker();
        }
    }

    /**
     * Face tracker for each detected individual. This maintains a face graphic within the app's
     * associated face overlay.
     */
    private class GraphicFaceTracker extends Tracker<Face> {

        /**
         * Start tracking the detected face instance within the face overlay.
         */
        @Override
        public void onNewItem(int faceId, Face item) {

            // mCameraSource.takePicture(null, callback);

        }

        /**
         * Update the position/characteristics of the face within the overlay.
         */
        @Override
        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {
        }

        /**
         * Hide the graphic when the corresponding face was not detected.  This can happen for
         * intermediate frames temporarily (e.g., if the face was momentarily blocked from
         * view).
         */
        @Override
        public void onMissing(FaceDetector.Detections<Face> detectionResults) {
        }

        /**
         * Called when the face is assumed to be gone for good. Remove the graphic annotation from
         * the overlay.
         */
        @Override
        public void onDone() {
        }
    }

    @Override
    public void OnErrorDialogDismiss(int tag) {

    }
}
