package com.sunubismo.oleholeh.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ImageResponse {

    @SerializedName("data")
    private String mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("success")
    private Boolean mSuccess;

    public String getData() {
        return mData;
    }

    public void setData(String data) {
        mData = data;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public Boolean getSuccess() {
        return mSuccess;
    }

    public void setSuccess(Boolean success) {
        mSuccess = success;
    }

}
