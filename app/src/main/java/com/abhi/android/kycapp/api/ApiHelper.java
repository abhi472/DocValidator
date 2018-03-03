package com.abhi.android.kycapp.api;


import com.abhi.android.kycapp.model.vision.VisionResponse;
import com.google.gson.JsonObject;

import io.reactivex.Observable;

/**
 * Created by abhishek on 27/12/17.
 */

public interface ApiHelper {


    Observable<VisionResponse> getVisionResponse(String key,
                                                 JsonObject jsonObject);
}
