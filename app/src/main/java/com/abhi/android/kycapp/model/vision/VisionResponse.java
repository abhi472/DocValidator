package com.abhi.android.kycapp.model.vision;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by abhishek on 31/1/18.
 */

public class VisionResponse {

    @SerializedName("responses")
    @Expose
    private List<Response> responses = null;

    public List<Response> getResponses() {
        return responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }

}
