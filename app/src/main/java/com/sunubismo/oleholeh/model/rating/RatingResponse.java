
package com.sunubismo.oleholeh.model.rating;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RatingResponse {

    @SerializedName("data")
    private List<Datum> mData;
    @SerializedName("message")
    private String mMessage;
    @SerializedName("success")
    private Boolean mSuccess;

    public List<Datum> getData() {
        return mData;
    }

    public void setData(List<Datum> data) {
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
