package com.sunubismo.oleholeh.model.user;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Datum {

    @SerializedName("email")
    private String mEmail;
    @SerializedName("gambar")
    private String mGambar;
    @SerializedName("id")
    private String mId;
    @SerializedName("nama")
    private String mNama;
    @SerializedName("token")
    private String mToken;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getGambar() {
        return mGambar;
    }

    public void setGambar(String gambar) {
        mGambar = gambar;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getNama() {
        return mNama;
    }

    public void setNama(String nama) {
        mNama = nama;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
    }

}
