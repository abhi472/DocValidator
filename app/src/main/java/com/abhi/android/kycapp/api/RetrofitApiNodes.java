package com.abhi.android.kycapp.api;


import com.abhi.android.kycapp.model.vision.VisionResponse;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by abhishek on 22/12/17.
 */

public interface RetrofitApiNodes {


    @POST("v1/images:annotate")
    Observable<VisionResponse> getVisionResponse(@Query("key") String key,
                                                 @Body JsonObject body);
}
