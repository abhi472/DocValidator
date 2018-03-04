package com.abhi.android.kycapp.ui.kyc_activity;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.abhi.android.kycapp.BuildConfig;
import com.abhi.android.kycapp.R;
import com.abhi.android.kycapp.data.DataManagerHelper;
import com.abhi.android.kycapp.model.vision.LabelAnnotation;
import com.abhi.android.kycapp.model.vision.VisionResponse;
import com.abhi.android.kycapp.ui.base.BasePresenter;
import com.abhi.android.kycapp.ui.dialogs.PhoneNumberDialog;
import com.abhi.android.kycapp.utils.BaseUtils;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by abhishek on 8/1/18.
 */

public class KycPresenter<V extends KycMvpView> extends BasePresenter<V>
        implements KycMvpPresenter<V> {

    private File file;

    @Inject
    public KycPresenter(DataManagerHelper dataManager, CompositeDisposable mCompositeDisposable) {
        super(dataManager, mCompositeDisposable);
    }

    @Override
    public void startDetection(File file) {

        if (getMvpView().isNetworkConnected()) {

            this.file = file;
            String filePath = file.getAbsolutePath();


            getMvpView().showLoading();

            Observable<JsonObject> observable = Observable.fromCallable(() -> {
                byte[] bytes = BaseUtils.readBytesFromFile(filePath);
                return Base64.encodeToString(bytes, Base64.DEFAULT);
            }).map(this::startApiCall).subscribeOn(Schedulers.io());


            Observable<VisionResponse> mapObservable = observable
                    .flatMap(jsonObject -> getDataManager()
                            .getVisionResponse("",
                                    jsonObject));

            getCompositeDisposable()
                    .add(mapObservable.observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(visionResponse -> {
                                if (isValid(visionResponse)) {
                                    getMvpView().hideLoading();
                                    getMvpView().showSuccess();
                                    getMvpView().enableButton();
                                } else {

                                    getMvpView().hideLoading();
                                    getMvpView().enableButton();
                                    getMvpView().onError("Please use a valid Photo ID", 0);

                                }
                            }, throwable -> {
                                getMvpView().hideLoading();
                                getMvpView().enableButton();
                                getMvpView()
                                        .onError("we're facing issues with content upload" +
                                                        "\nPlease try again",
                                                0);
                            })
                    );


        }
    }


    private JsonObject startApiCall(String result) {
        String json = "{\"requests\"" +
                ":[{\"image\":{\"content\":\""
                + result + "\"},\"features\":" +
                "[{\"type\": \"LABEL_DETECTION\", \"maxResults\": 10}]}]}";
        return (new JsonParser()).parse(json).getAsJsonObject();


    }

    @Override
    public void startUploadKyc(String number) {
        if (getMvpView().isNetworkConnected()) {
            getMvpView().showLoading();

            AmazonS3Client s3Client = new AmazonS3Client(getMvpView().getCredentialProvider());
            s3Client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
            TransferUtility transferUtility = getMvpView().getTransferUtility(s3Client);

            TransferObserver observer = transferUtility.upload(BuildConfig.BUCKET_NAME,
                    "kyc." + number + ".jpg",
                    file);

            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (state == TransferState.COMPLETED) {
                        getMvpView().hideLoading();
                        getMvpView().showSuccess();

                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    Log.d("check", "onProgressChanged: ");
                }

                @Override
                public void onError(int id, Exception ex) {
                    getMvpView().hideLoading();
                    getMvpView().enableButton();
                    getMvpView().onError();

                }
            });
        } else {
            getMvpView().onError(R.string.not_connected,
                    0);
        }


    }


    private boolean isValid(VisionResponse visionResponse) {

        List<LabelAnnotation> labelAnnotations = visionResponse
                .getResponses()
                .get(0)
                .getLabelAnnotations();

        for (LabelAnnotation annotation : labelAnnotations) {
            if (annotation.getDescription().equalsIgnoreCase("identity document"))
                return true;
        }
        return false;
    }


}
