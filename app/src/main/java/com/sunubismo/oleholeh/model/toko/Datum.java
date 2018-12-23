
package com.sunubismo.oleholeh.model.toko;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Datum implements Serializable {

    @SerializedName("alamat")
    private String mAlamat;
    @SerializedName("deskripsi")
    private String mDeskripsi;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("gambar")
    private String mGambar;
    @SerializedName("hari_buka")
    private String mHariBuka;
    @SerializedName("id")
    private String mId;
    @SerializedName("jam_kerja")
    private String mJamKerja;
    @SerializedName("lat")
    private Double mLat;
    @SerializedName("lng")
    private Double mLng;
    @SerializedName("n_rating")
    private Float mNRating;
    @SerializedName("nama")
    private String mNama;
    @SerializedName("telepon")
    private String mTelepon;

    private double mDistance;

    public String getAlamat() {
        return mAlamat;
    }

    public void setAlamat(String alamat) {
        mAlamat = alamat;
    }

    public String getDeskripsi() {
        return mDeskripsi;
    }

    public void serDeskripsi(String deskripsi) {
        mDeskripsi = deskripsi;
    }

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

    public String getHariBuka() {
        return mHariBuka;
    }

    public void setHariBuka(String hariBuka) {
        mHariBuka = hariBuka;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getJamKerja() {
        return mJamKerja;
    }

    public void setJamKerja(String jamKerja) {
        mJamKerja = jamKerja;
    }

    public Double getLat() {
        return mLat;
    }

    public void setLat(Double lat) {
        mLat = lat;
    }

    public Double getLng() {
        return mLng;
    }

    public void setLng(Double lng) {
        mLng = lng;
    }

    public Float getNRating() {
        return mNRating;
    }

    public void setNRating(Float nRating) {
        mNRating = nRating;
    }

    public String getNama() {
        return mNama;
    }

    public void setNama(String nama) {
        mNama = nama;
    }

    public String getTelepon() {
        return mTelepon;
    }

    public void setTelepon(String telepon) {
        mTelepon = telepon;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        this.mDistance = distance;
    }
}
